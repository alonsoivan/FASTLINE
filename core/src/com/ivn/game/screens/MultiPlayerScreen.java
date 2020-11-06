package com.ivn.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.ivn.game.managers.NetworkManager;
import com.ivn.game.models.Ball;
import com.ivn.game.models.MidBall;

import static com.ivn.game.models.Ball.Color.*;
import static com.ivn.game.models.Ball.Color.GREEN;

public class MultiPlayerScreen implements Screen {

    SpriteBatch batch;

    Array<Ball> balls = new Array<>();
    MidBall midBall = new MidBall();

    Texture amarillo;
    Texture azul;
    Texture rojo;
    Texture green;


    // testing
    Array<Sprite> puntos = new Array<>();
    Texture punto = new Texture("punto.png");

    private Game game;
    public MultiPlayerScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        amarillo = new Texture("yellowBall.png");
        azul = new Texture("blueBall.png");
        rojo = new Texture("redBall.png");
        green = new Texture("greenBall.png");

        batch = new SpriteBatch();

        generarBolas();

    }

    public void generarBolas(){
        Timer.schedule(new Timer.Task() {
            public void run() {
                Vector2 pos;
                if(MathUtils.randomBoolean())
                    pos = new Vector2(-128,MathUtils.random(0, Gdx.graphics.getHeight()));
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

                System.out.println(pos+" "+tAle+" "+color);
                balls.add(new Ball(pos,tAle,color));
            }
        }, 1,1.5f, 300);
    }

    @Override
    public void render(float delta) {
        draw();
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

        // INPUT
        Gdx.input.setInputProcessor(new InputAdapter(){
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

        if(flag)
            if(x > Gdx.graphics.getWidth()/2)
                midBall.rotateRight();
            else
                midBall.rotateLeft();

    }

    public void draw(){
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        midBall.draw(batch,true);

        for(Ball bola : balls)
            bola.draw(batch);

        for(Sprite punto : puntos)
            punto.draw(batch);

        batch.end();
    }

    public void collisions(){
        // Colisiones
        for(Ball ball : balls)
            if(Intersector.overlaps(midBall.circle,ball.circle)) {

                if(sameColor(ball)){
                    midBall.myScore += 20;
                    NetworkManager.client.sendTCP(20);
                }
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
    }
}