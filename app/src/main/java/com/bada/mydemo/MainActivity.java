package com.bada.mydemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.OutputStream;
import java.util.Random;

public class MainActivity extends Activity {

    WorkingThread workingThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(workingThread != null) {
                        workingThread.setStop();
                    }
                }catch (Throwable e){
                    e.printStackTrace();
                }
                workingThread = new WorkingThread();
                workingThread.start();
            }
        });
    }

//    private void work(int xcoord, int ycoord)
//    {
//        OutputStream os = null;
//        try {
//            Process sh = Runtime.getRuntime().exec("su", null,null);
//
//            os = sh.getOutputStream();
//            os.write(("/system/bin/screencap -p " + "/sdcard/colorPickerTemp.png").getBytes("ASCII"));
//            os.flush();
//
//            os.close();
//            sh.waitFor();
//
//            Bitmap screen = BitmapFactory.decodeFile("/sdcard/colorPickerTemp.png");
//            int pixel = screen.getPixel(xcoord ,ycoord);
//            //Log.e("cmd", "Pixel Color: + " + pixel + " at x:" + xcoord + " y:" + ycoord);
//
//            if(pixel != -527361) {
//
//                String cmd = "input tap " + getClickPoint();
//                for(int i = 0; i < 5; i++) {
//
//                    sh = Runtime.getRuntime().exec("su", null,null);
//                    os = sh.getOutputStream();
//
////                    os.write(("input tap 1120 750").getBytes("ASCII"));
//                    os.write((cmd).getBytes("ASCII"));
//
//                    //Log.e("cmd", cmd);
//
//                    os.flush();
//                    os.close();
//
//                    sh.waitFor();
//
//                    Thread.sleep(500);
//                }
//
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
}
