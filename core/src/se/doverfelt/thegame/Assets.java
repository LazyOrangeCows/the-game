package se.doverfelt.thegame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by robin.boregrim on 2016-09-30.
 */
public class Assets {
    AssetManager manager;
    public Assets(){
        //Create a new AssetManager
        manager = new AssetManager();

        //Textures
        manager.load("Harambe.jpg", Texture.class);

        update();


    }
    public AssetManager getAssetManager(){
        return manager;
    }

    public void update(){
        manager.update();
    }
    public void dispose(){
        manager.dispose();
    }
}
