package se.doverfelt.thegame.utils;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by rickard on 2017-01-16.
 */
public class MapUtils {

    public static final float PPM = 128;

    public static void populateWorld(World world, TiledMap map) {
        for (MapLayer l : map.getLayers()){
            if (!l.getName().equals("Collision")) {
                continue;
            }
            for (MapObject obj : l.getObjects()) {
                if (obj instanceof TextureMapObject) {
                    continue;
                }

                if (obj instanceof PolygonMapObject) {
                    addPolygonBody((PolygonMapObject) obj, world);
                } else if (obj instanceof RectangleMapObject) {
                    addRectangleBody((RectangleMapObject) obj, world);
                } else if (obj instanceof PolylineMapObject) {
                    addPolylineBody((PolylineMapObject) obj, world);
                } else if (obj instanceof CircleMapObject) {
                    addCircleBody((CircleMapObject) obj, world);
                }
            }
        }
    }

    private static void addCircleBody(CircleMapObject circleObject, World world) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / PPM);
        circleShape.setPosition(new Vector2(circle.x / PPM, circle.y / PPM));
        addStaticBody(circleShape, world);
        circleShape.dispose();
    }

    private static void addPolylineBody(PolylineMapObject obj, World world) {
        float[] vertices = obj.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / PPM;
            worldVertices[i].y = vertices[i * 2 + 1] / PPM;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        addStaticBody(chain, world);
        chain.dispose();
    }

    private static void addRectangleBody(RectangleMapObject obj, World world) {
        Rectangle rectangle = obj.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / PPM,
                (rectangle.y + rectangle.height * 0.5f ) / PPM);
        polygon.setAsBox(rectangle.width * 0.5f / PPM,
                rectangle.height * 0.5f / PPM,
                size,
                0.0f);
        addStaticBody(polygon, world);
        polygon.dispose();
    }

    private static void addPolygonBody(PolygonMapObject obj, World world) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = obj.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            System.out.println(vertices[i]);
            worldVertices[i] = vertices[i] / PPM;
        }

        polygon.set(worldVertices);
        addStaticBody(polygon, world);
        polygon.dispose();
    }

    private static void addStaticBody(Shape shape, World world) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(def);
        body.createFixture(shape, 1);
    }

}