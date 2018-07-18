package com.example.oikawayuta.screenshotservicetest;

import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class TestActivity extends AppCompatActivity {
    final static String TAG = "TestActivity";
    private static final int REQUEST_CODE_SCREEN_SHOT = 1001;
    private MediaProjectionManager mpManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mpManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);

        final Intent permissionIntent = mpManager.createScreenCaptureIntent();
        startActivityForResult(permissionIntent, REQUEST_CODE_SCREEN_SHOT);

        Log.w(TAG, "TestActivity onCreate");
    }

    // ユーザーの許可を受け取る
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_SCREEN_SHOT == requestCode) {
            if (resultCode != RESULT_OK) {
                // 拒否された
                Toast.makeText(this,
                        "User cancelled", Toast.LENGTH_LONG).show();
                return;
            }
            startScreenShotService(resultCode, data);
        }
    }

    private void startScreenShotService(final int resultCode, final Intent data) {
        final Intent intent = new Intent(this, ScreenShotService.class);
        intent.putExtra(ScreenShotService.EXTRA_RESULT_CODE, resultCode);
        intent.putExtras(data);
        startService(intent);
    }
}
