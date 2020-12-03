package com.ivn.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ivn.game.MainGame;
import com.ivn.game.managers.NetworkManager;
import com.ivn.game.managers.ResourceManager;
import com.ivn.game.models.MidBall;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;

import static com.ivn.game.managers.ResourceManager.assets;
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

        TextButton.TextButtonStyle textButtonStyle = table.getSkin().get(TextButton.TextButtonStyle.class);
        textButtonStyle.font = assets.get("fonts/OpenSans-Semibold.ttf", BitmapFont.class);

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

        TextButton multiPlayerButton = new TextButton("INICIO",textButtonStyle);
        multiPlayerButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.isMultiReady = false;
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainScreen(game));
                dispose();
            }
        });

        TextButton singlePlayerButton = new TextButton("VOLVER A JUGAR",textButtonStyle);
        singlePlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.isMultiReady = false;
                game.setScreen(new LoadingMultiPlayerScreen(game));
                dispose();
            }
        });

        float width = Gdx.graphics.getWidth()*0.35f;
        float height = Gdx.graphics.getHeight()*0.25f;

        // Añade filas a la tabla y añade los componentes
        table.row().colspan(2);
        table.add(image).center().width(image.getWidth()).height(image.getHeight()).pad(5).space(100);
        table.row();
        table.add(multiPlayerButton).center().width(width).height(height).pad(5);
        table.add(singlePlayerButton).center().width(width).height(height).pad(5);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
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
