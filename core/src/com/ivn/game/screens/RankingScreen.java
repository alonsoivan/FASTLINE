package com.ivn.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.ivn.game.MainGame;
import com.ivn.game.managers.NetworkManager;
import com.ivn.game.models.Ranking;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import static com.ivn.game.managers.ResourceManager.assets;

public class RankingScreen implements Screen {
    private Stage stage;
    private MainGame game;

    //public static Music musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("musica_fondo.mp3"));

    public static List list1;
    public static List list2;
    public static List list3;

    SpriteBatch batch = new SpriteBatch();
    Texture listBg = new Texture("listBg.png");

    public RankingScreen(MainGame game) {
        this.game = game;
    }

    private int score;
    public RankingScreen(MainGame game, int score) {
        this.game = game;
        this.score = score;
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
        //List.ListStyle listStyle = skin.get("default", List.ListStyle.class);
        List.ListStyle listStyle = table.getSkin().get(List.ListStyle.class);
        listStyle.font = assets.get("fonts/OpenSans-Semibold.ttf", BitmapFont.class);

        TextButton.TextButtonStyle textButtonStyle =  skin.get("default", TextButton.TextButtonStyle.class);
        //TextButton.TextButtonStyle textButtonStyle = table.getSkin().get(TextButton.TextButtonStyle.class);
        textButtonStyle.font = assets.get("fonts/OpenSans-Semibold.ttf", BitmapFont.class);

        Label.LabelStyle labelStyle = table.getSkin().get(Label.LabelStyle.class);
        labelStyle.font = assets.get("fonts/OpenSans-Semibold.ttf", BitmapFont.class);

        list1 = new List(listStyle);
        list2 = new List(listStyle);
        list3 = new List(listStyle);

        connect();

        TextButton quitButton = new TextButton("BACK",textButtonStyle);
        quitButton.getColor().a = 0.8f;
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainScreen(game));
                dispose();
            }
        });

        width = Gdx.graphics.getWidth()*0.25f;
        height = Gdx.graphics.getHeight()*0.60f;

        table.row().colspan(3);
        table.add(new Label("",table.getSkin()));
        table.add(new Label("RANKING",table.getSkin())).pad(20);
        table.row().colspan(3);
        table.add(list1).center().width(width).height(height);
        table.add(list2).center().width(width).height(height);
        table.add(list3).center().width(width).height(height);
        table.row().colspan(3);
        if(score > 0){
            table.add(new Label("ACTUAL SCORE: ",table.getSkin()));
            table.add(new Label(""+score,table.getSkin()));
        }else {
            table.add(new Label("",table.getSkin()));
            table.add(new Label("",table.getSkin()));
        }
        table.add(quitButton).center().width(width).height(height/3);


        Gdx.input.setInputProcessor(stage);
    }
    float width;
    float height;

    public static void updateList(Ranking ranking){
        Array<String > names = new Array<>();
        Array<String> scores = new Array<>();
        Array<String> dates = new Array<>();

        names.add(" NAME:");
        scores.add(" SCORE:");
        dates.add(" DATE:");
        for(int i = 0; i < ranking.names.length ; i++)
            if(ranking.names[i] != null){
                names.add(" "+ranking.names[i]);
                scores.add(" "+ranking.scores[i]);
                dates.add(" "+ranking.dates[i]);
            }

        list1.setItems(names);
        list2.setItems(scores);
        list3.setItems(dates);
    }

    public void connect(){
        Timer.schedule(new Timer.Task() {
            public void run() {
                new NetworkManager();
            }
        }, 0);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(listBg,Gdx.graphics.getWidth()/2 - (width*3.1f/2),Gdx.graphics.getHeight()/2 - (height*2.1f/2),width*3.1f,height*2.1f);

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