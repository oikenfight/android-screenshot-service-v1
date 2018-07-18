package com.example.oikawayuta.screenshotservicetest;

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
import android.widget.ImageView;

public class ScreenShotService extends Service {
    private static final String BASE = "ScreenShotService.";
    public static final String EXTRA_RESULT_CODE = BASE + "EXTRA_RESULT_CODE";
    private MediaProjectionManager mpManager;
    private MediaProjection mProjection;
    private ImageReader imageReader;
    private VirtualDisplay virtualDisplay;

    private int screenDensity;
    private int displayWidth;
    private int displayHeight;

    private final String TAG = "ScreenShotService";
    private boolean running = true;
    private long before;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mpManager = (MediaProjectionManager)getSystemService(MEDIA_PROJECTION_SERVICE);

        // 画面の縦横サイズとdpを取得
        final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenDensity = displayMetrics.densityDpi;
        displayWidth = displayMetrics.widthPixels;
        displayHeight = displayMetrics.heightPixels;

        Log.w(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "onStartCommand");

        // setUp
        setUpMediaProjection(intent);

        // TODO: run ScreenShot Thread

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "OnDestroy");
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