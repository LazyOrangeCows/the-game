package se.doverfelt.thegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import se.doverfelt.thegame.net.packet.Packet4KeyPressed;

import java.io.IOException;

/**
 * Created by rickard on 2016-09-30.
 */
public class InputHandler {

    private final TheGame theGame;

    public InputHandler(TheGame theGame) {
        this.theGame = theGame;
    }

    public void poll() throws IOException {
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            Packet4KeyPressed keyPressed = new Packet4KeyPressed();
            keyPressed.key = Input.Keys.A;
            theGame.client.send(keyPressed);
        }
    }

}
