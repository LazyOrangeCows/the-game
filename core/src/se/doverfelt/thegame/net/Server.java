package se.doverfelt.thegame.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Json;
import se.doverfelt.thegame.net.packet.*;
import se.doverfelt.thegame.net.util.ClientConnection;
import se.doverfelt.thegame.net.util.PacketListener;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Created by rickard on 2016-09-29.
 */
public class Server {

    private final ServerSocket serverSocket;
    private final SocketHints socketHints;
    private ArrayList<PacketListener> packetListeners = new ArrayList<PacketListener>();
    private HashMap<String, ClientConnection> connections = new HashMap<String, ClientConnection>();
    private Json json;

    /**
     * @param host
     * @param port
     */
    public Server(String host, int port) {
        json = new Json();
        ServerSocketHints hints = new ServerSocketHints();
        hints.acceptTimeout = 0;
        socketHints = new SocketHints();
        socketHints.keepAlive = true;
        serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, host, port, hints);
        startAccepting();
    }

    /**
     * Send a packet
     * @param packet The packet {@link Packet}
     * @param address The receiving address
     * @throws IOException Exception...
     */
    public void send(Packet packet, String address) throws IOException {
        connections.get(address).send(packet);
    }

    /**
     * @param packet The packet {@link Packet}
     * @throws IOException
     */
    public void broadcast(Packet packet) throws IOException {
        for (ClientConnection c : connections.values()) {
            try {
                c.send(packet);
            } catch (IOException e) {
                c.close();
                connections.remove(c.getAddress());
                throw e;
            }
        }
    }

    /**
     *
     * @param listener The packet listener
     */
    public void addPacketListener(PacketListener listener) {
        packetListeners.add(listener);
        for (ClientConnection c : connections.values()) {
            c.addListener(listener);
        }
    }

    private void startAccepting() {
        final Server server = this;
        Runnable acceptor = new Runnable() {
            @SuppressWarnings("InfiniteLoopStatement")
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
            return;
        }

        if (p.packet instanceof Packet4KeyPressed) {
            Packet5Message packet5Message = new Packet5Message();
            packet5Message.message = ((Packet4KeyPressed) p.packet).event.name();
            packet5Message.channel = "KeyPressed";
            try {
                broadcast(packet5Message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (p.packet instanceof Packet6MouseMoved) {
            Packet5Message packet5Message = new Packet5Message();
            packet5Message.message = "X: " + ((Packet6MouseMoved) p.packet).x + " Y: " + ((Packet6MouseMoved) p.packet).y;
            packet5Message.channel = "Mouse";
            Packet7TexturePosition packet7TexturePosition = new Packet7TexturePosition();
            packet7TexturePosition.texture = "badlogic.jpg";
            packet7TexturePosition.x = ((Packet6MouseMoved) p.packet).x;
            packet7TexturePosition.y = ((Packet6MouseMoved) p.packet).y;
            try {
                broadcast(packet5Message);
                broadcast(packet7TexturePosition);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            //Gdx.app.log("Server", "Recieved packet" + p.packet);
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

    /**
     * @return The address of the local host in LAN
     * @throws SocketException Something went wrong...
     */
    public String getAddress() throws SocketException {
        for (final Enumeration< NetworkInterface > interfaces = NetworkInterface.getNetworkInterfaces( ); interfaces.hasMoreElements( ); ) {
            final NetworkInterface cur = interfaces.nextElement( );

            if (cur.isLoopback()) {
                continue;
            }

            for ( final InterfaceAddress addr : cur.getInterfaceAddresses()) {
                final InetAddress inetAddr = addr.getAddress();

                if (!( inetAddr instanceof Inet4Address) )
                {
                    continue;
                }

                if (!(inetAddr.isMulticastAddress()) && !(inetAddr.isMulticastAddress()) && !(inetAddr.isAnyLocalAddress()) && inetAddr.isSiteLocalAddress()) {
                    return inetAddr.getHostAddress();
                }

            }
        }
        return null;
    }
}
