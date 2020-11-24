package com.ivn.game;

import com.badlogic.gdx.Game;
import com.ivn.game.screens.SplashScreen;

public class MainGame extends Game {
	public Boolean isMultiReady;
	@Override
	public void create () {
		super.setScreen(new SplashScreen(this));
		isMultiReady = false;
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
	}
}
