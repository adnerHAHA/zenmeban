package com.fkl.story;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class GuidActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid);
        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 5000);
    }

    private class splashhandler implements Runnable {
        @Override
        public void run() {
            startActivity(new Intent(GuidActivity.this, MainActivity.class));
            GuidActivity.this.finish();
        }
    }
    }

