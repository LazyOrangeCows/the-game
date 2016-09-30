package se.doverfelt.thegame.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import se.doverfelt.thegame.TheGame;
import se.doverfelt.thegame.net.packet.Packet4KeyPressed;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by rickard on 2016-09-30.
 */
public class InputManager {

    private final TheGame theGame;

    private ArrayList<Integer> keys = new ArrayList<Integer>();

    public InputManager(TheGame theGame) {
        this.theGame = theGame;
    }

    public void addKey(int key) {
        keys.add(key);
    }

    public void poll() throws IOException {
        for (int key : keys) {
            if (Gdx.input.isKeyJustPressed(key)) {
                Packet4KeyPressed keyPressed = new Packet4KeyPressed();
                keyPressed.key = key;
                keyPressed.ctrl = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
                keyPressed.alt = Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT);
                keyPressed.shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
                theGame.client.send(keyPressed);
            }
        }
    }

}
