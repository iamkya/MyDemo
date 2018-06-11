package com.bada.mydemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.OutputStream;
import java.util.Random;

public class WorkingThread extends Thread{
    @Override
    public void run() {
        try {

            Thread.sleep(getSleepTime() * 1000);

            while (true) {

                if(shouldClick()) {
                    click();

                    Thread.sleep(500);
                }else{

                    Thread.sleep(getSleepTime() * 1000);
                }
            }


        }catch (Throwable e){
            e.printStackTrace();
        }

    }

    private void click() {

        try {
            OutputStream os = null;

            Process sh = Runtime.getRuntime().exec("su", null, null);

            String cmd = "input tap " + getClickPoint();

            os = sh.getOutputStream();

//                    os.write(("input tap 1120 750").getBytes("ASCII"));
            os.write((cmd).getBytes("ASCII"));

            Log.e("cmd", cmd);

            os.flush();
            os.close();

            sh.waitFor();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private int getSleepTime() {

        Random rand = new Random();
        int value = rand.nextInt(20) + 10 ;

        return value;
    }

    private String getClickPoint() {
        Random rand = new Random();

        int x = rand.nextInt(200) + 1000;
        int y = rand.nextInt(70) + 710;

        return x + " " + y;
    }

    final static int xcoord = 431;
    final static int ycoord = 143;

    boolean shouldClick() {
        try {
            OutputStream os = null;
            Process sh = Runtime.getRuntime().exec("su", null,null);

            os = sh.getOutputStream();
            os.write(("/system/bin/screencap -p " + "/sdcard/colorPickerTemp.png").getBytes("ASCII"));
            os.flush();

            os.close();
            sh.waitFor();

            Bitmap screen = BitmapFactory.decodeFile("/sdcard/colorPickerTemp.png");
            int pixel = screen.getPixel(xcoord ,ycoord);
            Log.e("cmd", "shouldClick = " + (pixel != -527361));

            if(pixel != -527361){
                return true;
            }

        }catch (Throwable e) {
            e.printStackTrace();
        }

        return false;
    }
}
