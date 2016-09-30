package se.doverfelt.thegame.net.util;

import se.doverfelt.thegame.net.packet.Packet;
import se.doverfelt.thegame.net.packet.PacketWrapper;

/**
 * Created by rickard on 2016-09-30.
 */
public interface PacketListener {
    void handlePacket(PacketWrapper packet);
}
