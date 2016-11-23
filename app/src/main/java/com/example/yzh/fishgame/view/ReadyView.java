package com.example.yzh.fishgame.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.yzh.fishgame.R;
import com.example.yzh.fishgame.activity.MainActivity;
import com.example.yzh.fishgame.tools.Tools;

/**
 * Created by YZH on 2016/11/23.
 */

public class ReadyView extends SurfaceView implements Runnable,View.OnTouchListener,SurfaceHolder.Callback{

    private int window_width;
    private int window_height;
    private Bitmap background_image;
    private Bitmap start_game_image;
    private Bitmap end_game_image;
    private Canvas canvas;
    private Canvas temp_canvas;
    private Bitmap temp_bitmap;

    private Background background;
    private StartGame startGame;
    private EndGame endGame;

    private Paint paint;
    private boolean isRunning;
    private SurfaceHolder surfaceHolder;
    private Thread thread;

    private MainActivity mainActivity;

    public ReadyView(Context context) {
        super(context);

        mainActivity = (MainActivity) context;

        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        setOnTouchListener(this);

        background_image = BitmapFactory.decodeResource(getResources(),R.drawable.ready_background);
        start_game_image = BitmapFactory.decodeResource(getResources(),R.drawable.game_start);
        end_game_image = BitmapFactory.decodeResource(getResources(),R.drawable.game_over);

        paint = new Paint();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        window_width = this.getWidth();
        window_height = this.getHeight();
        background = new Background(background_image,window_width,window_height);
        startGame = new StartGame(start_game_image,window_width,window_height);
        endGame = new EndGame(end_game_image,window_width,window_height);
        temp_bitmap = Bitmap.createBitmap(window_width,window_height, Bitmap.Config.ARGB_8888);

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
            if (x > startGame.getX()
                    && x < startGame.getX() + startGame.getSize_width()
                    && y > startGame.getY()
                    && y < startGame.getY() + startGame.getSize_height()){
                start_game();
            }else if (x > endGame.getX()
                    && x < endGame.getX() + endGame.getSize_width()
                    && y > endGame.getY()
                    && y < endGame.getY() + endGame.getSize_height()){
                end_game();
            }

        }

        return true;
    }

    @Override
    public void run() {
        while (isRunning){
            long startTime = System.currentTimeMillis();
            draw();
            long endTime = System.currentTimeMillis();

            try {
                if (endTime - startTime < 100)
                    Thread.sleep(100 - (endTime - startTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void draw(){
        try {
            temp_canvas = new Canvas(temp_bitmap);

            background.draw(temp_canvas,paint);
            startGame.draw(temp_canvas,paint);
            endGame.draw(temp_canvas,paint);

            canvas = surfaceHolder.lockCanvas();
            canvas.drawBitmap(temp_bitmap,0,0,paint);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void start_game(){
        mainActivity.getHandler().sendEmptyMessage(Tools.TO_MAIN_VIEW);
    }

    private void end_game(){
        mainActivity.getHandler().sendEmptyMessage(Tools.END_GAME);
    }

    public void setThreadFlag(boolean isRunning){
        this.isRunning = isRunning;
    }

    abstract private class GameImage{
        protected int window_width;
        protected int window_height;
        protected Bitmap game_image;
        protected int size_width;
        protected int size_height;

        public GameImage(Bitmap game_image,int window_width,int window_height){
            this.window_width = window_width;
            this.window_height = window_height;
            this.game_image = game_image;
            this.size_width = game_image.getWidth();
            this.size_height = game_image.getHeight();
        }

        abstract void draw(Canvas canvas,Paint paint);
    }

    private class Background extends GameImage{


        public Background(Bitmap game_image, int window_width, int window_height) {
            super(game_image, window_width, window_height);
        }

        public void draw(Canvas canvas, Paint paint){
            canvas.drawBitmap(game_image,new Rect(0,0,size_width,size_height),new Rect(0,0,window_width,window_height),paint);
        }
    }

    private class StartGame extends GameImage{

        private int x;
        private int y;

        public StartGame(Bitmap game_image, int window_width, int window_height) {
            super(game_image, window_width, window_height);
            this.x = (window_width - size_width) / 2;
            this.y = (window_height - size_height) / 3 * 2 + 50;
        }

        public void draw(Canvas canvas, Paint paint){
            canvas.drawBitmap(game_image,x,y,paint);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getSize_width(){
            return this.size_width;
        }

        public int getSize_height(){
            return this.size_height;
        }
    }

    private class EndGame extends GameImage{

        private int x;
        private int y;

        public EndGame(Bitmap game_image, int window_width, int window_height) {
            super(game_image, window_width, window_height);
            this.x = (window_width - size_width) / 2;
            this.y = (window_height - size_height) / 3 * 2 + 150;
        }

        @Override
        void draw(Canvas canvas, Paint paint) {
            canvas.drawBitmap(game_image,x,y,paint);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getSize_width(){
            return this.size_width;
        }

        public int getSize_height(){
            return this.size_height;
        }
    }
}
