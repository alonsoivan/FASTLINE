package com.ivn.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.ivn.game.screens.MainScreen;

public class Aplication extends Game {
	@Override
	public void create () {
		((Game) Gdx.app.getApplicationListener()).setScreen(new MainScreen());
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
