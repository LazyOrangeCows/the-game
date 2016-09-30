package se.doverfelt.thegame.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import se.doverfelt.thegame.world.Flag;
import se.doverfelt.thegame.world.Ground;
import se.doverfelt.thegame.world.Tile;

import java.util.ArrayList;

/**
 * Created by Edvin Bergstrom on 2016-09-30.
 * Beskrivning
 */
public class ServerWorld {

    World world = new World(new Vector2(0, -9.82f), true);

    ArrayList<Entity> entityes = new ArrayList<Entity>();

    //ArrayList<ArrayList<Tile>> tile = new ArrayList<ArrayList<Tile>>();

    ArrayList<Tile> tiles = new ArrayList<Tile>();


    private static final float TIME_STEP=1/45;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private float accumulator = 0;


    public ServerWorld(){

        TiledMap map = new TmxMapLoader().load("test2.0.tmx");
        Gdx.app.log("ServerWorld",map.toString());



            //TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(i);
            //Gdx.app.log("ServerWorld",""+ layer.getWidth());

            MapObjects collisionObjects = map.getLayers().get("CollisionLayer").getObjects();
            int tileWidth = 64;
            int tileHeight = 64;

            for (int j = 0; j < collisionObjects.getCount(); j++) {
                RectangleMapObject obj = (RectangleMapObject) collisionObjects.get(j);
                Rectangle rect = obj.getRectangle();

                tiles.add(new Ground(rect.getX(),rect.getY(),rect.width,rect.height,this));


            }




            /*for (int x = 0; x < layer.getWidth(); x++) {
                tile.add(new ArrayList<Tile>());
                for (int y = 0; y < layer.getHeight(); y++) {
                    if (layer.getCell(x,y).getTile().)
                    if (layer.getProperties().containsKey("ground")){

                        Gdx.app.log("Server","är ground");

                        if (layer.getProperties().get("ground",Boolean.class)){
                            tile.get(x).add(new Ground());
                        }
                    }
                    if (layer.getProperties().containsKey("flag")){

                        Gdx.app.log("Server","är flag");

                        if (layer.getProperties().get("flag",Boolean.class)){
                            tile.get(x).add(new Flag());
                        }
                    }
                }*/






    }


    /**
     * one time tick
     *
     * @param deltaTime
     */
    public void doPhysicsStep(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }
    }

    public void addEntity(Entity ny ){
        entityes.add(ny);
    }

    public World getWorld() {
        return world;
    }
}
