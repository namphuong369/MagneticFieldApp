package com.nam.magneticfieldapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.nam.magneticfieldapp.R;

import static maes.tech.intentanim.CustomIntent.customType;

public class SplashScreenActivity extends AppCompatActivity {
    private ImageView iv_item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initView();
        setStatusBar();
        setAnimation();
    }

    private void initView(){
        iv_item=findViewById(R.id.iv_item);
    }
    private void setAnimation(){

        Thread timer=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(2000);
                    startActivity(new Intent(getApplicationContext(),OnBoardingActivity.class));
                    customType(SplashScreenActivity.this, "left-to-right");
                    finish();
                    super.run();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        timer.start();
    }
    private void setStatusBar(){
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }
    }
}
