package se.doverfelt.thegame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import se.doverfelt.thegame.TheGame;
import se.doverfelt.thegame.utils.MapUtils;

/**
 * Created by rickard on 2016-09-30.
 */
public class MapTestScreen implements Screen {
    private static final float MAX_VELOCITY = 7;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    SpriteBatch batch;
    OrthographicCamera camera;
    TheGame theGame;
    World world;
    Body player;
    Fixture playerFixture;
    Box2DDebugRenderer debug = new Box2DDebugRenderer();
    Array<Body> bodies = new Array<Body>();

    static final float PPM = 128;

    public MapTestScreen(TheGame theGame) {
        this.theGame = theGame;
        batch = new SpriteBatch();
        map = new TmxMapLoader().load("test5.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/PPM, batch);
        camera = new OrthographicCamera(16, 8);
        camera.setToOrtho(false, 16, 8);
        world = new World(new Vector2(0, -9.82f), true);
        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(16, 16);
        player = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.5f, 0.8f);
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0;
        playerFixture = player.createFixture(fixtureDef);
        polygonShape.dispose();
        MapUtils.populateWorld(world, map);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.D) && player.getLinearVelocity().x < MAX_VELOCITY) {
            player.applyLinearImpulse(0.8f, 0, player.getPosition().x, player.getPosition().y, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) && player.getLinearVelocity().x > -MAX_VELOCITY) {
            player.applyLinearImpulse(-0.8f, 0, player.getPosition().x, player.getPosition().y, true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.applyForceToCenter(0, 800, true);
        }
        camera.position.set(player.getPosition(), 1);
        camera.update();
        renderer.setView(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(theGame.assets.getAssetManager().get("bg.jpg", Texture.class), renderer.getViewBounds().getX(), renderer.getViewBounds().getY(), renderer.getViewBounds().getWidth(), renderer.getViewBounds().getHeight());
        batch.end();
        renderer.render();
        debug.render(world, camera.combined);
        world.step(1/60f, 6, 2);
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
