package com.example.oikawayuta.screenshotservicetest;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;

public class ScreenShotService extends Service{
    final static String TAG = "ScreenShotService";
    private static final String BASE = "ScreenShotService.";
    public static final String EXTRA_RESULT_CODE = BASE + "EXTRA_RESULT_CODE";

    private MediaProjectionManager mpManager;
    private MediaProjection mProjection;
    private ImageReader imageReader;
    private VirtualDisplay virtualDisplay;
    private ScreenShotThread screenShotThread;

    private int screenDensity;
    private int displayWidth;
    private int displayHeight;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "onStartCommand");

        int resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, 0);

        if (resultCode == Activity.RESULT_OK) {
            Log.w(TAG, "screen shot OK. start capture");

            mpManager = (MediaProjectionManager)getSystemService(MEDIA_PROJECTION_SERVICE);

            setDeviceDisplay();
            setUpMediaProjection(intent);

            screenShotThread = new ScreenShotThread(imageReader);
            screenShotThread.start();

        } else {
            // スクリーンショットが許可されていない場合 activity を起動して許可を取りに行く
            Log.w(TAG, "screen shot NG. start activity");

            Intent activityIntent = new Intent(this, MainActivity.class);
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(activityIntent);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy");
    }

    private void setDeviceDisplay() {
        // 画面の縦横サイズとdpを取得
        final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenDensity = displayMetrics.densityDpi / 2;
        displayWidth = displayMetrics.widthPixels / 2;
        displayHeight = displayMetrics.heightPixels / 2;
    }

    private void setUpMediaProjection(Intent intent) {
        final int resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, 0);
        mProjection = mpManager.getMediaProjection(resultCode, intent);
        setUpVirtualDisplay();
    }

    private void setUpVirtualDisplay() {
        imageReader = ImageReader.newInstance(
                displayWidth, displayHeight, PixelFormat.RGBA_8888, 2);

        virtualDisplay = mProjection.createVirtualDisplay("ScreenCapture",
                displayWidth, displayHeight, screenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                imageReader.getSurface(), null, null);
    }
}
