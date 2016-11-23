package com.example.yzh.fishgame.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by YZH on 2016/11/18.
 */

public class Goods extends GameObject {

    private Bitmap goods_image;
    private Random random;
    private int speed_x;
    private int speed_y;

    public Goods(Bitmap goods_image, int window_width, int window_height) {
        super(window_width, window_height);
        this.random = new Random();
        if (random.nextBoolean()) {
            this.speed_x = 5;
        }else {
            this.speed_x = -5;
        }
        this.speed_y = 5;
        this.goods_image = goods_image;
        this.size_width = goods_image.getWidth();
        this.size_height = goods_image.getHeight();
        this.x = random.nextInt(window_width - this.size_width);
        this.y = - (random.nextInt(window_height) + this.size_height);
    }


    public void move(){
        if ((speed_x > 0 && x + size_width >= window_width) || (speed_x < 0 && x <= 0)){
            speed_x = - speed_x;
        }
        if ((speed_y > 0 && y + size_height >= window_height) || (speed_y < 0 && y <= 0)){
            speed_y = - speed_y;
        }

        x += speed_x;
        y += speed_y;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(goods_image,x,y,paint);
    }

    @Override
    public void collide() {
        isAlive = false;
    }
}
