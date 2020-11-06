package com.ivn.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ivn.game.MainGame;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class SettingsScreen implements Screen {
    private Stage stage;
    private MainGame game;
    public SettingsScreen(MainGame game) {
        this.game = game;
    }

    //public static Music musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("musica_fondo.mp3"));

    @Override
    public void show() {

        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage();

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        stage.addActor(table);

        VisTextButton playButton = new VisTextButton("OPCIONES");
        playButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainScreen(game));
                dispose();
            }
        });

        VisTextButton configButton = new VisTextButton("OPCIONES");
        configButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainScreen(game));
                dispose();
            }
        });

        VisTextButton quitButton = new VisTextButton("VOLVER");
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainScreen(game));
                dispose();
            }
        });

        VisLabel aboutLabel = new VisLabel("MiJuego libGDX\n(c) IVÁN ALONSO 2020");

        // Añade filas a la tabla y añade los componentes
        table.row();
        table.add(playButton).center().width(600).height(200).pad(5);
        table.row();
        table.add(configButton).center().width(600).height(150).pad(5);
        table.row();
        table.add(quitButton).center().width(600).height(150).pad(5);
        table.row();
        table.add(aboutLabel).left().width(600).height(20).pad(5);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Pinta la UI en la pantalla
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Redimensiona la escena al redimensionar la ventana del juego
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Libera los recursos de la escena
        stage.dispose();
    }
}