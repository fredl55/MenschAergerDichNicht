package com.gruppe4.menschaergerdichnicht;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by manfrededer on 24.05.16.
 */
public class MyAsstes {
    public static AssetManager assets = new AssetManager();

    public static void loadAllAssets(){
        assets.load("1c.gif", Texture.class);
        assets.load("2c.gif", Texture.class);
        assets.load("3c.gif", Texture.class);
        assets.load("4c.gif", Texture.class);
        assets.load("5c.gif", Texture.class);
        assets.load("6c.gif", Texture.class);
        assets.load("greenPin.jpeg", Texture.class);
        assets.load("redPin.jpeg", Texture.class);
        assets.load("yellowPin.jpeg", Texture.class);
        assets.load("bluePin.jpeg", Texture.class);
    }
}
