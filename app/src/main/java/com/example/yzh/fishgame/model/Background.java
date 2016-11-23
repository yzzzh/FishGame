package com.example.yzh.fishgame.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by YZH on 2016/11/17.
 */

public class Background {

    private Bitmap background_image;
    private int window_width;
    private int window_height;
    private int size_width;
    private int size_height;

    public Background(Bitmap background_image,int window_width,int window_height){
        this.background_image = background_image;
        this.window_width = window_width;
        this.window_height = window_height;
        this.size_width = background_image.getWidth();
        this.size_height = background_image.getHeight();
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(background_image,new Rect(0,0,size_width,size_height),new Rect(0,0,window_width,window_height),paint);
    }

}

