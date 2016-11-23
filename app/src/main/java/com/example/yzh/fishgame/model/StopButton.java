package com.example.yzh.fishgame.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by YZH on 2016/11/18.
 */

public class StopButton extends GameObject {
    private Bitmap button_stop_image;
    private Bitmap button_start_image;
    private boolean isStop;

    public StopButton(Bitmap button_stop_image,Bitmap button_start_image,int window_width,int window_height){
        super(window_width,window_height);
        this.button_stop_image = button_stop_image;
        this.button_start_image = button_start_image;
        this.size_width = button_stop_image.getWidth();
        this.size_height = button_stop_image.getHeight();
        this.x = window_width - this.size_width - 20;
        this.y = 50;
        this.isStop = false;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (isStop){
            canvas.drawBitmap(button_stop_image,x,y,paint);
        }else {
            canvas.drawBitmap(button_start_image,x,y,paint);
        }
    }

    @Override
    void collide() {

    }

    public void setStop(boolean isStop){
        this.isStop = isStop;
    }
}
