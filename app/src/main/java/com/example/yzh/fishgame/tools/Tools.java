package com.example.yzh.fishgame.tools;

import com.example.yzh.fishgame.model.EnemyFish;
import com.example.yzh.fishgame.model.GameObject;
import com.example.yzh.fishgame.model.MyFish;

/**
 * Created by YZH on 2016/11/17.
 */

public class Tools {

    public static final int END_GAME = 0;
    public static final int TO_MAIN_VIEW = 1;
    public static final int TO_END_VIEW = 2;

    public static boolean isCollide(GameObject obj_1,GameObject obj_2){
        if (obj_2.getX() > obj_1.getX() && obj_2.getX() < obj_1.getX() + obj_1.getSize_width() &&
                obj_2.getY() > obj_1.getY() && obj_2.getY() < obj_1.getY() + obj_1.getSize_height()){
            return true;
        }else {
            return false;
        }
    }
}
