package com.gruppe4.menschaergerdichnicht;

import com.badlogic.gdx.Game;
import com.gruppe4.menschaergerdichnicht.Interface.IAndroidCallBack;
import com.gruppe4.menschaergerdichnicht.Interface.ILibGDXCallBack;
import com.gruppe4.menschaergerdichnicht.Logic.Draw;
import com.gruppe4.menschaergerdichnicht.Logic.PlaygroundModel;


public class MenschAergerDIchNicht extends Game implements ILibGDXCallBack{
	private IAndroidCallBack myAndroidCallBack;
	public void setMyAndroidCallBack(IAndroidCallBack callback) {
		myAndroidCallBack = callback;
	}
	private MainScreen myScreen;



	@Override
	public void create () {
		MyAssets.loadAllAssets();
		boolean loaded = false;
		while (!loaded){
			if(MyAssets.assets.update()){
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

	@Override
	public void playerAdded(String color) {
		boolean notLoaded = true;
		while (notLoaded){
			if(MyAssets.assets.update() && myScreen!=null){
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
	public void setMyColor(String color) {
		myScreen.setMyColor(color);
	}

	@Override
	public void dispose(){
		MyAssets.assets.dispose();
		myScreen.dispose();
	}

	@Override
	public void movePin(Draw draw) {
		PlaygroundModel.movePin(draw);
	}

	public void playerHasMoved(int pinId, String color,String type,int from, int to, int rollValue){
		myAndroidCallBack.playerMoved(pinId, color, type, from, to, rollValue);
	}


	public void cantRoll(int rollvalue,int rollTrys) {
		myAndroidCallBack.cantRoll(rollvalue, rollTrys);
	}

	public void playerWon() {
		myAndroidCallBack.playerWon();
	}
}
