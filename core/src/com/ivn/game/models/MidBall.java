package com.ivn.game.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.ivn.game.managers.ResourceManager;

import static com.ivn.game.managers.ResourceManager.*;

public class MidBall extends Sprite {

    public Circle circle;
    public static int myScore;
    public static int enemyScore;
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

    }

    public void draw(Batch batch, boolean multi){
        super.draw(batch);

        float cx = Gdx.graphics.getWidth() * 0.11f;
        float cy = Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * 0.05f;
        float cwidth = Gdx.graphics.getWidth() - cx - Gdx.graphics.getWidth() * 0.01f;
        float cheight = Gdx.graphics.getWidth() * 0.020F;

        float sx = cx + Gdx.graphics.getWidth() * 0.005f;
        float sy = cy + Gdx.graphics.getHeight() * 0.01f;
        float swidth = (cwidth - Gdx.graphics.getWidth() * 0.01f) / 100 ;
        float sheight = cheight/2;

        if(multi){
            // SCOREBAR1
            ResourceManager.container.draw(batch, cx , cy, cwidth, cheight);
            if (myScore > 0)
                ResourceManager.scoreBar.draw(batch, sx , sy, swidth * myScore, sheight);


            // SCOREBAR2
            ResourceManager.container.draw(batch, cx , cy - Gdx.graphics.getHeight() * 0.05f, cwidth, cheight);
            if (enemyScore > 0)
                ResourceManager.scoreBar.draw(batch, sx , sy - Gdx.graphics.getHeight() * 0.05f, swidth * enemyScore, sheight);



            if (myScore >= 100 || enemyScore >= 100) {
                myScore = 0;
                enemyScore = 0;
                level += 1f;
                Ball.speed++;
            }
        }
        else {
            float cx2 = Gdx.graphics.getWidth() * 0.01f;
            float cwidth2 = Gdx.graphics.getWidth() - cx2 - Gdx.graphics.getWidth() * 0.01f;

            float sx2 = cx2 + Gdx.graphics.getWidth() * 0.005f;
            float swidth2 = myScore * (cwidth2 - Gdx.graphics.getWidth() * 0.01f) / 100 ;

            // SCOREBAR
            ResourceManager.container.draw(batch, cx2, cy, cwidth2, cheight);
            if (myScore > 0)
                ResourceManager.scoreBar.draw(batch, sx2, sy, swidth2, sheight);

            if (myScore >= 100) {
                myScore = 0;
                level += 1f;
                Ball.speed++;
            }
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
