package se.doverfelt.thegame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import se.doverfelt.thegame.TheGame;

/**
 * Created by Robin on 2016-09-30.
 */
public class MainMenu implements CScreen {
    TheGame game;
    AssetManager manager;
    Texture img;
    public MainMenu(TheGame game){
        this.game = game;
        manager = game.assets.getAssetManager();
    }
    @Override
    public void show() {
        img = manager.get("harambe.png");
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void render(float delta, SpriteBatch batch) {

        batch.begin();
        batch.draw(img, 0, 0);
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
