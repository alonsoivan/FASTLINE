package com.ivn.game.models;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.ivn.game.managers.ResourceManager;

public class Ball extends Sprite {
    public enum Color {
        YELLOW,RED,BLUE,GREEN
    }

    public Vector2 position;
    public Vector2 direction;
    public Circle circle;
    static public float speed = 4f;

    public Color color;
    public boolean hasPowerUp;
    public Util.PowerUp pu;

    public Ball(Vector2 pos, Texture texture, Color color){
        super(texture);

        float ballWidth = Gdx.graphics.getWidth()*0.06f;

        super.setSize(ballWidth,ballWidth);
        super.setPosition(pos.x,pos.y);

        position = pos;
        circle = new Circle(pos.x + ballWidth/2,pos.y + ballWidth/2,ballWidth/2);

        this.color = color;

        getDirection();
    }

    public Ball(Vector2 pos, Texture texture, Color color, boolean hasPowerUp){
        super(texture);
        float ballWidth = Gdx.graphics.getWidth()*0.06f;

        super.setSize(ballWidth,ballWidth);
        super.setPosition(pos.x,pos.y);

        position = pos;
        circle = new Circle(pos.x + ballWidth/2,pos.y + ballWidth/2,ballWidth/2);

        this.color = color;

        this.hasPowerUp = hasPowerUp;

        getDirection();


        if(hasPowerUp)  // TODO hacer otros powerups
            pu = Util.PowerUp.INK;
    }

    public void getDirection(){
        Vector2 screenCentre = new Vector2(Gdx.graphics.getWidth()/2 , Gdx.graphics.getHeight()/2 );

        direction = screenCentre.sub(position);
        direction.nor();

        direction.scl(speed * (Gdx.graphics.getWidth()*0.001f));
    }

    public void mover(){
        position.add(direction);
        super.setPosition(position.x - super.getWidth()/2,position.y - super.getHeight()/2);
        circle.setPosition(new Vector2(position.x ,position.y ));
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        if(hasPowerUp){
            float puWidth = super.getWidth()*0.65f;

            if(pu.equals(Util.PowerUp.INK))
                batch.draw(ResourceManager.inkBall,position.x - puWidth/2,position.y - puWidth/2, puWidth, puWidth);
        }
    }
}
