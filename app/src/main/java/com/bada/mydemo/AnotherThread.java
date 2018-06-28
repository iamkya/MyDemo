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

    private RandomRect confirmExpedition = new RandomRect(1000, 710, 200, 70, "confirmExpedition");
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

    private static final int maxCombatCount = 6;

    private RandomRect combatRect = new RandomRect(1254, 690, 260, 160, "combatRect");
    private RandomRect zeroTwoRect = new RandomRect(650, 540, 1230, 110, "zeroTwoRect");
    private RandomRect normalCombatRect = new RandomRect(1025, 830, 200, 90, "normalCombatRect");

    private RandomRect startPointLeftRect = new RandomRect(334,481,100, 100,"startPointLeftRect"); //airport
    private RandomRect startPointRightRect = new RandomRect(936,496,100,100,"startPointRightRect"); //command center

    private RandomRect groupFormationRect = new RandomRect(226,905,260,60,"groupFormationRect");

    private RandomRect selectGroup1AtDeploy = new RandomRect(5, 208, 160,90,"selectGroup1AtDeploy");
    private RandomRect selectGroup2AtDeploy = new RandomRect(5, 350,160,90,"selectGroup2AtDeploy");

    private RandomRect selectGroup1AtFormation = new RandomRect(5, 170,160,90,"selectGroup1AtFormation");
    private RandomRect selectGroup2AtFormation = new RandomRect(5, 300, 160, 90,"selectGroup2AtFormation");

    private RandomRect savedFormationAtGroupDetail = new RandomRect(1640, 940,280,80,"savedFormationAtGroupDetail");
    private RandomRect savedFormationRect = new RandomRect(1830,356, 80,120,"savedFormationRect");

    private RandomRect formation1 = new RandomRect(1100, 44, 760, 140,"formation1");
    private RandomRect formation2 = new RandomRect(1100, 233, 760, 140,"formation2");

    private RandomRect applyFormationButton = new RandomRect(1530,890,334, 123,"applyFormationButton");

    private RandomRect forceApplyDialogButton = new RandomRect(1113,706,233,80,"forceApplyDialogButton");

    private RandomRect formationConfirmButton = new RandomRect(1630, 888, 223, 142,"formationConfirmButton");

    private RandomRect formationBackButton = new RandomRect(10, 10, 185 ,125,"formationBackButton");

    private RandomRect deployConfirmButton = new RandomRect(1654,937,223,82,"deployConfirmButton");

    private RandomRect startBattleButton = new RandomRect(1510, 916, 370, 140,"startBattleButton");

    private RandomRect supplyButton = new RandomRect(1630,800,280,80,"supplyButton");

    private RandomRect planModeButton = new RandomRect(5,865,200,40,"planModeButton");

    private RandomRect enemy1 = new RandomRect(696,887,105,105,"enemy1");
    private RandomRect enemy2 = new RandomRect(742,575,105,105,"enemy2");
    private RandomRect empty1 = new RandomRect(956,261,105,105,"empty1");
    private RandomRect enemy3 = new RandomRect(743,156,80,56,"enemy3");

    private RandomRect round2StartPoint = new RandomRect(735,340,105,105,"round2StartPoint");

    private RandomRect enemy4 = new RandomRect(1163,335,105,105,"enemy4");
    private RandomRect enemy5 = new RandomRect(1442,394,105,105,"enemy5");

    private RandomRect endRound = new RandomRect(1660,945,216,100,"endRound");//结束回合

    private RandomRect execPlanButton = new RandomRect(1676,938,220,106,"execPlanButton");

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
        fight1.add(formationConfirmButton);

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
        fight2.add(formationConfirmButton);
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

        click3(startBattleButton);

        click1(startPointLeftRect);
        click1(startPointLeftRect);
        click2(supplyButton);

//        if(index == 0){
//            click1(startPointRightRect);
//            click1(startPointRightRect);
//            click2(supplyButton);
//        }

        click2(startPointRightRect);//reset selected group...

        click2(planModeButton);

        //click2(startPointRightRect);//机场

        exec("input swipe 1338 208 1338 908");

        sleep(2000);

        click2(enemy1);



        sleep(2000);

        click3(enemy2);

        click3(empty1);
        click3(enemy3);

        click3(execPlanButton);

        sleep(120 * 1000);


        click3(endRound);
        sleep(25*1000);

        click2(round2StartPoint);

        click3(planModeButton);

        click3(enemy4);
        click3(enemy5);
        click3(execPlanButton);

        sleep(80*1000);

        click2(endRound);

    }


    void performPrepareAndDeploy(ArrayList<RandomRect> rectArrayList, int fightGroup) throws Throwable{

        for(RandomRect rect : rectArrayList){

            click4(rect);
        }

        if(fightGroup == 0) {
            click4(selectGroup2AtFormation);
        }else{
            click4(selectGroup1AtFormation);
        }

        DebugUtil.e("drag leader");
        exec("input swipe 600 520 345 520");

        sleep(2000);

        click4(formationBackButton);

        if(fightGroup == 0) {

            click4(startPointRightRect);
            click4(deployConfirmButton);

            click4(startPointLeftRect);
            click4(deployConfirmButton);
        }else{

            click4(startPointLeftRect);
            click4(deployConfirmButton);

            click4(startPointRightRect);
            click4(deployConfirmButton);
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
        toast("NO click");
        return false;
    }

}
