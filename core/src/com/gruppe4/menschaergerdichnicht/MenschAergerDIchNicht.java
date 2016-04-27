package com.gruppe4.menschaergerdichnicht;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TideMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MenschAergerDIchNicht extends Game {

	public interface MyGameCallback {
		 void onSendMessage(String msg);
	}

	private MyGameCallback myGameCallback;
	public void setMyGameCallback(MyGameCallback callback) {
		myGameCallback = callback;
	}



	@Override
	public void create () {
		setScreen(new MainScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public void someMethod() {// check the calling class has actually implemented MyGameCallback
		if (myGameCallback != null) {

			// initiate which ever callback method you need.
			if (true) {
				myGameCallback.onSendMessage("Hallo Spieler");
			}

		} else {
			//Log.e("MyGame", "To use this class you must implement MyGameCallback!")
		}
	}

}
