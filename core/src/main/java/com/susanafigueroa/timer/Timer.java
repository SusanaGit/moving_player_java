package com.susanafigueroa.timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.susanafigueroa.helpers.GameInfo;

public class Timer {

    private BitmapFont font;
    private float totalTime;

    public Timer(BitmapFont font, float totalTime) {
        this.font = font;
        this.totalTime = totalTime;
    }

    public void runTimer(SpriteBatch batch) {
        totalTime -= Gdx.graphics.getDeltaTime();

        int minutes = (int) totalTime/60;
        int seconds = (int) totalTime%60;

        String showTimer = minutes + " : " + seconds;
        font.draw(batch, showTimer, GameInfo.WIDTH - (float) GameInfo.WIDTH/15, (float) GameInfo.WIDTH/20);
    }

    public void reduceTimer() {

        if (this.totalTime > 25 ) {
            this.totalTime -= 25;
        } else {
            this.totalTime = 0;
        }
    }

    public void plusTimer() {
        this.totalTime += 10;
    }
}
