package se.doverfelt.thegame.world;

import com.badlogic.gdx.physics.box2d.Body;
import se.doverfelt.thegame.server.ServerWorld;

/**
 * Created by Edvin Bergstrom on 2016-09-30.
 * Beskrivning
 */
public class Tile {

    private Body body;

    private String texture;

    public Tile(float x, float y, float width, float heigth, ServerWorld world){


    }


    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }
}
