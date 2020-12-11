package com.ivn.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.ivn.game.MainGame;
import com.ivn.game.managers.HUD;
import com.ivn.game.managers.NetworkManager;
import com.ivn.game.models.Ball;
import com.ivn.game.models.MidBall;
import com.ivn.game.models.Ranking;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisWindow;

import java.util.Date;

import static com.ivn.game.managers.ResourceManager.*;
import static com.ivn.game.models.Ball.Color.*;
import static com.ivn.game.models.MidBall.*;
import static com.ivn.game.screens.SinglePlayerScreen.State.PAUSE;
import static com.ivn.game.screens.SinglePlayerScreen.State.PLAY;

public class SinglePlayerScreen implements Screen {

    public enum State {
        PLAY,PAUSE
    }

    private Array<Sprite> arrayBackground1 = new Array<>();
    private Array<Sprite> arrayBackground2 = new Array<>();


    public boolean lose;

    State state;

    SpriteBatch batch;

    public static Array<Ball> balls;
    MidBall midBall;

    Texture amarillo;
    Texture azul;
    Texture rojo;
    Texture green;


    // testing
    Array<Sprite> puntos = new Array<>();
    Texture punto = new Texture("punto.png");


    HUD hud;

    // Menús
    private Stage stage;
    private ImageButton pauseButton;
    private VisWindow table;

    private MainGame game;
    public SinglePlayerScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        state = PLAY;

        midBall = new MidBall();
        balls = new Array<>();

        amarillo = new Texture("balls/yellowBall.png");
        azul = new Texture("balls/blueBall.png");
        rojo = new Texture("balls/redBall.png");
        green = new Texture("balls/greenBall.png");

        batch = new SpriteBatch();

        generarBolas();

        generarMenu();

        hud = new HUD();
    }

    public void generarMenu(){
        // menús
        if (!VisUI.isLoaded())
            VisUI.load(VisUI.SkinScale.X2);

        stage = new Stage();


        Skin skin = new Skin(Gdx.files.internal("skins/star-soldier/skin/star-soldier-ui.json"));
        TextButton.TextButtonStyle textButtonStyle = skin.get(TextButton.TextButtonStyle.class);
        //TextButton.TextButtonStyle textButtonStyle = table.getSkin().get(TextButton.TextButtonStyle.class);
        textButtonStyle.font = assets.get("fonts/OpenSans-Semibold.ttf", BitmapFont.class);

        table = new VisWindow("");
        table.setSize(Gdx.graphics.getWidth()* 0.60f, Gdx.graphics.getHeight()* 0.6f);
        //table.setCenterOnAdd(true);
        table.setResizable(true);
        table.setMovable(false);
        table.setVisible(false);
        table.getColor().a = 0.85f;
        stage.addActor(table);
        table.center();
        table.setPosition(Gdx.graphics.getWidth()/2 - table.getWidth()/2 , Gdx.graphics.getHeight()/2 - table.getHeight()/2);


        float pauseWidth = Gdx.graphics.getHeight() * 0.15f;

        pauseButton = new ImageButton(new Skin(Gdx.files.internal("skins/star-soldier/skin/star-soldier-ui.json")));
        pauseButton.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("HUD/pausa2.png")));
        pauseButton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("HUD/pausa.png")));
        pauseButton.setPosition(Gdx.graphics.getWidth() - Gdx.graphics.getHeight() * 0.1f - pauseWidth/2, Gdx.graphics.getHeight() * 0.1f - pauseWidth/2 );
        pauseButton.setSize(pauseWidth,pauseWidth);
        pauseButton.getColor().a = 0.8f;
        pauseButton.setBackground((Drawable)null);
        pauseButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.setVisible(true);
                pauseButton.setVisible(false);

                state = PAUSE;
                task.cancel();
            }
        });
        stage.addActor(pauseButton);

        TextButton multiPlayerButton = new TextButton("RESUME",textButtonStyle);
        multiPlayerButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.setVisible(false);
                pauseButton.setVisible(true);

                state = PLAY;
                task.run();
            }
        });

        TextButton singlePlayerButton = new TextButton("RESTART",textButtonStyle);
        singlePlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SinglePlayerScreen(game));
                dispose();
                task.run();
                midBall.restart();
            }
        });

        TextButton configButton = new TextButton("RANKING",textButtonStyle);
        configButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new RankingScreen(game));
                dispose();
                task.cancel();
                midBall.restart();
            }
        });

        TextButton quitButton = new TextButton("QUIT",textButtonStyle);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                task.cancel();
                midBall.restart();
                game.setScreen(new MainScreen(game));
                dispose();
            }
        });


        // Añade filas a la tabla y añade los componentes

        float width = table.getWidth() * 0.45f;
        float height = table.getHeight() * 0.3f;
        float pad = table.getWidth() * 0.003f;
        float space = table.getWidth() * 0.05f;

        table.row();
        table.add(multiPlayerButton).center().width(width).height(height).pad(pad).space(space);
        table.add(singlePlayerButton).center().width(width).height(height).pad(pad).space(space);
        table.row();
        table.add(configButton).center().width(width).height(height).pad(pad).space(space);
        table.add(quitButton).center().width(width).height(height).pad(pad).space(space);
    }

    static Timer.Task task;
    public void generarBolas(){
            task = new Timer.Task() {
                public void run() {
                    Vector2 pos;
                    if (MathUtils.randomBoolean())
                        pos = new Vector2(-128, MathUtils.random(0, Gdx.graphics.getHeight()));
                    else
                        pos = new Vector2(Gdx.graphics.getWidth(), MathUtils.random(0, Gdx.graphics.getHeight()));


                    /////
                    //pos = new Vector2(0,Gdx.graphics.getHeight()/2);

                    //midball.level
                    int rango = 2;
                    if (midBall.level == 2) {
                        rango = 3; // 4 colores
                        midBall.changeColors(4);
                    } else if (midBall.level == 3) {
                        rango = 4; // 5 colores
                        midBall.changeColors(5);
                    }

                    int num = MathUtils.random(rango);
                    Ball.Color color = BLUE;
                    Texture tAle = azul;

                    if (num == 0) {
                        tAle = amarillo;
                        color = YELLOW;
                    }
                    if (num == 1) {
                        tAle = azul;
                        color = BLUE;
                    }
                    if (num == 2) {
                        tAle = rojo;
                        color = RED;
                    }
                    if (num == 3) {
                        tAle = green;
                        color = GREEN;
                    }

                    System.out.println("saca bola");
                    System.out.println(pos + " " + tAle + " " + color);

                    if(state == PLAY) {
                        balls.add(new Ball(pos, tAle, color));
                    }
                }
            };

        System.out.println("genero bolas");
        Timer.schedule( task, 1,1.5f, 300);
    }

    @Override
    public void render(float delta) {
        draw(delta);

        if(state == PLAY)
            update();
    }


    public void background(){
        int width = Gdx.graphics.getWidth();

        if(arrayBackground1.size < 3){
            Sprite sprite = new Sprite(back1);
            sprite.setSize(width,width);
            sprite.setRegion(new TextureRegion(back1,0,0,back1.getWidth() ,back1.getHeight()));
            sprite.setBounds(0,0,width,width);

            if(arrayBackground1.size > 0)
                sprite.setPosition(0, arrayBackground1.get(arrayBackground1.size-1).getY()+width);
            else
                sprite.setPosition(0,0);

            arrayBackground1.add(sprite);
        }

        if(arrayBackground2.size < 3){
            Sprite sprite = new Sprite(back2);
            sprite.setSize(width,width);
            sprite.setRegion(new TextureRegion(back2,0,0,back2.getWidth() ,back2.getHeight()));
            sprite.setBounds(0,0,width,width);

            if(arrayBackground2.size > 0)
                sprite.setPosition(0, arrayBackground2.get(arrayBackground2.size-1).getY()+width);
            else
                sprite.setPosition(0,0);

            arrayBackground2.add(sprite);
        }
    }

    public void update(){

        for(Sprite back : arrayBackground1)
            if((back.getY()+back.getHeight()) < 0)
                arrayBackground1.removeIndex(0);
            else
                back.setPosition(back.getX(),back.getY()-5);

        for(Sprite back : arrayBackground2)
            if((back.getY()+back.getHeight()) < 0)
                arrayBackground2.removeIndex(0);
            else
                back.setPosition(back.getX(),back.getY()-3);


        for(Ball bola : balls) {
            bola.mover(false);
        }

        background();

        userInput();

        collisions();

        // lose perder
        if(lose){
            int score = overallScore;
            sendScore(score);
            task.cancel();
            midBall.restart();
            game.setScreen(new RankingScreen(game,score));
            dispose();
        }
    }
    public void sendScore(final int score){
        Timer.schedule(new Timer.Task() {
            public void run() {
                Ranking ranking = new Ranking();
                ranking.names[0] = myName;
                ranking.scores[0] = String.valueOf(score);
                ranking.dates[0] = String.valueOf(new Date().getTime());

                new NetworkManager(ranking);
            }
        }, 0);
    }


    boolean flag = false;
    int x = 0;
    public void userInput(){

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new InputAdapter(){
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                flag = false;

                if(Gdx.input.isTouched())
                    flag = true;
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                flag = true;
                x = screenX;

                return true;
            }

        });
        Gdx.input.setInputProcessor(inputMultiplexer);

        if(flag)
            if(x > Gdx.graphics.getWidth()/2)
                midBall.rotateRight();
            else
                midBall.rotateLeft();

    }

    public void draw(float dt){
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.begin();

        for(Sprite back : arrayBackground1)
            back.draw(batch);
        for(Sprite back : arrayBackground2)
            back.draw(batch);

        batch.draw(bg,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


        midBall.draw(batch);

        hud.draw(batch, false);

        for(Ball bola : balls) {
            bola.draw(batch);
        }

        for(Sprite punto : puntos)
            punto.draw(batch);


        batch.end();


        // Pinta la UI en la pantalla
        stage.act(dt);
        stage.draw();

    }

    public void collisions(){
        // Colisiones
        for(Ball ball : balls)
            if(Intersector.overlaps(midBall.circle,ball.circle)) {

                if(sameColor(ball)){
                    if(prefs.getBoolean("sounds"))
                        sound.play(0.2f);

                    int pts = 5 * midBall.racha;
                    myScore += pts;
                    midBall.overallScore += pts;

                    racha ++;
                }
                else{
                    racha = 1;
                    myScore -= 10;
                    midBall.overallScore -=10;

                    if(midBall.overallScore <= 0)
                        midBall.overallScore = 0;
                    if(myScore <= 0)
                        myScore = 0;

                    if(myScore <= 0 && MidBall.level > 1)
                        lose = true;
                    //Gdx.input.vibrate(100);
                }

                balls.removeValue(ball,false);
            }
    }

    public boolean sameColor(Ball ball){
        // color de pixel de la pantalla


        //testing
        Vector2 pos = ball.position;
        Vector2 mid = new Vector2(Gdx.graphics.getWidth()/2 , Gdx.graphics.getHeight()/2);
        Vector2 dir = mid.sub(ball.position);
        dir.nor();
        dir.scl(Gdx.graphics.getWidth()*0.07f);
        pos.add(dir);

        Sprite sprite = new Sprite(punto);
        sprite.setPosition(pos.x - punto.getWidth()/2,pos.y - punto.getWidth()/2);
        //puntos.add(sprite);

        if(puntos.size > 2)
            puntos.removeIndex(0);




        int pixel = ScreenUtils.getFrameBufferPixmap( MathUtils.round(pos.x ),MathUtils.round(pos.y ),1,1).getPixel(0,0);
        //int pixel = 0;
        Color midBallColor = new Color(pixel);

        System.out.println(midBallColor.r + " " + midBallColor.b + " " + midBallColor.g);
        int r = midBallColor.r > 0 ? 1 : 0;
        int g = midBallColor.g > 0 ? 1 : 0;
        int b = midBallColor.b > 0 ? 1 : 0;
        String col = null;

        System.out.println(r + " " + b + " " + g);

        if(r == 1 && g == 0 && b == 0) {
            col = "rojo";
            if (ball.color == RED)
                return true;
        }
        else if(r == 0 && g == 1 && b == 1) {
            col = "azul";
            if (ball.color == BLUE)
                return true;
        }
        else if(r == 1 && g == 1 && b == 0) {
            col = "amarillo";
            if (ball.color == YELLOW)
                return true;
        }
        else if(r == 0 && g == 1 && b == 0) {
            col = "verde";
            if (ball.color == GREEN)
                return true;
        }

        System.out.println(col);
        return false;
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
        stage.dispose();
    }
}
