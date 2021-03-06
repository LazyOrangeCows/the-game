package se.doverfelt.thegame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import se.doverfelt.thegame.TheGame;

/**
 * Created by Robin on 2016-09-30.
 */
public class LoadingScreen implements CScreen{
    SpriteBatch batch;
    Texture img;
    TheGame game;
    AssetManager manager;
    BitmapFont font;
    public LoadingScreen(TheGame game){
        this.game = game;
        manager = game.assets.getAssetManager();
        this.batch = batch;
    }
    @Override
    public void show() {
        font = new BitmapFont();
        //manager.finishLoadingAsset("harambe.png");


    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        if (manager.update()){
            game.setScreen(game.screens.get("map"));
        }
        String s = + ( 100 *manager.getProgress()) + "%";
        System.out.println(s);
        batch.begin();
        font.draw(batch, s,20,20);
        batch.end();
    }
    //public void update(float delta){
    //
    //}

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
