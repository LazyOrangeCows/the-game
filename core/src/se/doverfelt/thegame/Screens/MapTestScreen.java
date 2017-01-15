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
        initWorldBodies();
    }

    @Override
    public void show() {

    }

    private void initWorldBodies() {
        for (MapLayer l : map.getLayers()){
            for (MapObject obj : l.getObjects()) {
                if (obj instanceof TextureMapObject) {
                    continue;
                }

                if (obj instanceof PolygonMapObject) {
                    addPolygonBody((PolygonMapObject) obj);
                } else if (obj instanceof RectangleMapObject) {
                    addRectangleBody((RectangleMapObject) obj);
                } else if (obj instanceof PolylineMapObject) {
                    addPolylineBody((PolylineMapObject) obj);
                } else if (obj instanceof CircleMapObject) {
                    addCircleBody((CircleMapObject) obj);
                }
            }
        }
    }

    private void addCircleBody(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / PPM);
        circleShape.setPosition(new Vector2(circle.x / PPM, circle.y / PPM));
        addStaticBody(circleShape);
        circleShape.dispose();
    }

    private void addPolylineBody(PolylineMapObject obj) {
        float[] vertices = obj.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / PPM;
            worldVertices[i].y = vertices[i * 2 + 1] / PPM;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        addStaticBody(chain);
        chain.dispose();
    }

    private void addRectangleBody(RectangleMapObject obj) {
        Rectangle rectangle = obj.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / PPM,
                (rectangle.y + rectangle.height * 0.5f ) / PPM);
        polygon.setAsBox(rectangle.width * 0.5f / PPM,
                rectangle.height * 0.5f / PPM,
                size,
                0.0f);
        addStaticBody(polygon);
        polygon.dispose();
    }

    private void addPolygonBody(PolygonMapObject obj) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = obj.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            System.out.println(vertices[i]);
            worldVertices[i] = vertices[i] / PPM;
        }

        polygon.set(worldVertices);
        addStaticBody(polygon);
        polygon.dispose();
    }

    private void addStaticBody(Shape shape) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(def);
        body.createFixture(shape, 1);
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
