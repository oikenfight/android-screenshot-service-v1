package com.example.oikawayuta.screenshotservicetest;

import android.graphics.Bitmap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenShotThread extends Thread implements Runnable {
    private final String TAG = "ScreenShotService";
    private ImageReader imgReader;
    private boolean running = true;
    private long before;

    ScreenShotThread(ImageReader imageReader) {
        imgReader = imageReader;
    }

    public void run() {
        long before = System.currentTimeMillis();

        while (this.running) {
            Log.w(TAG, "thread is running");

            VoidDiff("1");

            Image image = prepareImageReader();

            VoidDiff("2");

            Image.Plane[] planes = image.getPlanes();

            VoidDiff("3");

            ByteBuffer buffer = planes[0].getBuffer();

            VoidDiff("4");

            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * imgReader.getWidth();

            int width = (imgReader.getWidth() + rowPadding / pixelStride);
            int height = imgReader.getHeight();

            VoidDiff("5");

            // バッファからBitmapを生成
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);

            VoidDiff("6");

            // save Image
            saveImage(bitmap);

            VoidDiff("7");

            image.close();
        }
    }

    public void stopThread() {
        this.running = false;
    }

    protected void saveImage(Bitmap bitmap) {
        try {
            // sdcardフォルダを指定
            File root = Environment.getExternalStorageDirectory();

            // 日付でファイル名を作成
            Date mDate = new Date();
            SimpleDateFormat fileName = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");

            File newFile = new File(root + "/screencap/", fileName.format(mDate) + ".png");


            // 保存処理開始
            FileOutputStream fos = null;
            fos = new FileOutputStream(new File(root + "/screencap/", fileName.format(mDate) + ".png"));

            VoidDiff("6-1");

            // pngで保存
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            VoidDiff("6-2");

            // 保存処理終了
            fos.close();
        } catch (Exception e) {
            Log.e("Error", "" + e.toString());
        }
    }

    protected void VoidDiff(String tag) {
        long now = System.currentTimeMillis();
        Log.w(tag, String.valueOf(now - before) + "ms");
        before = now;
    }

    protected Image prepareImageReader() {
        Image image = imgReader.acquireLatestImage();
        if (image == null) {
            try {
                Thread.sleep(50);
                image = prepareImageReader();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return image;
    }
}
