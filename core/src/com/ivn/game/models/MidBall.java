package com.ivn.game.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.ivn.game.managers.ResourceManager;

import static com.ivn.game.managers.ResourceManager.*;

public class MidBall extends Sprite {

    public Circle circle;
    public int score;
    public int level;

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

    @Override
    public void draw(Batch batch) {
        super.draw(batch);

        // VIDA PANTALLA
        ResourceManager.container.draw(batch, 10, Gdx.graphics.getHeight() - 75, Gdx.graphics.getWidth() - 20 , 40);
        ResourceManager.scoreBar.draw(batch, 5 + 10 , Gdx.graphics.getHeight() - 75 + 5, score*Gdx.graphics.getWidth()/100 -20, 30);

        if(score >= 100){
            score = 0;
            level++;
            Ball.speed++;
        }
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
