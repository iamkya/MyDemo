package com.bada.mydemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;

import com.bada.mydemo.dataType.GunInfo;
import com.bada.mydemo.dataType.RandomRect;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class ClickThread extends Thread {

    private RandomRect combactRect = new RandomRect();
    private RandomRect zeroTwoRect = new RandomRect();
    private RandomRect normalCombatRect = new RandomRect();

    private RandomRect startPointLeftRect = new RandomRect(); //airport
    private RandomRect startPointRightRect = new RandomRect(); //command center

    private RandomRect groupFormationRect = new RandomRect();

    private RandomRect selectGroup1AtDeploy = new RandomRect();
    private RandomRect selectGroup2AtDeploy = new RandomRect();



    Process shell = null;
    DataOutputStream os = null;
    InputStream is = null;

    private static final String screen_group1 = "/sdcard/bada/group1.jpg";

    @Override
    public void run() {

        try {

            shell = Runtime.getRuntime().exec("gtsu", null,null);
            os = new DataOutputStream(shell.getOutputStream());
            is = shell.getInputStream();

            while (true) {

//                click2(combactRect);

//                click2(zeroTwoRect);

//                click2(normalCombatRect);

                //check which group has ammo

                checkAmmo();

                break;
            }
        }catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //start x = 210 y = 213 , rect 246 ,617 , spacing 14

    ArrayList<GunInfo> gunInfos = new ArrayList<>();

    void checkAmmo() {

        Bitmap screen = BitmapFactory.decodeFile(screen_group1);

        for (int i = 0; i < 5; i ++) {

            int x = 222 + i * (260 + 17);
            int y = 230;
            int width = 263;
            int height = 655;

            GunInfo gunInfo = new GunInfo(x, y, width, height);

            gunInfo.setImage(screen);

            gunInfos.add(gunInfo);
        }

        GunInfo gunInfo1 = gunInfos.get(0);

        Bitmap gray = convertGray(gunInfo1.getBitmap());

        OCRManager.getInstance().setImage(gray);

        Rect rect = new Rect(0, 441, 260, 474); //name
        String str1 = OCRManager.getInstance().doOcrRect(rect);
        DebugUtil.e("str1 " + str1);

        Rect rect1 = new Rect(rect.left, rect.top + 33, rect.right, rect.bottom + 33); //type
        String str2 = OCRManager.getInstance().doOcrRect(rect1);
        DebugUtil.e("str2 " + str2);

        Rect life = new Rect(0, 520, 78, 565); //life
        String slife = OCRManager.getInstance().doOcrRect(life);
        DebugUtil.e("slife " + slife);

        Rect lifeNumber = new Rect(78, 520, 260, 565);
        String sln = OCRManager.getInstance().doOcrRect(lifeNumber, true);
        DebugUtil.e("sln " + sln);
    }

    ColorMatrix colorMatrix;

    public Bitmap convertGray(Bitmap bitmap3) {
        colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);

        Paint paint = new Paint();
        paint.setColorFilter(filter);
        Bitmap result = Bitmap.createBitmap(bitmap3.getWidth(), bitmap3.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

        canvas.drawBitmap(bitmap3, 0, 0, paint);
        return result;
    }

    private void click(RandomRect rect) {

        click(rect, 0.5f);
    }

    private void click2(RandomRect rect) {
        click(rect, 2);
    }

    private void click(RandomRect rect, float waitTime) {

        try {
            String xy = rect.getClickPoint();

            if(xy.equals("0 0"))
                return;

            String cmd = "input tap " + xy;

            os.writeBytes((cmd));

            os.writeBytes("\n");
            os.flush();

            //TODO find better ways ??
            /*quote"of course, this assumes that no other command would output anything to the shell's
            stdout stream (still works if another command outputs anything to the shell's stderr stream) "
            */

            os.writeBytes("echo -n 0\n");
            os.flush();

            is.read();

            Thread.sleep((long)waitTime * 1000);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

}
