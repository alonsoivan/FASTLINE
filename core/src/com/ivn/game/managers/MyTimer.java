package com.ivn.game.managers;

import com.badlogic.gdx.utils.Timer;

public class MyTimer {
    public int seg;
    private int totalSeg;

    public boolean isFinished;
    public boolean isStarted;

    private int originalTotalSeg;

    public MyTimer(int seg){
        this.seg = seg;
        totalSeg = seg ;
        originalTotalSeg = totalSeg;
    }

    private Timer.Task myTimerTask = new Timer.Task() {
        @Override
        public void run() {
            totalSeg--;

            seg = totalSeg % 60;

            if(totalSeg == 0){
                isFinished = true;
                isStarted = false;
            }
        }
    };

    public void start(){
        totalSeg = originalTotalSeg;
        seg = totalSeg % 60;
        isFinished = false;
        isStarted = true;
        Timer.schedule(myTimerTask, 1f, 1f, originalTotalSeg - 1);
    }

    public void stop(){
        myTimerTask.cancel();
        isFinished = true;
        isStarted = false;
    }

    public void restart(){
        stop();
        start();
    }

}
