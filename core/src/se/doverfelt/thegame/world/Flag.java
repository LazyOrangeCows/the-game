package se.doverfelt.thegame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import se.doverfelt.thegame.server.ServerWorld;

/**
 * Created by Edvin Bergstrom on 2016-09-30.
 * Beskrivning
 */
public class Flag extends Tile{




    public Flag(float x, float y, float width, float heigth, ServerWorld world) {
        super(x, y, width, heigth, world);
    }
}
