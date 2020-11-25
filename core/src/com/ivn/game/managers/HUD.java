package com.ivn.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.TimeUtils;
import com.ivn.game.models.Ball;
import com.ivn.game.models.MidBall;

import static com.ivn.game.managers.ResourceManager.*;
import static com.ivn.game.models.MidBall.*;

public class HUD {

    // FPS
    long lastTimeCounted = 0;
    private float sinceChange = 0;
    private float frameRate = 0;

    public HUD(){
        /*
        // FPS
        lastTimeCounted = TimeUtils.millis();
        sinceChange = 0;
        frameRate = Gdx.graphics.getFramesPerSecond();
        */

        timer = new MyTimer(30);
        countDown = new MyTimer(3);



        // COUNTDOWN
        countDownFont = assets.get("fonts/OpenSans-Semibold.ttf", BitmapFont.class);
        countDownFont.setUseIntegerPositions(false);
        countDownFont.getColor().a = 0.4f;


        // NAMES
        myName = assets.get("fonts/LemonMilk.otf", BitmapFont.class);
        myName.setUseIntegerPositions(false);

        enemyName = assets.get("fonts/LemonMilk.otf", BitmapFont.class);
        enemyName.setUseIntegerPositions(false);

        // TIMER
        timerFont = assets.get("fonts/LemonMilk.otf", BitmapFont.class);
        timerFont.setUseIntegerPositions(false);

        // SCORE
        score = assets.get("fonts/LemonMilk.otf", BitmapFont.class);
        score.setUseIntegerPositions(false);

        // FPS
        fpsPantalla = assets.get("fonts/LemonMilk.otf", BitmapFont.class);
        fpsPantalla.setUseIntegerPositions(false);
    }

    public void draw(Batch batch, boolean multi){
        update();

        /*
        // FPS
        layout.setText(fpsPantalla,"" + (int)frameRate);
        fpsPantalla.setColor(Color.WHITE);
        fpsPantalla.draw(batch, (int)frameRate + " FPS",  Gdx.graphics.getWidth() /2, 0+layout.height);
        */

        // SCOREBARS
        float cx = Gdx.graphics.getWidth() * 0.01f;
        float cy = Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * 0.05f;
        float cwidth = Gdx.graphics.getWidth() - cx - Gdx.graphics.getWidth() * 0.505f;
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
            ResourceManager.container.draw(batch, cx + cwidth + Gdx.graphics.getWidth() * 0.01f, cy , cwidth, cheight);
            //if (enemyScore >= 0)
            System.out.println(enemyScore);
                ResourceManager.scoreBar.draw(batch, ((sx + cwidth + Gdx.graphics.getWidth() * 0.01f) + (swidth*100)) - enemyScore*swidth , sy , swidth * enemyScore, sheight);

            // NAMES
            timerFont.setColor(Color.WHITE);
            layout.setText(timerFont,"JUGADOR1");
            myName.draw(batch, "JUGADOR1", cx , cy - Gdx.graphics.getHeight() * 0.025f );
            layout.setText(timerFont,"JUGADOR2");
            enemyName.draw(batch, "JUGADOR2",Gdx.graphics.getWidth() -  (layout.width + Gdx.graphics.getWidth() * 0.01f), cy - Gdx.graphics.getHeight() * 0.025f );

            // RONDAS
            myRonda1.setPosition(cx , cy - Gdx.graphics.getWidth() * 0.07f);
            myRonda2.setPosition(cx + (myRonda2.getWidth() / 2) * 3,cy - Gdx.graphics.getWidth() * 0.07f);
            myRonda3.setPosition(cx + (myRonda2.getWidth() / 2) * 6,cy - Gdx.graphics.getWidth() * 0.07f);
            myRonda1.draw(batch);
            myRonda2.draw(batch);
            myRonda3.draw(batch);

            enemyRonda1.setPosition( Gdx.graphics.getWidth() - enemyRonda1.getWidth() - (Gdx.graphics.getWidth() * 0.01f)  ,cy - Gdx.graphics.getWidth() * 0.07f);
            enemyRonda2.setPosition((Gdx.graphics.getWidth() - enemyRonda1.getWidth() - (Gdx.graphics.getWidth() * 0.01f)) - (myRonda2.getWidth() / 2) * 3,cy - Gdx.graphics.getWidth() * 0.07f);
            enemyRonda3.setPosition((Gdx.graphics.getWidth() - enemyRonda1.getWidth() - (Gdx.graphics.getWidth() * 0.01f)) - (myRonda2.getWidth() / 2) * 6,cy - Gdx.graphics.getWidth() * 0.07f);
            enemyRonda1.draw(batch);
            enemyRonda2.draw(batch);
            enemyRonda3.draw(batch);

            // TIMER
            if(timer.isStarted) {
                if(timer.seg <= 5)
                    timerFont.setColor(Color.RED);
                else if(timer.seg == 30)
                    timerFont.setColor(Color.WHITE);

                layout.setText(timerFont,"" + timer.seg);
                timerFont.draw(batch, "" + timer.seg, Gdx.graphics.getWidth() / 2 - layout.width / 2, cy - Gdx.graphics.getHeight() * 0.03f);
            }

            if(countDown.isStarted){
                layout.setText(countDownFont,""+countDown.seg);
                countDownFont.draw(batch, ""+countDown.seg, Gdx.graphics.getWidth() / 2 - layout.width / 2, Gdx.graphics.getHeight() / 2 + layout.height/2);
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


            // SCORE
            layout.setText(fpsPantalla,""+overallScore);
            score.draw(batch, ""+overallScore,Gdx.graphics.getWidth() - ( layout.width + Gdx.graphics.getWidth() * 0.01f ), cy - Gdx.graphics.getWidth() * 0.02f);


            if (myScore >= 100) {
                myScore = 0;
                MidBall.level += 1f;
                Ball.speed++;
            }
        }

    }

    public void update() {
        // FPS
        long delta = TimeUtils.timeSinceMillis(lastTimeCounted);
        lastTimeCounted = TimeUtils.millis();

        sinceChange += delta;
        if(sinceChange >= 1000) {
            sinceChange = 0;
            frameRate = Gdx.graphics.getFramesPerSecond();
        }
    }

    static public void setRounds(int myWinRounds, int enemyWinRounds){
        switch (myWinRounds){
            case 3:
                myRonda3.setTexture(textureTurnosW);
            case 2:
                myRonda2.setTexture(textureTurnosW);
            case 1:
                myRonda1.setTexture(textureTurnosW);
                break;
        }

        switch (enemyWinRounds){
            case 3:
                enemyRonda3.setTexture(textureTurnosW);
            case 2:
                enemyRonda2.setTexture(textureTurnosW);
            case 1:
                enemyRonda1.setTexture(textureTurnosW);
                break;
        }

    }
}