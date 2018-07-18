package com.example.oikawayuta.screenshotservicetest;

import android.content.Intent;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.projection.MediaProjectionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private MediaProjectionManager mpManager;
    private static final int REQUEST_CODE_SCREEN_SHOT = 1001;
//    private int screenShotPermissionResultCode;
//    private Intent screenShotPermissionIntent;

    private ScreenShotService screenShotService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // 画面の縦横サイズとdpを取得
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        screenDensity = displayMetrics.densityDpi / 2;
//        displayWidth = displayMetrics.widthPixels / 2;
//        displayHeight = displayMetrics.heightPixels / 2;

        mpManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);

        Button startButton = findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: ここで毎回 permission 確認するのがキモいけど、サービス単体で起動するときを考えたら仕方ない。
                final Intent permissionIntent = mpManager.createScreenCaptureIntent();
                // permission 確認後にサービスがスタートする
                startActivityForResult(permissionIntent, REQUEST_CODE_SCREEN_SHOT);
                Log.w("DEBUG", "start button clicked");
            }
        });

        Button stopButton = findViewById(R.id.button_stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getBaseContext(), ScreenShotService.class));
                Log.w("DEBUG", "stop button clicked");
            }
        });
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

//    @Override
//    protected void onDestroy() {
//        if (virtualDisplay != null) {
//            Log.d("debug", "release VirtualDisplay");
//            virtualDisplay.release();
//        }
//        super.onDestroy();
//    }
}
