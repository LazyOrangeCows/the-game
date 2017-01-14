package se.doverfelt.thegame.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Created by rickard on 2016-09-30.
 */
public class MapTestScreen implements Screen {
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    SpriteBatch batch;
    OrthographicCamera camera;

    public MapTestScreen() {
        batch = new SpriteBatch();
        map = new TmxMapLoader().load("test3.0.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, batch);
        camera = new OrthographicCamera(32*30, 32*15);
        camera.setToOrtho(false, 32*30, 32*15);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        renderer.setView(camera);
        renderer.render();
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
