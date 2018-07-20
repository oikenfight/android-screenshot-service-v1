package com.example.oikawayuta.screenshotservicetest;

import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    final static String TAG = "MainActivity";
    private static final int REQUEST_CODE_SCREEN_SHOT = 1001;
    private MediaProjectionManager mpManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Log.w(TAG, "MainActivity onCreate");

        mpManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);

        final Intent captureIntent = mpManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, REQUEST_CODE_SCREEN_SHOT);
    }

    // ユーザーの許可を受け取る
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (REQUEST_CODE_SCREEN_SHOT == requestCode) {
            if (resultCode != RESULT_OK) {
                Log.w(TAG, "activity request denied.");
                // 拒否された
                Toast.makeText(this,
                        "User cancelled", Toast.LENGTH_LONG).show();
                return;
            }
            Log.w(TAG, "activity request agreeded. start service");
            startScreenShotService(resultCode, intent);

        }
    }

    private void startScreenShotService(final int resultCode, final Intent captureIntent) {
        final Intent intent = new Intent(this, ScreenShotService.class);
        intent.putExtra(ScreenShotService.EXTRA_RESULT_CODE, resultCode);
        intent.putExtras(captureIntent);

        startService(intent);
    }
}
