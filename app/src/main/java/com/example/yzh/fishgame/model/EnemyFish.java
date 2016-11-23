package com.example.yzh.fishgame.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by YZH on 2016/11/17.
 */

public class EnemyFish extends GameObject {

    private int score;
    private int speed;
    private Random random;
    private int direction;
    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;
    public static final int GENERATE_RATE = 100;

    public EnemyFish(Bitmap image_left,Bitmap image_right,int window_width, int window_height) {
        super(window_width, window_height);
        this.image_left = image_left;
        this.image_right = image_right;
        this.size_width = image_left.getWidth();
        this.size_height = image_left.getHeight();
        this.random = new Random();
        this.speed = random.nextInt(5);
        if (random.nextBoolean()){
            //在右边出现
            this.x = random.nextInt(window_width) + window_width;
            this.direction = DIRECTION_LEFT;
            this.speed *= -1;
        }else {
            //在左边出现
            this.x = - random.nextInt(window_width);
            this.direction = DIRECTION_RIGHT;
        }
        this.y = random.nextInt(window_height - size_height);
        this.weight = size_width * size_height;
        this.score = this.weight / 100;

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (this.direction == DIRECTION_RIGHT) {
            canvas.drawBitmap(image_right,x,y,paint);
        }else if (this.direction == DIRECTION_LEFT){
            canvas.drawBitmap(image_left,x,y,paint);
        }

    }

    public int getScore(){
        return this.score;
    }

    public boolean isAlive(){
        return this.isAlive;
    }

    public int getDirection(){
        return this.direction;
    }

    @Override
    public void collide() {
        this.isAlive = false;
    }

    public void move() {
        this.x += speed;
    }
}
