package com.bada.mydemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import com.bada.mydemo.dataType.RandomRect;

import java.io.OutputStream;
import java.util.ArrayList;

public class AnotherThread extends BaseThread {

    private static final String screen_cap_file =  "/sdcard/colorPickerTemp.png";

    private RandomRect confirmExpedition = new RandomRect(1000, 710, 200, 70);
    @Override
    public void run() {
        super.run();

        try {

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

    private static final int maxCombatCount = 4;

    private RandomRect combatRect = new RandomRect();
    private RandomRect zeroTwoRect = new RandomRect();
    private RandomRect normalCombatRect = new RandomRect();

    private RandomRect startPointLeftRect = new RandomRect(); //airport
    private RandomRect startPointRightRect = new RandomRect(); //command center

    private RandomRect groupFormationRect = new RandomRect();

    private RandomRect selectGroup1AtDeploy = new RandomRect();
    private RandomRect selectGroup2AtDeploy = new RandomRect();

    private RandomRect selectGroup1AtFormation = new RandomRect();
    private RandomRect selectGroup2AtFormation = new RandomRect();

    private RandomRect savedFormationAtGroupDetail = new RandomRect();
    private RandomRect savedFormationRect = new RandomRect();

    private RandomRect formation1 = new RandomRect();
    private RandomRect formation2 = new RandomRect();

    private RandomRect applyFormationButton = new RandomRect();

    private RandomRect forceApplyDialogButton = new RandomRect();
    private RandomRect formationBackButton = new RandomRect();

    private RandomRect deployConfirmButton = new RandomRect();

    private RandomRect startBattleButton = new RandomRect();

    private RandomRect supplyButton = new RandomRect();

    private RandomRect planModeButton = new RandomRect();

    private RandomRect enemy1 = new RandomRect();
    private RandomRect enemy2 = new RandomRect();
    private RandomRect empty1 = new RandomRect();
    private RandomRect enemy3 = new RandomRect();

    private RandomRect enemy4 = new RandomRect();
    private RandomRect enemy5 = new RandomRect();

    private RandomRect endRound = new RandomRect();//结束回合

    private RandomRect execPlanButton = new RandomRect();

    boolean startCombat() throws Throwable{
        prepareCombat();

        for (int i = 0; i < maxCombatCount ; i ++) {

            doCombat(i);

            checkExpedition(false);
        }

        return true;
    }

    ArrayList<RandomRect> fight1 = new ArrayList<>();
    ArrayList<RandomRect> fight2 = new ArrayList<>();


    void prepareGroup1(){
        fight1.clear();

        fight1.add(selectGroup1AtDeploy);
        fight1.add(groupFormationRect);
        fight1.add(savedFormationAtGroupDetail);
        fight1.add(savedFormationRect);
        fight1.add(formation2);
        fight1.add(applyFormationButton);
        fight1.add(forceApplyDialogButton);

    }

    void prepareGroup2(){
        fight2.clear();

        fight2.add(selectGroup2AtDeploy);
        fight2.add(groupFormationRect);
        fight2.add(savedFormationAtGroupDetail);
        fight2.add(savedFormationRect);
        fight2.add(formation1);
        fight2.add(applyFormationButton);
        fight2.add(forceApplyDialogButton);
    }

    void prepareCombat() {
        prepareGroup1();
        prepareGroup2();
    }

    private void doCombat(int index) throws Throwable{

        click2(combatRect);

        click2(zeroTwoRect);

        click2(normalCombatRect);

        //已经进入地图

        click2(startPointRightRect);//点机场

        int fightGroup = index % 2;

        if(fightGroup == 0) {

            performPrepareAndDeploy(fight1, fightGroup);

        }else{

            performPrepareAndDeploy(fight2, fightGroup);
        }

        //then battle should start
        battleBegan(index);
    }

    void battleBegan(int index) throws Throwable{

        click2(startBattleButton);

        click2(startPointLeftRect);
        click2(supplyButton);

        if(index == 0){
            click2(startPointRightRect);
        }

        click2(planModeButton);

        click2(startPointRightRect);//机场

        click2(enemy1);
        click2(enemy2);

        //TODO drag
        exec("input swipe ");

        click2(empty1);
        click2(enemy3);

        click2(execPlanButton);

        sleep(60 * 1000);


        click2(endRound);
        sleep(20 * 1000);


        click2(planModeButton);

        click2(enemy3);
        click2(enemy4);
        click2(enemy5);
        click2(execPlanButton);

        sleep(40 * 1000);

        click2(endRound);
    }


    void performPrepareAndDeploy(ArrayList<RandomRect> rectArrayList, int fightGroup){

        for(RandomRect rect : rectArrayList){

            click2(rect);
        }

        if(fightGroup == 0) {
            click2(selectGroup2AtFormation);
        }else{
            click2(selectGroup1AtFormation);
        }

        //TODO drag
        exec("input swipe ");

        click2(formationBackButton);

        if(fightGroup == 0) {

            click2(startPointRightRect);
            click2(deployConfirmButton);

            click2(startPointLeftRect);
            click2(deployConfirmButton);
        }else{

            click2(startPointLeftRect);
            click2(deployConfirmButton);

            click2(startPointRightRect);
            click2(deployConfirmButton);
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
                sleep(1000);
                i++;
            }

            if(!keepChecking && i > 6) {
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
