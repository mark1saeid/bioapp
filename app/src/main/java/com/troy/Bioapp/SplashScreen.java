package com.troy.Bioapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 4000;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.content_splash_screen);


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent mainIntent = new Intent(SplashScreen.this, LoginActivity.class);
        SplashScreen.this.startActivity(mainIntent);
        SplashScreen.this.finish();

    }
}, SPLASH_DISPLAY_LENGTH);

    }
}
