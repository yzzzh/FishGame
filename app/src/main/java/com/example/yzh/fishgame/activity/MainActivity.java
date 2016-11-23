package com.example.yzh.fishgame.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.yzh.fishgame.R;
import com.example.yzh.fishgame.tools.Tools;
import com.example.yzh.fishgame.view.EndView;
import com.example.yzh.fishgame.view.MainView;
import com.example.yzh.fishgame.view.ReadyView;

public class MainActivity extends Activity {

    private MainView mainView = null;
    private ReadyView readyView = null;
    private EndView endView = null;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Tools.TO_MAIN_VIEW){
                toMainView();
            }else if (msg.what == Tools.TO_END_VIEW){
                toEndView();
            }else if (msg.what == Tools.END_GAME){
                endGame();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        readyView = new ReadyView(this);
        setContentView(readyView);
    }

    private void toMainView(){
        if (mainView == null){
            mainView = new MainView(this);
        }
        setContentView(mainView);
        if (readyView != null) {
            readyView.setThreadFlag(false);
            readyView = null;
        }
        if (endView != null){
            endView.setThreadFlag(false);
            endView = null;
        }
    }

    private void toEndView(){
        if (endView == null){
            endView = new EndView(this);
        }
        setContentView(endView);
        if (mainView != null) {
            mainView.setThreadFlag(false);
            mainView = null;
        }
    }

    private void endGame(){
        if (readyView != null){
            readyView.setThreadFlag(false);
            readyView = null;
        }else if (mainView != null){
            mainView.setThreadFlag(false);
            mainView = null;
        }else if (endView != null){
            endView.setThreadFlag(false);
            endView = null;
        }
        this.finish();
    }


    @Override
    public void onBackPressed() {
        endGame();
    }

    public Handler getHandler(){
        return this.handler;
    }
}
