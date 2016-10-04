package se.doverfelt.thegame.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Json;
import se.doverfelt.thegame.net.packet.Packet;
import se.doverfelt.thegame.net.packet.Packet3Disconnected;
import se.doverfelt.thegame.net.packet.PacketWrapper;
import se.doverfelt.thegame.net.util.PacketListener;
import se.doverfelt.thegame.net.util.PacketListenerThread;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by rickard on 2016-09-29.
 */
public class Client {

    private Socket socket;
    private ArrayList<PacketListener> packetListeners = new ArrayList<PacketListener>();
    private BufferedReader reader;
    private BufferedWriter writer;
    private Json json;
    private PacketListenerThread listener;
    public long lastPacket = 0;
    public int ping;

    public Client(String host, int port) {
        json = new Json();
        SocketHints hints = new SocketHints();
        hints.keepAlive = true;
        socket = Gdx.net.newClientSocket(Net.Protocol.TCP, host, port, hints);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        startListening();
    }

    public void send(Packet packet) throws IOException {
        PacketWrapper wrapper = new PacketWrapper();
        packet.timestamp = System.currentTimeMillis();
        wrapper.packet = packet;
        writer.write(json.toJson(wrapper));
        writer.newLine();
        writer.flush();
        lastPacket = System.currentTimeMillis();
    }

    public int getPing() {
        return ping;
    }

    public void addPacketListener(PacketListener listener) {
        this.listener.addListener(listener);
    }

    private void startListening() {
        listener = new PacketListenerThread(socket, this);
        Thread t = new Thread(listener, "packetListener");
        t.start();
        addPacketListener(new PacketListener() {
            @Override
            public void handlePacket(PacketWrapper packet) {
                if (packet.packet instanceof Packet3Disconnected) {
                    listener.stop();
                    socket.dispose();
                }
            }
        });
    }

    public void close() {
        try {
            send(new Packet3Disconnected());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
