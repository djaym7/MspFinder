package com.desai.jay.mspfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();

                }finally {
                    Intent i = new Intent("com.desai.jay.MAINACTIVITY");
                    startActivity(i);
                }

            }


        };
        timer.start();
    }
}
