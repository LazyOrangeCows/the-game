package se.doverfelt.thegame.net.packet;

import com.badlogic.gdx.Input;
import se.doverfelt.thegame.client.InputManager;

/**
 * Created by rickard on 2016-09-30.
 */
public class Packet4KeyPressed extends Packet {
    public InputManager.Event event;
    public boolean ctrl;
    public boolean shift;
    public boolean alt;
}
