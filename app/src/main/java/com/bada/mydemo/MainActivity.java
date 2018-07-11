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

    //cd C:\games\Nox\bin && nox_adb.exe connect 127.0.0.1:62001

    WorkingThread workingThread;
    BadaThread badaThread;
    OpenThread openThread;
    ClickThread clickThread;
    AnotherThread anotherThread;
    MyThread myThread;

    public static MainActivity mainActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                try {
//                    if(workingThread != null) {
//                        workingThread.setStop();
//                    }
//                }catch (Throwable e){
//                    e.printStackTrace();
//                }
//                workingThread = new WorkingThread();
//                workingThread.start();

//                openThread = new OpenThread();
//                openThread.start();

//                badaThread = new BadaThread();
//                badaThread.start();

//                clickThread = new ClickThread();
//                clickThread.start();
                anotherThread = new AnotherThread();
                anotherThread.start();

                //OCRManager2.getInstance().doOcr();

//                myThread = new MyThread();
//                myThread.start();
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                workingThread = new WorkingThread();
                workingThread.start();
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                openThread = new OpenThread();
//                openThread.start();

                myThread = new MyThread();
                myThread.start();
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


    @Override
    protected void onDestroy() {
        mainActivity = null;
        System.exit(0);

        super.onDestroy();
    }
}
