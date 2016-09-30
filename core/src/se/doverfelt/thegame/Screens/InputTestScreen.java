package se.doverfelt.thegame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
public class InputTestScreen implements Screen {

    private final TheGame theGame;
    private final InputManager inputHandler;
    private String text = "";
    private SpriteBatch batch = new SpriteBatch();
    private BitmapFont font = new BitmapFont();

    public InputTestScreen(TheGame theGame) {
        this.theGame = theGame;
        this.inputHandler = new InputManager(theGame);
        inputHandler.addKey(Input.Keys.A);
        inputHandler.addKey(Input.Keys.W);
        inputHandler.addKey(Input.Keys.S);
        inputHandler.addKey(Input.Keys.D);
        theGame.client.addPacketListener(new PacketListener() {
            @Override
            public void handlePacket(PacketWrapper packet) {
                if (packet.packet instanceof Packet5Message) {
                    text = ((Packet5Message) packet.packet).message;
                }
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        try {
            inputHandler.poll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        batch.begin();
        font.draw(batch, text, 50, 50);
        batch.end();
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
}
