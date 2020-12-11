package com.ivn.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.ivn.game.MainGame;
import com.ivn.game.managers.NetworkManager;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import static com.ivn.game.managers.ResourceManager.assets;
import static com.ivn.game.managers.ResourceManager.midBall3;

public class LoadingMultiPlayerScreen implements Screen {
    public static Stage stage;

    Label waitingMessage;
    VisImage loadingImage1 = new VisImage(midBall3);
    VisImage loadingImage2 = new VisImage(midBall3);
    VisImage loadingImage3 = new VisImage(midBall3);

    String dots = "";

    public void setWaitingMessage(){
        Timer.schedule(new Timer.Task() {
            public void run() {
                waitingMessage.setText("SEARCHING FOR OPPONENT"+dots);

                dots += ".";

                if(dots.equals("...."))
                    dots = "";

            }
        }, 0,0.5f, 300);
    }

    public void connect(){
        Timer.schedule(new Timer.Task() {
            public void run() {
                new NetworkManager(game);
            }
        }, 0);
    }

    private MainGame game;
    public LoadingMultiPlayerScreen(MainGame game){
        this.game = game;

        MultiPlayerScreen.disconected = false;
        MultiPlayerScreen.endGame = false;

        connect();
    }

    @Override
    public void show() {

        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage();

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        stage.addActor(table);

        Skin skin = new Skin(Gdx.files.internal("skins/star-soldier/skin/star-soldier-ui.json"));
        TextButton.TextButtonStyle textButtonStyle = skin.get(TextButton.TextButtonStyle.class);
        //TextButton.TextButtonStyle textButtonStyle = table.getSkin().get(TextButton.TextButtonStyle.class);
        textButtonStyle.font = assets.get("fonts/OpenSans-Semibold.ttf", BitmapFont.class);

        Label.LabelStyle labelStyle = table.getSkin().get(Label.LabelStyle.class);
        labelStyle.font = assets.get("fonts/OpenSans-Semibold.ttf", BitmapFont.class);

        float width = Gdx.graphics.getHeight()*0.3f;
        float pad = Gdx.graphics.getWidth()*0.015f;

        loadingImage1.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.rotateBy(10, 0.1f)));
        loadingImage1.setOrigin(width/2, width/2);
        loadingImage2.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.rotateBy(10, 0.1f)));
        loadingImage2.setOrigin(width/2, width/2);
        loadingImage3.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.rotateBy(10, 0.1f)));
        loadingImage3.setOrigin(width/2, width/2);

        waitingMessage = new Label("SEARCHING FOR OPPONENT",labelStyle);
        waitingMessage.setAlignment(Align.center);

        setWaitingMessage();


        TextButton quitButton = new TextButton("CANCEL",textButtonStyle);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                NetworkManager.client.stop();
                game.setScreen(new MainScreen(game));
                dispose();
            }
        });


        // Añade filas a la tabla y añade los componentes
        table.row();
        table.add(loadingImage1).center().width(width).height(width).pad(pad);
        table.add(loadingImage2).center().width(width).height(width).pad(pad);
        table.add(loadingImage3).center().width(width).height(width).pad(pad);
        table.row().colspan(3);
        table.add(waitingMessage).center().width(width*3).height(width/2).pad(pad);
        table.row().colspan(3);
        table.add(quitButton).center().width(width*2).height(width*0.8f).pad(pad);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Pinta la UI en la pantalla
        stage.act(dt);
        stage.draw();

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
