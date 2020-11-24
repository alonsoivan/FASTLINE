package com.ivn.game.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.ivn.game.managers.HUD;
import com.ivn.game.managers.ResourceManager;
import com.ivn.game.screens.MultiPlayerScreen;
import com.ivn.game.screens.SinglePlayerScreen;

import static com.ivn.game.managers.ResourceManager.*;
import static com.ivn.game.screens.SinglePlayerScreen.State.PAUSE;

public class MidBall extends Sprite {

    public Circle circle;
    public static int myScore;
    public static int enemyScore;
    public static int overallScore = 0;

    public static int myWinRounds = 0;
    public static int enemyWinRounds = 0;

    public static int level;

    public MidBall(){
        super(midBall3);

        float ballWidth = Gdx.graphics.getWidth()*0.2f;

        super.setSize(ballWidth,ballWidth);
        super.setPosition(Gdx.graphics.getWidth()/2 - ballWidth/2,Gdx.graphics.getHeight()/2 - ballWidth/2);
        super.setOriginCenter();

        level = 1;

        circle = new Circle(Gdx.graphics.getWidth()/2 ,Gdx.graphics.getHeight()/2 ,ballWidth/2);
    }

    public void rotateLeft(){
        super.setRotation(super.getRotation() + 4);
    }

    public void rotateRight(){
        super.setRotation(super.getRotation() - 4);
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
    }

    public static void nextRound(){
        MultiPlayerScreen.balls.clear();
        myScore = 0;
        enemyScore = 0;
        MidBall.level += 1f;
        Ball.speed++;

        ResourceManager.timer.stop();
        ResourceManager.countDown.restart();

        MultiPlayerScreen.state = PAUSE;
    }

    public void changeColors(int amount){

        float rotation = getRotation();

        if(amount == 3)
            setTexture(midBall3);
        else if(amount == 4)
            setTexture(midBall4);
        else if(amount == 5)
            setTexture(midBall5);

        setRotation(rotation);
    }
}
