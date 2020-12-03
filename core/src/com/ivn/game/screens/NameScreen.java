package com.ivn.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.ivn.game.MainGame;
import com.ivn.game.managers.NetworkManager;
import com.ivn.game.models.MidBall;
import com.ivn.game.models.Ranking;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import javax.swing.*;

import static com.ivn.game.managers.ResourceManager.assets;
import static com.ivn.game.managers.ResourceManager.prefs;

public class NameScreen implements Screen {
    private Stage stage;
    private MainGame game;

    //public static Music musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("musica_fondo.mp3"));

    public static List list1;
    public static List list2;
    public static List list3;

    SpriteBatch batch = new SpriteBatch();
    Texture listBg = new Texture("listBg.png");

    public NameScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {

        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage();

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        stage.addActor(table);

        TextButton.TextButtonStyle textButtonStyle = table.getSkin().get(TextButton.TextButtonStyle.class);
        textButtonStyle.font = assets.get("fonts/OpenSans-Semibold.ttf", BitmapFont.class);

        TextField.TextFieldStyle textFieldStyle = table.getSkin().get(TextField.TextFieldStyle.class);
        textFieldStyle.font = assets.get("fonts/OpenSans-Semibold.ttf", BitmapFont.class);

        Label.LabelStyle labelStyle = table.getSkin().get(Label.LabelStyle.class);
        labelStyle.font = assets.get("fonts/OpenSans-Semibold.ttf", BitmapFont.class);

        int rnd = MathUtils.random(1000,9999);

        final TextField tfNombre = new TextField("GUEST"+rnd,textFieldStyle);
        tfNombre.setAlignment(Align.center);
        stage.addActor(tfNombre);
        stage.setKeyboardFocus(tfNombre);

        TextButton acceptBt = new TextButton("ACEPTAR",textButtonStyle);
        acceptBt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                prefs.putString("name",tfNombre.getText());
                prefs.flush();

                MidBall.myName = tfNombre.getText();
                game.setScreen(new MainScreen(game));
                dispose();
            }
        });

        width = Gdx.graphics.getWidth()*0.5f;
        height = Gdx.graphics.getHeight()*0.1f;

        table.row();
        table.add(new Label("INGRESA TU NOMBRE:",labelStyle));
        table.row();
        table.add(tfNombre).center().width(width).height(height).pad(50);
        table.row();
        table.add(acceptBt).center().width(width).height(height*3).pad(50);


        Gdx.input.setInputProcessor(stage);
    }
    float width;
    float height;

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(listBg,Gdx.graphics.getWidth()/2 - (width*1.5f/2),Gdx.graphics.getHeight()/2 - (height*8.1f/2),width*1.5f,height*8.1f);

        batch.end();

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