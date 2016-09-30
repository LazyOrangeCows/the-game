package se.doverfelt.thegame.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Robin on 2016-09-30.
 */
public interface CScreen extends Screen {
     void render(float delta, SpriteBatch batch);
}
