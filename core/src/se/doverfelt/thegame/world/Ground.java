package se.doverfelt.thegame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import se.doverfelt.thegame.server.ServerWorld;

/**
 * Created by Edvin Bergstrom on 2016-09-30.
 * Beskrivning
 */
public class Ground extends Tile{


    public Ground(float x, float y, float width, float heigth, ServerWorld world) {
        super(x, y, width, heigth,world);

        Gdx.app.log("Ground ","JA");

        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
// Set our body's starting position in the world
        bodyDef.position.set(x, y);

// Create our body in the world using our body definition
        Body body = world.getWorld().createBody(bodyDef);

// Create a circle shape and set its radius to 6
        PolygonShape rek = new PolygonShape();
        rek.setAsBox(width,heigth);
        body.createFixture(rek, 0.0f);
// Clean up after ourselves
        rek.dispose();


    }
}
