package com.ivn.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.ivn.game.models.Ball;
import com.ivn.game.models.MidBall;

import static com.ivn.game.models.Ball.COLOR.*;

public class SingleGameScreen implements Screen {

    SpriteBatch batch;

    Array<Ball> balls = new Array<>();
    MidBall midBall = new MidBall(new Texture("midBall4.png"));

    Texture amarillo;
    Texture azul;
    Texture rojo;
    Texture green;

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
                    pos = new Vector2(-128,MathUtils.random(0,Gdx.graphics.getHeight()));
                else
                    pos = new Vector2(Gdx.graphics.getWidth(),MathUtils.random(0,Gdx.graphics.getHeight()));

                Texture tAle = null;

                int num = MathUtils.random(midBall.level);
                Ball.COLOR color = null;

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

                balls.add(new Ball(pos,tAle,color));
            }
        }, 1,1.5f, 300);
    }

    @Override
    public void render(float delta) {
        update();
        draw();
    }

    public void update(){
        for(Ball bola : balls)
            bola.mover();

        userInput();

        collisions();

        System.out.println("PUNTUACION::::::::: "+ midBall.score);
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        midBall.draw(batch);
        for(Ball bola : balls)
            bola.draw(batch);

        batch.end();
    }

    public void collisions(){
        // Colisiones
        for(Ball ball : balls)
            if(Intersector.overlaps(midBall.circle,ball.circle)) {
                if(sameColor(ball.position,ball.direction, ball.color))
                    midBall.score += 5;
                else
                    System.exit(0);

                balls.removeValue(ball,false);
            }
    }

    public boolean sameColor(Vector2 pos, Vector2 dir, Ball.COLOR ballColor){
        // color de pixel de la pantalla

        pos = new Vector2(pos.x + 64, pos.y + 64);
        for(int i = 0; i < 15 ; i++)
            pos.add(dir);

        int pixel = ScreenUtils.getFrameBufferPixmap(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()).getPixel(MathUtils.round(pos.x),MathUtils.round(pos.y));
        Color midBallColor = new Color(pixel);

        System.out.println(midBallColor.r + " " + midBallColor.b + " " + midBallColor.g);
        int r = midBallColor.r > 0 ? 1 : 0;
        int g = midBallColor.g > 0 ? 1 : 0;
        int b = midBallColor.b > 0 ? 1 : 0;
        String col = null;

        System.out.println(r + " " + b + " " + g);

        if(r == 1 && g == 0 && b == 0) {
            col = "rojo";
            if (ballColor == RED)
                return true;
        }
        else if(r == 0 && g == 1 && b == 1) {
            col = "azul";
            if (ballColor == BLUE)
                return true;
        }
        else if(r == 1 && g == 1 && b == 0) {
            col = "amarillo";
            if (ballColor == YELLOW)
                return true;
        }
        else if(r == 0 && g == 1 && b == 0) {
            col = "verde";
            if (ballColor == GREEN)
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
