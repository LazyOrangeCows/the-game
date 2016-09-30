package se.doverfelt.thegame.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Json;
import se.doverfelt.thegame.net.packet.Packet;
import se.doverfelt.thegame.net.packet.Packet1Accepted;
import se.doverfelt.thegame.net.packet.Packet3Disconnected;
import se.doverfelt.thegame.net.packet.PacketWrapper;
import se.doverfelt.thegame.net.util.ClientConnection;
import se.doverfelt.thegame.net.util.PacketListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rickard on 2016-09-29.
 */
public class Server {

    private final ServerSocket serverSocket;
    private final SocketHints socketHints;
    private Socket socket;
    private ArrayList<PacketListener> packetListeners = new ArrayList<PacketListener>();
    private HashMap<String, ClientConnection> connections = new HashMap<String, ClientConnection>();
    private Json json;

    public Server(String host, int port) {
        json = new Json();
        ServerSocketHints hints = new ServerSocketHints();
        hints.acceptTimeout = 0;
        socketHints = new SocketHints();
        socketHints.keepAlive = true;
        serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, host, port, hints);
        startAccepting();
    }

    public void send(Packet packet, String address) throws IOException {
        connections.get(address).send(packet);
    }

    public void broadcast(Packet packet) throws IOException {
        for (ClientConnection c : connections.values()) {
            c.send(packet);
        }
    }

    public void addPacketListener(PacketListener listener) {
        packetListeners.add(listener);
        for (ClientConnection c : connections.values()) {
            c.addListener(listener);
        }
    }

    public void startAccepting() {
        final Server server = this;
        Runnable acceptor = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Socket socket = serverSocket.accept(null);
                    Gdx.app.log("Server", "New connection: " + socket.getRemoteAddress());
                    connections.put(socket.getRemoteAddress(), new ClientConnection(socket, packetListeners, server));
                    try {
                        connections.get(socket.getRemoteAddress()).send(new Packet1Accepted());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t = new Thread(acceptor);
        t.start();
    }

    public void handle(PacketWrapper p, String source) {
        if (p.packet instanceof Packet3Disconnected) {
            connections.remove(source).close();
        }

        try {
            Gdx.app.log("Server", "Recieved packet" + p.packet);
            broadcast(p.packet);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            broadcast(new Packet3Disconnected());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
