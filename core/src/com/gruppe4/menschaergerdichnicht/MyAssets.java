package com.gruppe4.menschaergerdichnicht;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by manfrededer on 24.05.16.
 */
public class MyAssets {
    public static AssetManager assets = new AssetManager();

    public static void loadAllAssets(){
        assets.load("1c.gif", Texture.class);
        assets.load("2c.gif", Texture.class);
        assets.load("3c.gif", Texture.class);
        assets.load("4c.gif", Texture.class);
        assets.load("5c.gif", Texture.class);
        assets.load("6c.gif", Texture.class);
        assets.load("greenPin.png", Texture.class);
        assets.load("redPin.png", Texture.class);
        assets.load("yellowPin.png", Texture.class);
        assets.load("bluePin.png", Texture.class);
    }
}
