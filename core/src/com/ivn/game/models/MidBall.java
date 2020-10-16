package com.ivn.game.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.ivn.game.managers.ResourceManager;

public class MidBall extends Sprite {

    public Circle circle;
    public int score;
    public int level;

    public MidBall(Texture texture){
        super(texture);

        float ballWidth = Gdx.graphics.getWidth()*0.2f;

        super.setSize(ballWidth,ballWidth);
        super.setPosition(Gdx.graphics.getWidth()/2 - ballWidth/2,Gdx.graphics.getHeight()/2 - ballWidth/2);
        super.setOriginCenter();

        circle = new Circle(Gdx.graphics.getWidth()/2 ,Gdx.graphics.getHeight()/2 ,ballWidth/2);
    }

    public void rotateLeft(){
        super.setRotation(super.getRotation() + 3);
    }

    public void rotateRight(){
        super.setRotation(super.getRotation() - 3);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);

        // VIDA PANTALLA
        ResourceManager.container.draw(batch, 5, Gdx.graphics.getHeight() - 75, Gdx.graphics.getWidth(), 40);
        ResourceManager.health.draw(batch, 5 + 5, Gdx.graphics.getHeight() - 75 + 5, score*Gdx.graphics.getWidth()/100, 30);
    }
}
