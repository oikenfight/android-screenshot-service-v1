package com.example.oikawayuta.screenshotservicetest;

import android.media.projection.MediaProjectionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class TestActivity extends AppCompatActivity {
    final static String TAG = "TestActivity";
    private MediaProjectionManager mpManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mpManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);

        Log.w(TAG, "TestActivity onCreate");
    }
}
