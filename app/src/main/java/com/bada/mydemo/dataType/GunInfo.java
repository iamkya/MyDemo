package com.bada.mydemo.dataType;

import android.graphics.Bitmap;

public class GunInfo {

    private int x;
    private int y;
    private int width;
    private int height;

    private RandomRect randomRect;

    public GunInfo(int x, int y, int width, int height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.randomRect = new RandomRect(x, y, width, height);
    }

    private Bitmap bitmap;

    public void setImage(Bitmap screen) {
        this.bitmap = Bitmap.createBitmap(screen, x, y, width, height);
    }

    public Bitmap getBitmap(){
        return bitmap;
    }
}
