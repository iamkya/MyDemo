package com.bada.mydemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import com.bada.mydemo.dataType.RandomRect;

import java.io.OutputStream;

public class AnotherThread extends BaseThread {

    private static final String screen_cap_file =  "/sdcard/colorPickerTemp.png";

    private RandomRect confirmExpedition = new RandomRect(1000, 710, 200, 70);
    @Override
    public void run() {
        super.run();

        try {


//            sleep(15 * 1000);
//
//            while (true) {
//
//                checkExpedition(false);
//
//                checkExpedition(true);
//
                startCombat();
//            }

        }catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static final int maxCombatCount = 4;

    private RandomRect combatRect = new RandomRect();
    private RandomRect zeroTwoRect = new RandomRect();
    private RandomRect normalCombatRect = new RandomRect();

    private RandomRect startPointLeftRect = new RandomRect(); //airport
    private RandomRect startPointRightRect = new RandomRect(); //command center

    private RandomRect groupFormationRect = new RandomRect();

    private RandomRect selectGroup1AtDeploy = new RandomRect();
    private RandomRect selectGroup2AtDeploy = new RandomRect();

    void startCombat(){

        for (int i = 0; i < maxCombatCount ; i ++) {

            doCombat(i);
        }


    }

    private void doCombat(int index) {

        click2(combatRect);

        click2(zeroTwoRect);

        click2(normalCombatRect);

        click2(startPointRightRect);//点机场

        //弹出队伍选择
        click2(groupFormationRect);

        int fightGroup = index%2;
        int waitGroup = (index + 1)%2;


    }

    private void checkExpedition(boolean keepChecking) throws Exception{

        int i = 0;
        while (true) {

            if(shouldClick()) {
                i = 0;
                click2(confirmExpedition);
            }
            else{
                sleep(getSleepTime() * 1000);
                i++;
            }

            if(!keepChecking && i > 11) {
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
        toast("no click");
        return false;
    }

}
