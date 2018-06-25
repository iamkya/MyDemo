package com.bada.mydemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import com.bada.mydemo.dataType.RandomRect;

import java.io.OutputStream;

public class AnotherThread extends BaseThread {

    private static final String screen_cap_file =  "/sdcard/colorPickerTemp.png";

    private RandomRect comfirmExpedition = new RandomRect(1000, 710, 200, 70);
    @Override
    public void run() {
        super.run();

        try {

            sleep(15 * 1000);

//            while (true) {
//
                checkExpedition();
//
//                break;
//            }

        }catch (Throwable e) {
            e.printStackTrace();
        }
    }

    void checkExpedition() throws Exception{


        while (true) {

            if(shouldClick())
                click2(comfirmExpedition);
            else{
                sleep(getSleepTime() * 1000);
            }
        }

    }


    final static int xcoord = 431;
    final static int ycoord = 143;

    void screenCap() {
        exec("/system/bin/screencap -p " + screen_cap_file);
    }

    boolean shouldClick() {
        try {
            screenCap();

            Bitmap screen = BitmapFactory.decodeFile(screen_cap_file);
            int pixel = screen.getPixel(xcoord ,ycoord);
            if(pixel != -527361){
                toast("should click");
                return true;
            }

        }catch (Throwable e) {
            e.printStackTrace();
        }
        toast("no click");
        return false;
    }

}
