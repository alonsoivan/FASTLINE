package com.ivn.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.ivn.game.MainGame;
import com.ivn.game.managers.NetworkManager;
import com.ivn.game.managers.ResourceManager;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class LoadingMultiPlayerScreen implements Screen {
    public static Stage stage;

    VisLabel waitingMessage = new VisLabel("BUSCANDO PARTIDA");
    String dots = "";

    public void setWaitingMessage(){
        Timer.schedule(new Timer.Task() {
            public void run() {
                waitingMessage.setText("BUSCANDO PARTIDA"+dots);

                dots += ".";

                if(dots.equals("...."))
                    dots = "";

            }
        }, 0,0.5f, 300);
    }

    private MainGame game;
    public LoadingMultiPlayerScreen(MainGame game){
        this.game = game;
        // CUANDO ME DE EL OK LANZAR LA PARTIDA
        MultiPlayerScreen.disconected = false;
        MultiPlayerScreen.endGame = false;
        new NetworkManager(game);
    }

    @Override
    public void show() {

        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage();

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        stage.addActor(table);

        setWaitingMessage();

        VisTextButton quitButton = new VisTextButton("CANCELAR");
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainScreen(game));
                dispose();
            }
        });


        // Añade filas a la tabla y añade los componentes
        table.row();
        table.add(waitingMessage).center().width(600).height(200).pad(5);
        table.row();
        table.add(quitButton).center().width(600).height(150).pad(5);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Pinta la UI en la pantalla
        stage.act(dt);
        stage.draw();


        System.out.println(game.isMultiReady);
        if(game.isMultiReady){
            game.setScreen(new MultiPlayerScreen(game));
            dispose();
        }
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
