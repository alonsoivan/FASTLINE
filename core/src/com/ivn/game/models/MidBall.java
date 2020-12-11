package com.ivn.game.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.ivn.game.managers.ResourceManager;
import com.ivn.game.screens.MultiPlayerScreen;

import static com.ivn.game.managers.ResourceManager.midBall3;
import static com.ivn.game.managers.ResourceManager.midBall4;
import static com.ivn.game.screens.MultiPlayerScreen.freezed;
import static com.ivn.game.screens.MultiPlayerScreen.opacity;
import static com.ivn.game.screens.SinglePlayerScreen.State.PAUSE;

public class MidBall extends Sprite {

    public Circle circle;
    public static String myName = "";
    public static String enemyName = "";

    public static int myScore;
    public static int enemyScore;
    public static int overallScore = 0;

    public static int myWinRounds = 0;
    public static int enemyWinRounds = 0;

    public static int level;

    public static int racha;

    public MidBall(){
        super(midBall3);

        float ballWidth = Gdx.graphics.getWidth()*0.2f;

        super.setSize(ballWidth,ballWidth);
        super.setPosition(Gdx.graphics.getWidth()/2 - ballWidth/2,Gdx.graphics.getHeight()/2 - ballWidth/2);
        super.setOriginCenter();

        level = 1;

        circle = new Circle(Gdx.graphics.getWidth()/2 ,Gdx.graphics.getHeight()/2 ,ballWidth/2);

        racha = 0;
    }

    public void rotateLeft(){
        super.setRotation(super.getRotation() + 5);
    }

    public void rotateRight(){
        super.setRotation(super.getRotation() - 5);
    }

    public static void restart(){
        level = 1;
        Ball.speed = 4f;
        myScore = 0;
        enemyScore = 0;
        overallScore = 0;
        myWinRounds = 0;
        enemyWinRounds = 0;
        MultiPlayerScreen.balls.clear();

        racha = 1;
    }

    public static void nextRound(){
        Gdx.input.cancelVibrate();

        freezed = false;

        MultiPlayerScreen.balls.clear();
        myScore = 0;
        enemyScore = 0;
        MidBall.level += 1f;
        Ball.speed++;

        opacity = 0;

        ResourceManager.timer.stop();
        ResourceManager.countDown.restart();

        MultiPlayerScreen.state = PAUSE;

        racha /= 2;
    }

    public void changeColors(int amount){

        float rotation = getRotation();

        if(amount == 3)
            setTexture(midBall3);
        else if(amount == 4)
            setTexture(midBall4);


        setRotation(rotation);
    }
}
