package com.example.yzh.fishgame.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.yzh.fishgame.R;

import java.util.List;

/**
 * Created by YZH on 2016/11/17.
 */

public class MyFish extends GameObject {

    private int life;
    private boolean isReset;
    private int level;
    private List<Bitmap> image_left_list;
    private List<Bitmap> image_right_list;
    private int direction;
    private boolean isInvincible;
    private Bitmap invincible_left_image;
    private Bitmap invincible_right_image;
    private int reset_index;

    public static final int LEVEL_1 = 0;
    public static final int LEVEL_2 = 1;
    public static final int LEVEL_3 = 2;
    public static final int LEVEL_4 = 3;
    public static final int LEVEL_5 = 4;
    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;


    public MyFish(List<Bitmap> image_left_list,List<Bitmap> image_right_list,int window_width, int window_height) {
        super(window_width, window_height);
        this.invincible_left_image = image_left_list.get(4);
        this.invincible_right_image = image_right_list.get(4);
        this.x = (window_width - size_width) / 2;
        this.y = (window_height - size_height) / 2;
        this.image_left_list = image_left_list;
        this.image_right_list = image_right_list;
        this.life = 3;
        this.reset_index = 0;
        this.isReset = false;
        this.isInvincible = false;
        this.level = LEVEL_1;
        getImage();
        this.size_width = image_left.getWidth();
        this.size_height = image_left.getHeight();
        this.weight = this.size_width * this.size_height;
        this.direction = DIRECTION_LEFT;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        getImage();
        if (!isReset) {
            if (direction == DIRECTION_RIGHT) {
                canvas.drawBitmap(image_right, x, y, paint);
            } else if (direction == DIRECTION_LEFT) {
                canvas.drawBitmap(image_left, x, y, paint);
            }
        }else {
            if (reset_index >= 3){
                if (direction == DIRECTION_RIGHT) {
                    canvas.drawBitmap(image_right, x, y, paint);
                } else if (direction == DIRECTION_LEFT) {
                    canvas.drawBitmap(image_left, x, y, paint);
                }
            }
            reset_index++;
            if (reset_index == 6){
                reset_index = 0;
            }
        }
    }

    @Override
    public void collide() {
        if (!isInvincible && !isReset) {
            life--;
        }
        if (life == 0){
            isAlive = false;
        }
        reset();
    }

    public int getWeight(){
        return this.weight;
    }

    public void setReset(boolean isReset){
        this.isReset = isReset;
    }

    private void getImage(){
        if (!isInvincible) {
            image_left = image_left_list.get(level);
            image_right = image_right_list.get(level);
            size_height = image_left.getHeight();
            size_width = image_left.getWidth();
            weight = size_width * size_height;
        }else {
            image_left = invincible_left_image;
            image_right = invincible_right_image;
            size_height = image_left.getHeight();
            size_width = image_left.getWidth();
            weight = size_width * size_height;
        }
    }

    private void reset(){
        isReset = true;
        this.x = (window_width - size_width) / 2;
        this.y = (window_height - size_height) / 2;
    }

    public void levelUp(){
        if (level == LEVEL_1){
            level = LEVEL_2;
        }else if (level == LEVEL_2){
            level = LEVEL_3;
        }else if (level == LEVEL_3){
            level = LEVEL_4;
        }else if (level == LEVEL_4){
            level = LEVEL_5;
        }else {
            return;
        }
    }

    public boolean isAlive(){
        return this.isAlive;
    }
    public boolean isInvincible(){
        return this.isInvincible;
    }
    public boolean isReset(){
        return this.isReset;
    }
    public void setInvincible(boolean isInvincible){
        this.isInvincible = isInvincible;
    }
    public void setDirection(int direction){
        this.direction = direction;
    }
    public int getLife(){
        return this.life;
    }
}
