package com.ivn.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.ivn.game.MainGame;
import com.ivn.game.models.Ball;
import com.ivn.game.models.MidBall;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

import java.util.TimerTask;

import static com.ivn.game.models.Ball.Color.*;
import static com.ivn.game.screens.SinglePlayerScreen.State.PAUSE;
import static com.ivn.game.screens.SinglePlayerScreen.State.PLAY;

public class SinglePlayerScreen implements Screen {

    public enum State {
        PLAY,PAUSE
    }

    State state;

    SpriteBatch batch;

    Array<Ball> balls;
    MidBall midBall;

    Texture amarillo;
    Texture azul;
    Texture rojo;
    Texture green;


    // testing
    Array<Sprite> puntos = new Array<>();
    Texture punto = new Texture("punto.png");


    // Menús
    private Stage stage;
    private VisTextButton pauseButton;
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

        amarillo = new Texture("yellowBall.png");
        azul = new Texture("blueBall.png");
        rojo = new Texture("redBall.png");
        green = new Texture("greenBall.png");

        batch = new SpriteBatch();

        generarBolas();

        generarMenu();

    }

    public void generarMenu(){
        // menús
        if (!VisUI.isLoaded())
            VisUI.load(VisUI.SkinScale.X2);


        float pauseWidth = Gdx.graphics.getWidth() * 0.1f;
        float pauseHeight = Gdx.graphics.getHeight() * 0.1f;

        stage = new Stage();
        pauseButton = new VisTextButton("PAUSA");
        pauseButton.setPosition(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() * 0.025f - pauseWidth, Gdx.graphics.getHeight() * 0.045f );
        pauseButton.setSize(pauseWidth,pauseHeight);
        pauseButton.getColor().a = 0.8f;
        pauseButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
               table.setVisible(true);
               pauseButton.setVisible(false);


               state = PAUSE;
                Timer.instance().stop();
            }
        });
        stage.addActor(pauseButton);

        table = new VisWindow("");
        table.setSize(Gdx.graphics.getWidth()* 0.45f, Gdx.graphics.getHeight()* 0.6f);
        table.setCenterOnAdd(true);
        table.setResizable(true);
        table.setMovable(false);
        table.setVisible(false);
        table.getColor().a = 0.95f;
        stage.addActor(table);


        VisTextButton multiPlayerButton = new VisTextButton("RESUME");
        multiPlayerButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.setVisible(false);
                pauseButton.setVisible(true);


                state = PLAY;
                Timer.instance().start();
            }
        });

        VisTextButton singlePlayerButton = new VisTextButton("RESTART");
        singlePlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SinglePlayerScreen(game));
                dispose();

                Timer.instance().start();
                /// TODO
                /// restart puntuacion, nivel, speed..
            }
        });

        VisTextButton configButton = new VisTextButton("SETTINGS");
        configButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
                dispose();
            }
        });

        VisTextButton quitButton = new VisTextButton("QUIT");
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Timer.instance().clear();
                game.setScreen(new MainScreen(game));
                dispose();
            }
        });


        // Añade filas a la tabla y añade los componentes

        float width = table.getWidth() * 0.35f;
        float height = table.getHeight() * 0.3f;
        float pad = table.getWidth() * 0.005f;
        float space = table.getWidth() * 0.08f;

        table.row();
        table.add(multiPlayerButton).center().width(width).height(height).pad(pad).space(space);
        table.add(singlePlayerButton).center().width(width).height(height).pad(pad).space(space);
        table.row();
        table.add(configButton).center().width(width).height(height).pad(pad).space(space);
        table.add(quitButton).center().width(width).height(height).pad(pad).space(space);


    }

    Timer.Task task;
    public void generarBolas(){
        task = new Timer.Task(){
            public void run() {
                Vector2 pos;
                if(MathUtils.randomBoolean())
                    pos = new Vector2(-128,MathUtils.random(0,Gdx.graphics.getHeight()));
                else
                    pos = new Vector2(Gdx.graphics.getWidth(),MathUtils.random(0,Gdx.graphics.getHeight()));


                /////
                //pos = new Vector2(0,Gdx.graphics.getHeight()/2);


                //midball.level
                int rango = 2;
                if(midBall.level == 2) {
                    rango = 3; // 4 colores
                    midBall.changeColors(4);
                }
                else if (midBall.level == 3) {
                    rango = 4; // 5 colores
                    midBall.changeColors(5);
                }

                int num = MathUtils.random(rango);
                Ball.Color color = BLUE;
                Texture tAle = azul;

                if(num == 0) {
                    tAle = amarillo;
                    color = YELLOW;
                }
                if(num == 1) {
                    tAle = azul;
                    color = BLUE;
                }
                if(num == 2) {
                    tAle = rojo;
                    color = RED;
                }
                if(num == 3){
                    tAle = green;
                    color = GREEN;
                }

                //System.out.println(pos+" "+tAle+" "+color);
                balls.add(new Ball(pos,tAle,color));
            }
        };

        Timer.schedule( task, 1,1.5f, 300);
    }

    @Override
    public void render(float delta) {
        draw(delta);

        if(state == PLAY)
            update();
    }

    public void update(){
        for(Ball bola : balls)
            bola.mover();

        userInput();

        collisions();
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

        midBall.draw(batch,false);

        for(Ball bola : balls)
            bola.draw(batch);

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

                if(sameColor(ball))
                    midBall.myScore += 20;
                else{
                    System.out.println("perdiste wey");
                    Gdx.input.vibrate(100);
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
