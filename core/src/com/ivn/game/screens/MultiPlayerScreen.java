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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.ivn.game.MainGame;
import com.ivn.game.managers.HUD;
import com.ivn.game.managers.NetworkManager;
import com.ivn.game.managers.ResourceManager;
import com.ivn.game.models.Ball;
import com.ivn.game.models.MidBall;
import com.ivn.game.models.Util;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.dialog.ConfirmDialogListener;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.VisTextButton;

import static com.ivn.game.managers.ResourceManager.inkScreen;
import static com.ivn.game.models.Ball.Color.*;
import static com.ivn.game.models.MidBall.enemyWinRounds;
import static com.ivn.game.screens.SinglePlayerScreen.State.PAUSE;
import static com.ivn.game.screens.SinglePlayerScreen.State.PLAY;

public class MultiPlayerScreen implements Screen {

    public static boolean endGame;
    public static boolean disconected;

    SpriteBatch batch;

    public static Array<Ball> balls = new Array<>();
    MidBall midBall = new MidBall();

    // TODO mover esto y hacerlo bien
    Texture amarillo;
    Texture azul;
    Texture rojo;
    Texture green;

    // testing
    Array<Sprite> puntos = new Array<>();
    Texture punto = new Texture("punto.png");

    HUD hud;


    public static SinglePlayerScreen.State state;

    // Menús
    private Stage stage;
    private VisTextButton pauseButton;

    private MainGame game;
    public MultiPlayerScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        state = PAUSE;

        amarillo = new Texture("yellowBall.png");
        azul = new Texture("blueBall.png");
        rojo = new Texture("redBall.png");
        green = new Texture("greenBall.png");

        batch = new SpriteBatch();

        generarBolas();

        hud = new HUD();

        ResourceManager.countDown.start();

        generarMenu();
    }
    public void generarMenu(){
        if (!VisUI.isLoaded())
            VisUI.load(VisUI.SkinScale.X2);

        float pauseWidth = Gdx.graphics.getWidth() * 0.1f;
        float pauseHeight = Gdx.graphics.getHeight() * 0.1f;

        stage = new Stage();

        pauseButton = new VisTextButton("PAUSA");
        pauseButton.setPosition(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() * 0.025f - pauseWidth, Gdx.graphics.getHeight() * 0.045f );
        pauseButton.setSize(pauseWidth,pauseHeight);
        pauseButton.getColor().a = 0.8f;
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                final int salir = 1;
                final int cancelar = 2;

                //confirmdialog may return result of any type, here we are just using ints
                Dialogs.showConfirmDialog(stage, "CUIDAO", "SEGURO QUIERES SALIR?",
                        new String[]{"salir", "cancelar"}, new Integer[]{salir, cancelar},
                        new ConfirmDialogListener<Integer>() {
                            @Override
                            public void result (Integer result) {
                                if (result == salir){
                                    midBall.restart();
                                    game.setScreen(new VictoryOrDefeatScreen(game));
                                }
                                //if (result == cancelar)

                            }
                        });
            }
        });
        stage.addActor(pauseButton);
    }

    public void generarBolas(){
        System.out.println("genero bolas");
        SinglePlayerScreen.task = new Timer.Task() {
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
                if(state == PLAY){
                    balls.add(new Ball(pos, tAle, color,MathUtils.randomBoolean()));
                }
            }
        };


        System.out.println("genero bolas");
        Timer.schedule( SinglePlayerScreen.task, 1,1.5f, 300);
    }

    @Override
    public void render(float delta) {
        draw(delta);

        if(state == PLAY)
            update();

        //
        if(ResourceManager.countDown.isFinished && !ResourceManager.timer.isStarted){
            ResourceManager.timer.start();
            state = PLAY;
        }
        System.out.println("ARRAY LENGTH:::" + balls.size + " " + state);
    }

    public void update(){
        for(Ball bola : balls)
            bola.mover();

        userInput();

        collisions();

        if(endGame) {
            game.setScreen(new VictoryOrDefeatScreen(game));
        }else if(disconected){
            disconected = false;
            state = PAUSE;

            Dialogs.showConfirmDialog(stage, "ERROR", "Parece que hubo algún problema con la conexión",
                    new String[]{"ACEPTAR"}, new Integer[]{1},
                    new ConfirmDialogListener<Integer>() {
                        @Override
                        public void result (Integer result) {
                            if (result == 1){
                                midBall.restart();
                                game.setScreen(new VictoryOrDefeatScreen(game));
                            }
                        }
                    });
        }


        if(ResourceManager.timer.isFinished){
            if(midBall.myScore > midBall.enemyScore)
                MidBall.myWinRounds++;
            else
                enemyWinRounds++;


            if(MidBall.myWinRounds >= 3 || enemyWinRounds >= 3)
                endGame = true;
            else {
                MidBall.nextRound();
                HUD.setRounds(MidBall.myWinRounds, enemyWinRounds);
            }
        }

    }


    boolean flag = false;
    int x = 0;
    public void userInput(){

        // INPUT
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

        midBall.draw(batch);

        hud.draw(batch, true);

        for (Ball bola : balls)
            bola.draw(batch);

        for (Sprite punto : puntos)
            punto.draw(batch);


        // POWERUPS
        if(ResourceManager.timer.isStarted){
            inkScreen.setPosition(0,0);
            inkScreen.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
            inkScreen.setColor(inkScreen.getColor().r,inkScreen.getColor().g,inkScreen.getColor().b,opacity);
            inkScreen.draw(batch);
            if(opacity > 0)
                opacity-= 0.005f;
        }
        batch.end();

        // Pinta la UI en la pantalla
        stage.act(dt);
        stage.draw();

    }
    public static float opacity = 0;

    public void collisions(){
        // Colisiones

        for(Ball ball : balls)
            if(Intersector.overlaps(midBall.circle,ball.circle)) {

                if(sameColor(ball)){
                    midBall.myScore += 5;
                    NetworkManager.client.sendTCP(midBall.myScore);

                    if(ball.hasPowerUp)
                        NetworkManager.client.sendTCP(new Util(ball.pu));
                }
                else{
                    System.out.println("wrong color");
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

        //if(puntos.size > 2)
        //   puntos.removeIndex(0);




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