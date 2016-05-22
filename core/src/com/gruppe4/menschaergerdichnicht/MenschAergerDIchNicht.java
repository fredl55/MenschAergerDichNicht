package com.gruppe4.menschaergerdichnicht;

import com.badlogic.gdx.Game;
import com.gruppe4.menschaergerdichnicht.Interface.IGDXCallBack;
import com.gruppe4.menschaergerdichnicht.Interface.IGameCallBack;

public class MenschAergerDIchNicht extends Game{
	private IGameCallBack myGameCallback;
	public void setMyGameCallback(IGameCallBack callback) {
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
				//myGameCallback.onSendMessage("Hallo Spieler");
			}

		} else {
			//Log.e("MyGame", "To use this class you must implement MyGameCallback!")
		}
	}
}
