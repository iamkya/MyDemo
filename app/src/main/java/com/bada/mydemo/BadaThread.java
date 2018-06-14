package com.bada.mydemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bada.mydemo.rect.FocusRect;

import java.io.OutputStream;

public class BadaThread extends Thread {

    Process shell = null;

    private static final String screen_cap_path = "/sdcard/colorPickerTemp.png";
    @Override
    public void run() {

        try {
            shell = Runtime.getRuntime().exec("su", null,null);
        }catch (Throwable e){
            e.printStackTrace();
        }


    }

    static final FocusRect friendInfo = new FocusRect(0, 0, 1000, 200);

    boolean capture() {
        try {
            OutputStream os = null;

            os = shell.getOutputStream();
            os.write(("/system/bin/screencap -p " + screen_cap_path).getBytes("ASCII"));
            os.flush();

            os.close();
            shell.waitFor();

            Bitmap screen = BitmapFactory.decodeFile(screen_cap_path);

            Bitmap part = getPart(screen, friendInfo);


        }catch (Throwable e) {
            e.printStackTrace();
        }

        return false;
    }

    private Bitmap getPart(Bitmap screen, FocusRect rect) {
        return Bitmap.createBitmap(screen, rect.getTopX(), rect.getTopY(), rect.getWidth(), rect.getHeight());
    }
}
