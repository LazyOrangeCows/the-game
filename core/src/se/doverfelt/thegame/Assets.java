package se.doverfelt.thegame;

import com.badlogic.gdx.assets.AssetManager;

/**
 * Created by robin.boregrim on 2016-09-30.
 */
public class Assets {
    AssetManager manager;
    public Assets(){
        manager = new AssetManager();

        


    }
    public AssetManager getAssetManager(){
        return manager;
    }

    public void update(){
        manager.update();
    }
}
