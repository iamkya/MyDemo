package com.bada.mydemo;

import android.widget.Toast;

import com.bada.mydemo.dataType.RandomRect;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.Random;

public class BaseThread extends Thread {

    Process shell = null;
    DataOutputStream os = null;
    InputStream is = null;

    void toast(final String str){

        if(MainActivity.mainActivity != null) {
            MainActivity.mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.mainActivity, str, Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    int getSleepTime() {

        Random rand = new Random();
        int value = rand.nextInt(20) + 10 ;

        return value;
    }

    @Override
    public void run() {

        try {
            shell = Runtime.getRuntime().exec("su", null,null);
            os = new DataOutputStream(shell.getOutputStream());
            is = shell.getInputStream();

        }catch (Throwable e){
            e.printStackTrace();
        }

    }

    void exec(String command) {

        DebugUtil.e("exec " + command);
        try {

            os.writeBytes((command));
            os.writeBytes("\n");
            os.flush();

            //TODO find better ways ??
            /*quote"of course, this assumes that no other command would output anything to the shell's
            stdout stream (still works if another command outputs anything to the shell's stderr stream) "
            */

            os.writeBytes("echo -n 0\n");
            os.flush();

            is.read();

//            Bitmap screen = BitmapFactory.decodeFile(screen_cap_path);

//            Bitmap part = getPart(screen);

        }catch (Throwable e) {
            e.printStackTrace();
        }
    }

    Random random = new Random();
    protected int getSmallRan(){
        return random.nextInt(500);
    }

    void click(RandomRect rect, int sleepTime) {

        DebugUtil.e("clicking " + rect.getTag());

        String command = rect.getClickPoint();
        exec("input tap " + command);

        try {
            if(sleepTime != 0f){
                Thread.sleep((long) (sleepTime  * 1000 + getSmallRan()));
            }

        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    void click1(RandomRect rect) {
        click(rect, 1);
    }

    void click2(RandomRect rect) {
        click(rect, 2);
    }

    void click4(RandomRect rect) {
        click(rect, 4);
    }


    void click3(RandomRect rect) {
        click(rect, 3);
    }

    void click2(RandomRect... rects){

        for(RandomRect rect:rects){
            click2(rect);
        }
    }

    static final String screen_cap_path = "/sdcard/colorPickerTemp.png";

    boolean capture() {
        try {

            os.writeBytes(("/system/bin/screencap -p " + screen_cap_path));
            os.writeBytes("\n");
            os.flush();

            //TODO find better ways ??
            /*quote"of course, this assumes that no other command would output anything to the shell's
            stdout stream (still works if another command outputs anything to the shell's stderr stream) "
            */

            os.writeBytes("echo -n 0\n");
            os.flush();

            is.read();

//            Bitmap screen = BitmapFactory.decodeFile(screen_cap_path);

//            Bitmap part = getPart(screen);

        }catch (Throwable e) {
            e.printStackTrace();
        }

        return false;
    }

}
