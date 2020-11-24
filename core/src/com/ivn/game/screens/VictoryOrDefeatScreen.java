package com.ivn.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ivn.game.MainGame;
import com.ivn.game.managers.NetworkManager;
import com.ivn.game.managers.ResourceManager;
import com.ivn.game.models.MidBall;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import static com.ivn.game.models.MidBall.enemyWinRounds;
import static com.ivn.game.models.MidBall.myWinRounds;

public class VictoryOrDefeatScreen implements Screen {
    private Stage stage;
    private MainGame game;

    public VictoryOrDefeatScreen(MainGame game) {
        this.game = game;
    }

    //public static Music musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("musica_fondo.mp3"));

    @Override
    public void show() {

        // TODO CORTAR CONEXIÓN

        ResourceManager.loadAllResources();

        /*
        if(prefs.getBoolean("sound"))
            if (!musicaFondo.isPlaying())
                playMusicaFondo();
        */

        if (!VisUI.isLoaded())
            VisUI.load(VisUI.SkinScale.X2);

        stage = new Stage();

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        stage.addActor(table);

        VisImage image;
        if(myWinRounds >= 3)
            image= new VisImage(new Texture("win.JPG"));
        else
            image = new VisImage(new Texture("lose.JPG"));

        MidBall.restart();
        SinglePlayerScreen.task.cancel();


        //  TODO poner una u otra depende de quien gane
        if(NetworkManager.client.isConnected())
            NetworkManager.client.stop();

        VisTextButton multiPlayerButton = new VisTextButton("INICIO ");
        multiPlayerButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.isMultiReady = false;
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainScreen(game));
                dispose();
            }
        });

        VisTextButton singlePlayerButton = new VisTextButton("VOLVER A JUGAR");
        singlePlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.isMultiReady = false;
                game.setScreen(new LoadingMultiPlayerScreen(game));
                dispose();
            }
        });


        // Añade filas a la tabla y añade los componentes
        table.row().colspan(2);
        table.add(image).center().width(image.getWidth()).height(image.getHeight()).pad(5).space(100);
        table.row();
        table.add(multiPlayerButton).center().width(600).height(200).pad(5);
        table.add(singlePlayerButton).center().width(600).height(150).pad(5);

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
