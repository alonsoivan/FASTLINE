package com.ivn.game.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Ball extends Sprite {
    public enum COLOR {
        YELLOW,RED,BLUE,GREEN
    }

    public Vector2 position;
    public Vector2 direction;
    public Circle circle;
    static public int speed = 6;

    public COLOR color;

    public Ball(Vector2 pos, Texture texture, COLOR color){
        super(texture);

        float ballWidth = Gdx.graphics.getWidth()*0.06f;

        super.setSize(ballWidth,ballWidth);
        super.setPosition(pos.x,pos.y);

        position = pos;
        circle = new Circle(pos.x + ballWidth/2,pos.y + ballWidth/2,ballWidth/2);

        this.color = color;

        getDirection();
    }

    public void getDirection(){
        Vector2 centro = new Vector2(Gdx.graphics.getWidth()/2 - super.getWidth(), Gdx.graphics.getHeight()/2 - super.getHeight()/2);

        direction = centro.sub(position);
        direction.nor();

        direction.scl(speed);
    }

    public void mover(){
        position.add(direction);
        super.setPosition(position.x ,position.y );
        circle.setPosition(new Vector2(position.x  + super.getWidth()/2,position.y  + super.getHeight()/2));
    }
}
