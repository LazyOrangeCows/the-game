package se.doverfelt.thegame.net.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Json;
import se.doverfelt.thegame.net.Server;
import se.doverfelt.thegame.net.packet.Packet;
import se.doverfelt.thegame.net.packet.PacketWrapper;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by rickard on 2016-09-29.
 */
public class ClientConnection {

    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final Json json;
    private ArrayList<PacketListener> packetListeners;
    private final Server server;
    private Thread t;
    private boolean connected = false;

    /**
     * @param socket The Client socket
     * @param packetListeners The current packet listeners
     * @param server The server
     */
    public ClientConnection(Socket socket, ArrayList<PacketListener> packetListeners, Server server) {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.json = new Json();
        this.packetListeners = packetListeners;
        this.server = server;
        while (!socket.isConnected());
        connected = true;
        startListening();
    }

    /**
     * @param listener New packet listener
     */
    public void addListener(PacketListener listener) {
        this.packetListeners.add(listener);
    }

    /**
     * @param packet Packet to send
     * @throws IOException Something went wrong
     */
    public void send(Packet packet) throws IOException {
        PacketWrapper wrapper = new PacketWrapper();
        wrapper.packet = packet;
        writer.write(json.toJson(wrapper));
        writer.newLine();
        Gdx.app.log("ClientConncection - " + socket.getRemoteAddress(), json.toJson(packet));
        writer.flush();
    }

    private void startListening() {
        Runnable listener = new Runnable() {
            @Override
            public void run() {
                while (connected) {
                    try {
                        PacketWrapper p = json.fromJson(PacketWrapper.class, reader.readLine());
                        server.handle(p, socket.getRemoteAddress());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t = new Thread(listener, "packetListenerClient-"+socket.getRemoteAddress());
        t.start();
    }

    /**
     * Close the connection
     */
    public void close() {
        connected = false;
    }
}
