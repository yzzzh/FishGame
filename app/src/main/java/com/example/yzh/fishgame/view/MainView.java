package com.example.yzh.fishgame.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.yzh.fishgame.R;
import com.example.yzh.fishgame.activity.MainActivity;
import com.example.yzh.fishgame.model.Background;
import com.example.yzh.fishgame.model.EnemyFish;
import com.example.yzh.fishgame.model.Goods;
import com.example.yzh.fishgame.model.MyFish;
import com.example.yzh.fishgame.model.StopButton;
import com.example.yzh.fishgame.tools.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by YZH on 2016/11/17.
 */

public class MainView extends SurfaceView implements Runnable,SurfaceHolder.Callback,View.OnTouchListener{

    private List<Bitmap> myfish_left_image_list;
    private List<Bitmap> myfish_right_image_list;
    private List<Bitmap> enemy_left_image_list;
    private List<Bitmap> enemy_right_image_list;
    private Bitmap enemy_image_left;
    private Bitmap enemy_image_right;
    private Bitmap goods_invincible_image;
    private Bitmap stop_button_image;
    private Bitmap start_button_image;
    private Bitmap background_image;

    private int window_width;
    private int window_height;
    private SurfaceHolder surfaceHolder;
    private Thread thread;
    private boolean isRunning;
    private Paint paint;
    private Canvas canvas;
    private Canvas tempCanvas;//二级缓存
    private Bitmap tempBitmap;
    private boolean isSelected;
    private Random random;
    private int score;
    private int enemy_counter;
    private int goods_counter;
    private int level;
    private boolean isStopped;
    private int reset_counter;
    private int invincible_counter;
    private Paint paint_text;

    private MyFish myFish;
    private Background background;
    private StopButton stop_button;
    private ArrayList<EnemyFish> enemy_list;
    private ArrayList<Goods> goods_list;

    private MainActivity mainActivity;

    public MainView(Context context) {
        super(context);

        mainActivity = (MainActivity) context;

        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        setOnTouchListener(this);

        myfish_left_image_list = new ArrayList<Bitmap>();
        myfish_right_image_list = new ArrayList<Bitmap>();
        enemy_left_image_list = new ArrayList<Bitmap>();
        enemy_right_image_list = new ArrayList<Bitmap>();
        initBitmap();

        enemy_list = new ArrayList<EnemyFish>();
        goods_list = new ArrayList<Goods>();

        paint = new Paint();
        isSelected = false;
        random = new Random();
        score = 0;
        enemy_counter = 0;
        goods_counter = 0;
        invincible_counter = 0;
        reset_counter = 0;
        level = 1;
        isStopped = false;
        paint_text = new Paint();
        paint_text.setTextSize(60);
        paint_text.setColor(Color.BLACK);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        window_width = this.getWidth();
        window_height = this.getHeight();
        background = new Background(background_image,window_width,window_height);
        myFish = new MyFish(myfish_left_image_list,myfish_right_image_list,window_width,window_height);
        tempBitmap = Bitmap.createBitmap(window_width,window_height, Bitmap.Config.ARGB_8888);
        stop_button = new StopButton(stop_button_image, start_button_image,window_width,window_height);
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            //点击到自己
            if (x > myFish.getX()
                    && x < myFish.getX() + myFish.getSize_height()
                    && y > myFish.getY()
                    && y < myFish.getY() + myFish.getSize_height()) {
                isSelected = true;
            } else {
                isSelected = false;
            }

            //点击暂停
            if (x > stop_button.getX()
                    && x < stop_button.getX() + stop_button.getSize_width()
                    && y > stop_button.getY()
                    && y < stop_button.getY() + stop_button.getSize_height()){
                if (isStopped){
                    isStopped = false;
                    stop_button.setStop(false);
                    //从暂停恢复为运行
                    synchronized (thread){
                        thread.notify();
                    }

                }else {
                    isStopped = true;
                    stop_button.setStop(true);
                }
            }
        }else if (event.getAction() == MotionEvent.ACTION_MOVE){
            if (isSelected){

//                myFish.setX((int) event.getX() - myFish.getSize_width()/2);
//                myFish.setY((int) event.getY() - myFish.getSize_height()/2);
                if (event.getX() > myFish.getX()){
                    myFish.setDirection(MyFish.DIRECTION_RIGHT);
                }else if (event.getX() < myFish.getX()){
                    myFish.setDirection(MyFish.DIRECTION_LEFT);
                }
                myFish.setX((int)event.getX());
                myFish.setY((int)event.getY());
            }
        }else if (event.getAction() == MotionEvent.ACTION_UP){
            isSelected = false;
        }
        return true;
    }

    @Override
    public void run() {
        while (isRunning) {
            long startTime = System.currentTimeMillis();
            draw();
            if (isStopped){
                synchronized (thread){
                    try {
                        //暂停
                        thread.wait();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            long endTime = System.currentTimeMillis();

            try {
                if (endTime - startTime < 10)
                    Thread.sleep(10 - (endTime - startTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void levelUp(){
        if (level == 1 && score >= 3000){
            level = 2;
            myFish.levelUp();
        }else if (level == 2 && score >= 10000){
            level = 3;
            myFish.levelUp();
        }else if (level == 3 && score >= 20000){
            level = 4;
            myFish.levelUp();
        }else if (level == 4 && score >= 50000){
            level = 5;
            myFish.levelUp();
        }
    }

    private void initBitmap() {
        background_image = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        goods_invincible_image = BitmapFactory.decodeResource(getResources(),R.drawable.goods_invincible);
        stop_button_image = BitmapFactory.decodeResource(getResources(),R.drawable.button_stop);
        start_button_image = BitmapFactory.decodeResource(getResources(),R.drawable.button_start);

        myfish_left_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.myfish1_left));
        myfish_left_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.myfish2_left));
        myfish_left_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.myfish3_left));
        myfish_left_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.myfish4_left));
        myfish_left_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.myfish_invincible_left));

        myfish_right_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.myfish1_right));
        myfish_right_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.myfish2_right));
        myfish_right_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.myfish3_right));
        myfish_right_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.myfish4_right));
        myfish_right_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.myfish_invincible_right));

        enemy_left_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish1_left));
        enemy_left_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish2_left));
        enemy_left_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish3_left));
        enemy_left_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish4_left));
        enemy_left_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish5_left));
        enemy_left_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish6_left));
        enemy_left_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish7_left));
        enemy_left_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish8_left));
        enemy_left_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish9_left));
        enemy_left_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish10_left));

        enemy_right_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish1_right));
        enemy_right_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish2_right));
        enemy_right_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish3_right));
        enemy_right_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish4_right));
        enemy_right_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish5_right));
        enemy_right_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish6_right));
        enemy_right_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish7_right));
        enemy_right_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish8_right));
        enemy_right_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish9_right));
        enemy_right_image_list.add(BitmapFactory.decodeResource(getResources(),R.drawable.fish10_right));
    }

    private void deleteEnemy(){
        for (EnemyFish enemyFish:(ArrayList<EnemyFish>)enemy_list.clone()){
            if (!enemyFish.isAlive()){
                score += enemyFish.getScore();
                enemy_list.remove(enemyFish);
            }
        }
    }

    private void draw(){
        //增加敌人
        if (enemy_counter++ == EnemyFish.GENERATE_RATE){
            //随机生成图片
            int x = random.nextInt(9);
            enemy_image_left = enemy_left_image_list.get(x);
            enemy_image_right = enemy_right_image_list.get(x);
            enemy_list.add(new EnemyFish(enemy_image_left, enemy_image_right,window_width,window_height));
            enemy_counter = 0;
        }
        //增加补给
        if (goods_counter++ == 2000){
            goods_list.add(new Goods(goods_invincible_image,window_width,window_height));
            goods_counter = 0;
        }
        //移动+越界检测+碰撞检测
        for (EnemyFish enemyFish:(ArrayList<EnemyFish>)enemy_list.clone()){
            enemyFish.move();

            if ((enemyFish.getDirection() == EnemyFish.DIRECTION_LEFT && enemyFish.getX() <= - enemyFish.getSize_width()) || (enemyFish.getDirection() == EnemyFish.DIRECTION_RIGHT && enemyFish.getX() >= window_width)) {
                enemy_list.remove(enemyFish);
            }

            if (Tools.isCollide(myFish,enemyFish)) {
                if (myFish.isInvincible() || myFish.isReset()){
                    enemyFish.collide();
                }else {
                    if (myFish.getWeight() >= enemyFish.getWeight()) {
                        enemyFish.collide();
                    } else {
                        myFish.collide();
                    }
                }
            }
        }
        //补给
        for (Goods goods:(ArrayList<Goods>)goods_list.clone()) {
            goods.move();

            if (Tools.isCollide(myFish,goods)){
                    myFish.setInvincible(true);
                    goods_list.remove(goods);
            }
        }

        //删除死去的敌人
        deleteEnemy();

        //升级
        levelUp();

        //重置期间无敌
        if (myFish.isReset()) {
            if (reset_counter++ == 500) {
                myFish.setReset(false);
                reset_counter = 0;
            }
        }
        //无敌时间
        if (myFish.isInvincible()) {
            if (invincible_counter++ == 500) {
                myFish.setInvincible(false);
                invincible_counter = 0;
            }
        }


        //结束游戏
        if (!myFish.isAlive()) {
            endGame();
        }


        //绘图
        try {
            tempCanvas = new Canvas(tempBitmap);

            background.draw(tempCanvas,paint);

            for (EnemyFish enemyFish:enemy_list){
                enemyFish.draw(tempCanvas,paint);
            }

            for (Goods goods:goods_list){
                goods.draw(tempCanvas,paint);
            }

            myFish.draw(tempCanvas,paint);

            stop_button.draw(tempCanvas,paint);

            tempCanvas.drawText("Score : " + score,20,50,paint_text);
            tempCanvas.drawText("Level : " + level,20,150,paint_text);
            tempCanvas.drawText("Life : " + myFish.getLife(),20,250,paint_text);

            canvas = surfaceHolder.lockCanvas();
            canvas.drawBitmap(tempBitmap,0,0,paint);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void setThreadFlag(boolean isRunning){
        this.isRunning = isRunning;
    }

    private void endGame(){
        mainActivity.getHandler().sendEmptyMessage(Tools.TO_END_VIEW);
    }
}
