package se.doverfelt.thegame.net.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Json;
import se.doverfelt.thegame.net.Client;
import se.doverfelt.thegame.net.packet.PacketWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by rickard on 2016-09-30.
 */
public class PacketListenerThread implements Runnable {
    private Socket socket;
    private Json json;
    private BufferedReader reader;
    private ArrayList<PacketListener> packetListeners = new ArrayList<PacketListener>();
    private boolean shouldRun = true;
    private final Client client;

    public PacketListenerThread(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
        json = new Json();
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void addListener(PacketListener listener) {
        packetListeners.add(listener);
    }

    @Override
    public void run() {
        while (!socket.isConnected());
        while (shouldRun) {
            try {
                PacketWrapper p = json.fromJson(PacketWrapper.class, reader.readLine());
                //Gdx.app.log("Client", p.toString());
                client.ping = (int) (System.currentTimeMillis() - client.lastPacket);
                for (PacketListener packetListener : packetListeners) {
                    packetListener.handlePacket(p);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        shouldRun = false;
    }
}
