package com.gruppe4.menschaergerdichnicht;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.gruppe4.menschaergerdichnicht.Interface.IAndroidCallBack;
import com.gruppe4.menschaergerdichnicht.Interface.ILibGDXCallBack;

public class MenschAergerDIchNicht extends Game implements ILibGDXCallBack{
	private IAndroidCallBack myGameCallback;
	public void setMyGameCallback(IAndroidCallBack callback) {
		myGameCallback = callback;
	}
	private MainScreen myScreen;



	@Override
	public void create () {
		MyAsstes.loadAllAssets();
		boolean loaded = false;
		while (!loaded){

			if(MyAsstes.assets.update()){
				myScreen = new MainScreen(this);
				setScreen(myScreen);
				loaded = true;
			}
		}


	}

	@Override
	public void render () {
		super.render();
	}

	public void someMethod() {// check the calling class has actually implemented MyGameCallback
		if (myGameCallback != null) {
			// initiate which ever callback method you need.
			if (true) {
				//myGameCallback.onSendMessage("Hallo Spieler");

			}

		} else {
			//Log.e("MyGame", "To use this class you must implement MyGameCallback!")
		}
	}

	@Override
	public void playerAdded(String color) {
		boolean notLoaded = true;
		while (notLoaded){
			if(MyAsstes.assets.update() && myScreen!=null){
				myScreen.playerAdded(color);
				notLoaded = false;
			}
		}
	}

	@Override
	public void playerHasRoled(int number) {
		myScreen.playerHasRoled(number);
	}

	@Override
	public void dispose(){
		MyAsstes.assets.dispose();
		myScreen.dispose();
	}


}
