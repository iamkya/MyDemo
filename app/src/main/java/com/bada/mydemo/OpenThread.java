package com.bada.mydemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bada.mydemo.dataType.ClickRect;
import com.bada.mydemo.dataType.RandomRect;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpenThread extends BaseThread {

    private static final String screen_cap_file =  "/sdcard/colorPickerTemp.png";

    HashMap<String, ClickRect> rectMap;
    private static final String fileClickRectData = "OpenThreadClickRectData.dat";

    private RandomRect confirmExpedition = new RandomRect(1010, 720, 180, 50, "confirmExpedition");

    private void init() {
        rectMap = (HashMap<String, ClickRect>)SerializeObject.read(fileClickRectData);
        if(rectMap == null){
            rectMap = new HashMap<>();
        }
    }

    @Override
    public void run() {
        super.run();

        try {
            init();

            sleep(15 * 1000);

            while (true) {

                checkExpedition(false);

                boolean b = startCombat();

                if(b)
                    break;
            }
        }catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void checkExpedition(boolean keepChecking) throws Exception{

        int i = 0;
        while (true) {

            if(shouldClick()) {
                i = 0;
                click2(confirmExpedition);
            }
            else{
                if(keepChecking) {
                    sleep(getSleepTime() * 1000);
                }else{
                    sleep(1000);
                    i++;
                }
            }

            if(!keepChecking && i > 4) {
                break;
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
        toast("NO click");
        return false;
    }

    private static final int maxCombatCount = 4;

    boolean startCombat() throws Throwable{

        for (int i = 0; i < maxCombatCount ; i ++) {

            doCombat(i);

            checkExpedition(false);
        }

        return true;
    }

    private void doCombat(int index) throws Throwable{

//        click2(combatRect);
//
//        click2(zeroTwoRect);
//
//        click2(normalCombatRect);
//
//        //已经进入地图
//
//        click2(startPointRightRect);//点机场
//
//        int fightGroup = index % 2;
//
//        if(fightGroup == 0) {
//
//            performPrepareAndDeploy(fight1, fightGroup);
//
//        }else{
//
//            performPrepareAndDeploy(fight2, fightGroup);
//        }
//
//        //then battle should start
//        battleBegan(index);
    }

}
