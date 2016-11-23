package com.example.yzh.fishgame.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

/**
 * Created by YZH on 2016/11/17.
 */

abstract public class GameObject {

    protected Bitmap image_left;
    protected Bitmap image_right;
    protected int window_width;
    protected int window_height;
    protected int size_width;
    protected int size_height;
    protected int x;
    protected int y;
    protected int weight;
    protected boolean isAlive;

    protected GameObject(int window_width,int window_height) {
        this.window_width = window_width;
        this.window_height = window_height;
        this.isAlive = true;
        this.x = 0;
        this.y = 0;
    }

    public int getX() {
        return x;
    }

    public int getWeight() {
        return weight;
    }

    public int getY() {
        return y;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getSize_width() {
        return size_width;
    }

    public int getSize_height() {
        return size_height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    abstract void draw(Canvas canvas, Paint paint);
    abstract void collide();
}
