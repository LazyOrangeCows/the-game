package se.doverfelt.thegame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import se.doverfelt.thegame.client.InputManager;
import se.doverfelt.thegame.TheGame;
import se.doverfelt.thegame.net.packet.Packet5Message;
import se.doverfelt.thegame.net.packet.PacketWrapper;
import se.doverfelt.thegame.net.util.PacketListener;

import java.io.IOException;

/**
 * Created by rickard on 2016-09-30.
 */
public class InputTestScreen implements CScreen {

    private final TheGame theGame;
    private final InputManager inputHandler;
    private String text = "";
    private BitmapFont font = new BitmapFont();
    private String textMouse = "";

    public InputTestScreen(TheGame theGame) {
        this.theGame = theGame;
        this.inputHandler = new InputManager(theGame);

        for (Controller controller : Controllers.getControllers()) {
            inputHandler.setController(controller);
        }



        inputHandler.addKey(Input.Keys.A, InputManager.Event.WALK_LEFT);
        inputHandler.addKey(Input.Keys.W, InputManager.Event.JUMP);
        inputHandler.addKey(Input.Keys.D, InputManager.Event.WALK_RIGHT);
        inputHandler.addButton(Input.Buttons.LEFT, InputManager.Event.FIRE);
        inputHandler.addButton(Input.Buttons.RIGHT, InputManager.Event.SECONDARY_FIRE);
        theGame.client.addPacketListener(new PacketListener() {
            @Override
            public void handlePacket(PacketWrapper packet) {
                if (packet.packet instanceof Packet5Message) {
                    if (((Packet5Message) packet.packet).channel.equals("KeyPressed")) {
                        text = ((Packet5Message) packet.packet).message;
                    } else if (((Packet5Message) packet.packet).channel.equals("Mouse")) {
                        textMouse = ((Packet5Message) packet.packet).message;
                    }
                }
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        try {
            inputHandler.poll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        batch.begin();
        font.draw(batch, text, 50, 50);
        font.draw(batch, textMouse, 50, 80);
        batch.end();
    }
}
