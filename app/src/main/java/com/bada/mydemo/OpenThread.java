package com.bada.mydemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bada.mydemo.dataType.ClickRect;
import com.bada.mydemo.dataType.RandomRect;
import com.baidu.ocr.sdk.model.Word;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class OpenThread extends BaseThread {

    private static final String screen_cap_file =  "/sdcard/colorPickerTemp.png";

    HashMap<String, ClickRect> clickRectMap;
    private static final String fileClickRectData = "OpenThreadClickRectData.dat";

    private RandomRect confirmExpedition = new RandomRect(1010, 720, 180, 50, "confirmExpedition");

    private void init() {
        clickRectMap = (HashMap<String, ClickRect>)SerializeObject.read(fileClickRectData);
        if(clickRectMap == null){
            clickRectMap = new HashMap<>();
        }
    }

    @Override
    public void run() {
        super.run();

        try {
            init();

            sleep(15 * 1000);

            if(true){
                testFunc();
                return;
            }

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

    private static final int maxCombatCount = 4;

    boolean startCombat() throws Throwable{

        for (int i = 0; i < maxCombatCount ; i ++) {

            doCombat(i);

            checkExpedition(false);
        }

        return true;
    }

    private void doCombat(int index) throws Throwable {

        clickText("COMBAT", "combatRect");

        clickText("黑色情报", "0-2");

        clickText("普通作战", "normalCombat");

        doBattle(index);
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
    }

    ArrayList<ClickRect> deployGroupRects = null;
    ArrayList<ClickRect> formationGroupRects = null;
    ArrayList<ClickRect> formations = null;

    boolean shouldDragLeader = false;

    final String strConfirm = "确认";
    final String strStartBattle = "开始作战";
    final String strSupply = "补给";
    final String strPlanMode = "计划模式";
    final String strExecPlan = "执行计划";
    final String strEndRound = "结束回合";

    void doBattle(int index){

        ArrayList<ClickRect> blues = getColorRect(RectColor.blue);

        ClickRect right = findMostRight(blues);

        ClickRect left = findMostLeft(blues);

        int fightGroup = index % 2;

        click4(right);

        clickText("队伍编成", "groupFormation");

        //////
        if(index == 0){
            screenCap();
            formationGroupRects = findSeveralRects(screen_cap_path, SeveralRectType.groupNumber);

            formationGroupRects.get(0).setTag("formationGroupRect1");

            formationGroupRects.get(1).setTag("formationGroupRect2");
        }

        if(fightGroup == 0){

            //click4(deployGroupRects.get(0));
        }else {

            click4(formationGroupRects.get(1));
        }

        ///////

        clickText("阵型预设", "savedFormationAtGroupDetail");

        clickText("预设", "savedFormationRect");

        if(index == 0){
            screenCap();
            formations = findSeveralRects(screen_cap_path, SeveralRectType.savedFormation);
            formations.get(0).setTag("savedFormation1");

            formations.get(1).setTag("savedFormation2");
        }

        click4(formations.get(fightGroup));

        clickText("套用预设", "applyFormationButton");

        clickText("确认", "forceApplyDialogButton");

        if(shouldDragLeader){

        }

        clickText("返回", "formationBack");

        if(fightGroup == 0) {

            click4(right);
            clickText(strConfirm, "deployConfirmButton");

            click4(left);
            clickText(strConfirm, "deployConfirmButton");
        }else{

            click4(left);
            clickText(strConfirm, "deployConfirmButton");

            click4(right);
            clickText(strConfirm, "deployConfirmButton");
        }

        //deployment finish, start the battle

        clickText(strStartBattle, "startBattleButton");

        //fill supply first
        click1(left);
        click1(left);
        clickText(strSupply, "supplyButton");

        //select right
        click4(right);

        clickText(strPlanMode, "planModeButton");

        //self
        click2(right);

        ArrayList<ClickRect> reds = getColorRect(RectColor.red);

        ClickRect enemy1 = findClosest(right, reds, Direction.TOP_LEFT);
        enemy1.setTag("enemy1");
        click2(enemy1);

        Random random = new Random();
        int swipeX = random.nextInt(100) + 1300;
        int swipeYStart = random.nextInt(50) + 200;
        int swipeYHeight = random.nextInt(100) + 600;

        String cmd = "input swipe " + swipeX + " " +  swipeYStart +  " " + swipeX + 908 + " " + swipeYHeight;
        exec(cmd);

        mySleep(4);

        ArrayList<ClickRect> whites = getColorRect(RectColor.white);
        ClickRect white = findMostLeft(whites);
        white.setTag("white");

        reds = getColorRect(RectColor.red);

        ClickRect enemy2 = findClosest(white, reds, Direction.BOTTOM_LEFT);
        enemy2.setTag("enemy2");

        ClickRect enemy3 = findClosest(white, reds, Direction.TOP_LEFT);
        enemy3.setTag("enemy3");

        click2(enemy2);

        click2(white);

        click2(enemy3);

        clickText(strExecPlan, "execPlanButton");

        mySleep(130, "round 1 fighting");


        clickText(strEndRound, "endRound");

        mySleep(25, "round 1 enemy turn");

        blues = getColorRect(RectColor.blue);

        //222222
        ClickRect round2Start = findMostLeft(blues);
        round2Start.setTag("round2Start");
        click2(round2Start);

        clickText(strPlanMode, "planModeButton");

        reds = getColorRect(RectColor.red);

        ClickRect enemy4 = findClosest(round2Start, reds, Direction.TOP_RIGHT);
        enemy4.setTag("enemy4");

        ClickRect enemy5 = findClosest(enemy4, reds, Direction.BOTTOM_RIGHT);
        enemy5.setTag("enemy5");

        click2(enemy4);

        click2(enemy5);

        clickText(strExecPlan, "execPlanButton");

        mySleep(80, "round 2 fighting");

        clickText(strEndRound, "endRound");
    }

    enum Direction{
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    ClickRect findClosest(ClickRect base, ArrayList<ClickRect> list, Direction direction){

        double smallestDistance = 0;
        ClickRect closestRect = null;

        for (ClickRect c : list){

            if(c.getCenter().equals(base.getCenter())){
                DebugUtil.e("findClosest getting same point " + c.getCenter().toString());
                continue;
            }

            double atan2 = Math.atan2(c.getCenter().y - base.getCenter().y, c.getCenter().x - base.getCenter().x);
            if(isSameDirection(atan2, direction)){
                double distance = distance(base.getCenter(), c.getCenter());

                if(smallestDistance == 0 || distance < smallestDistance){
                    smallestDistance = distance;
                    closestRect = c;
                }
            }
        }

        return closestRect;
    }

    boolean isSameDirection(double atan2, Direction direction){
        switch (direction){
            case TOP_LEFT:
                return atan2 < -Math.PI/2 && atan2 > -Math.PI ;
            case TOP_RIGHT:
                return atan2 < 0 && atan2 > -Math.PI/2;
            case BOTTOM_LEFT:
                return atan2 > Math.PI/2 && atan2 < Math.PI;
            case BOTTOM_RIGHT:
                return atan2 < Math.PI/2 && atan2 > 0;
        }

        return false;
    }

    public static double distance(Point a, Point b){

        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    ClickRect findSavedFormationRect() {

        screenCap();

        return null;
    }

    void testFunc(){

        findSeveralRects("/sdcard/bada/10.png", SeveralRectType.groupNumber);
    }

    ArrayList<ClickRect> wordToGroupClickRect(ArrayList<Word> list, SeveralRectType type){

        if(type == SeveralRectType.groupNumber){

            ArrayList<ClickRect> list1 = new ArrayList<>();
            ArrayList<ClickRect> list2 = new ArrayList<>();
            for(Word word: list){
                if(word.getLocation().getLeft() < 40){

                    if(word.getWords().contains("梯队")){
                        list1.add(new ClickRect(word));
                    }else if(word.getWords().contains("ECHELON")){
                        list2.add(new ClickRect(word));
                    }
                }
            }

            if(list1.size() > list2.size()){
                return list1;
            }else
                return list2;
        }else if(type == SeveralRectType.savedFormation){

            ArrayList<ClickRect> list1 = new ArrayList<>();
            for(Word word: list){
                if(word.getLocation().getTop() > screenHeight/2){

                    if(word.getWords().contains("预设队伍")){
                        list1.add(new ClickRect(word));
                    }
                }
            }

            return list1;
        }

        return null;
    }


    enum SeveralRectType{
        groupNumber,
        savedFormation
    }

    ArrayList<ClickRect> findSeveralRects(String filePath, SeveralRectType type){

        DebugUtil.e("findGroupNumberRect");

        ArrayList<Word> words = getRectsViaEngine(filePath);

        ArrayList<ClickRect> clickRects1 = wordToGroupClickRect(words, type);

        Collections.sort(clickRects1, new Comparator<ClickRect>() {
            @Override
            public int compare(ClickRect o1, ClickRect o2) {

                return o1.getTopY() - o2.getTopY();
            }
        });

        return clickRects1;
    }

    ClickRect findMostLeft(ArrayList<ClickRect> rects){
        ClickRect left = null;
        for(ClickRect rect: rects){
            if(left == null || left.getCenter().x < rect.getCenter().x){
                left = rect;
            }
        }

        return left;
    }

    ClickRect findMostRight(ArrayList<ClickRect> rects){
        ClickRect right = null;
        for(ClickRect rect: rects){
            if(right == null || right.getCenter().x > rect.getCenter().x){
                right = rect;
            }
        }

        return right;
    }

    enum RectColor{
        white,
        blue,
        red
    }

    ArrayList<ClickRect> getColorRect(RectColor rectColor){

        capture();

        switch (rectColor){
            case red: {
                return findRoundRed();
            }
            case blue: {
                return findRoundBlue();
            }
            case white: {
                return findRoundWhite();
            }
            default:
                return null;
        }
    }

    ArrayList<ClickRect> findRoundBlue(){
        try {

            Mat src = Imgcodecs.imread(screen_cap_path);

            Mat gray = new Mat();
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2HSV);

//            Mat lower_red_hue_range = new Mat();
//            Core.inRange(gray, new Scalar(0, 100, 100), new Scalar(10, 255, 255), lower_red_hue_range);
//
//            Mat upper_red_hue_range = new Mat();
//            Core.inRange(gray, new Scalar(160, 100, 100), new Scalar(179, 255, 255), upper_red_hue_range);
//
//            Mat red_hue_image = new Mat();
//            Core.addWeighted(lower_red_hue_range, 1.0, upper_red_hue_range, 1.0, 0.0, red_hue_image);
//
            Mat blue = new Mat();
//            Core.inRange(gray, new Scalar(105,135,68), new Scalar(120,220,180), blue);
            Core.inRange(gray, new Scalar(100, 100, 100), new Scalar(120, 255, 255), blue);
//
//            ColorDetection.detectSingleBlob(mYuv, mColor, "B", mResult);
//            Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);

//            Mat blue = new Mat();
//            Core.bitwise_and(gray, gray, blue, mask);
            //Imgproc.GaussianBlur(blue, blue, new Size(9, 9), 2, 2);
//            Core.bitwise_and(gray, gray, blue);

            Mat circles = new Mat();
            Imgproc.HoughCircles(blue, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                    200,
                    500, 20, 40, 150);

            DebugUtil.e("findRoundBlue circles " + circles.cols());

            ArrayList<ClickRect> clickRectArrayList = new ArrayList<>();

            for (int x = 0; x < circles.cols(); x++) {
                double[] c = circles.get(0, x);
                Point center = new Point(Math.round(c[0]), Math.round(c[1]));
                // circle center
//                Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );
                // circle outline
                int radius = (int) Math.round(c[2]);
//                Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );

                ClickRect clickRect = getClickRectFromRound(center, radius);

                clickRectArrayList.add(clickRect);
            }

//            Imgcodecs.imwrite("/sdcard/blue.jpg", blue);
//            Imgcodecs.imwrite("/sdcard/test3.jpg", src);

            DebugUtil.e("findRoundBlue end");

            return clickRectArrayList;
        }catch (Throwable e){
            e.printStackTrace();
        }

        return null;
    }

    ArrayList<ClickRect> findRoundRed() {

        try {

            Mat src = Imgcodecs.imread(screen_cap_file);

            Mat gray = new Mat();
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2HSV);

            Mat lower_red_hue_range = new Mat();
            Core.inRange(gray, new Scalar(0, 100, 100), new Scalar(10, 255, 255), lower_red_hue_range);

            Mat upper_red_hue_range = new Mat();
            Core.inRange(gray, new Scalar(160, 100, 100), new Scalar(179, 255, 255), upper_red_hue_range);

            Mat red_hue_image = new Mat();
            Core.addWeighted(lower_red_hue_range, 1.0, upper_red_hue_range, 1.0, 0.0, red_hue_image);

            Imgproc.GaussianBlur(red_hue_image, red_hue_image, new Size(9, 9), 2, 2);

            Mat circles = new Mat();
            Imgproc.HoughCircles(red_hue_image, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                    200,
                    500, 20, 40, 120);

            DebugUtil.e("findRoundRed circles = " + circles.cols());

            ArrayList<ClickRect> clickRectArrayList = new ArrayList<>();

            for (int x = 0; x < circles.cols(); x++) {
                double[] c = circles.get(0, x);
                Point center = new Point(Math.round(c[0]), Math.round(c[1]));
                // circle center
//                Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );
                // circle outline
                int radius = (int) Math.round(c[2]);
//                Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );

                ClickRect clickRect = getClickRectFromRound(center, radius);

                clickRectArrayList.add(clickRect);
            }


//            Imgcodecs.imwrite("/sdcard/red_hue_image.jpg", red_hue_image);
//            Imgcodecs.imwrite("/sdcard/findRoundRed.jpg", src);

            DebugUtil.e("findRoundRed end");

            return clickRectArrayList;
        }catch (Throwable e){
            e.printStackTrace();
        }

        return null;
    }

    ArrayList<ClickRect> findRoundWhite() {

        try {

            Mat src = Imgcodecs.imread(screen_cap_file);

            Mat gray = new Mat();
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2HSV);

//            Mat lower_red_hue_range = new Mat();
//            Core.inRange(gray, new Scalar(0, 100, 100), new Scalar(10, 255, 255), lower_red_hue_range);
//
//            Mat upper_red_hue_range = new Mat();
//            Core.inRange(gray, new Scalar(160, 100, 100), new Scalar(179, 255, 255), upper_red_hue_range);
//
//            Mat red_hue_image = new Mat();
//            Core.addWeighted(lower_red_hue_range, 1.0, upper_red_hue_range, 1.0, 0.0, red_hue_image);
//
            Mat white = new Mat();
            Core.inRange(gray, new Scalar(0,0,0), new Scalar(0,0,255), white);

            Imgproc.GaussianBlur(white, white, new Size(9, 9), 2, 2);

            Mat circles = new Mat();
            Imgproc.HoughCircles(white, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                    200,
                    550, 20, 40, 50);

            DebugUtil.e("findRoundWhite circles " + circles.cols());

            ArrayList<ClickRect> clickRectArrayList = new ArrayList<>();
            for (int x = 0; x < circles.cols(); x++) {
                double[] c = circles.get(0, x);
                Point center = new Point(Math.round(c[0]), Math.round(c[1]));
                // circle center
//                Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );
                // circle outline
                int radius = (int) Math.round(c[2]);
                DebugUtil.e(" radius = " + radius);
//                Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );

                ClickRect clickRect = getClickRectFromRound(center, radius);

                clickRectArrayList.add(clickRect);
            }

//            Imgcodecs.imwrite("/sdcard/white.jpg", white);
//            Imgcodecs.imwrite("/sdcard/test3.jpg", src);

            DebugUtil.e("findRoundWhite end");

            return clickRectArrayList;
        }catch (Throwable e){
            e.printStackTrace();
        }

        return null;
    }


    ClickRect getClickRectFromRound(Point center, int radius){

        int a = (int)Math.round( Math.sqrt( radius * radius / 2));

        int x = (int) center.x - a;
        int y = (int) center.y - a;
        int width = a * 2;
        int height = a * 2;

        ClickRect clickRect = new ClickRect(x, y, width, height);
        clickRect.setCenter(center);
        clickRect.setRadius(radius);

        return clickRect;
    }



    private final Object waitLock = new Object();

    void clickText(final String text, final String tag){

        capture();

        ClickRect clickRect = clickRectMap.get(tag);
        if(clickRect == null) {
            clickRect = getRectViaEngine(text, tag, screen_cap_path);
        }

        if(clickRect == null){
            DebugUtil.e("ClickRect == null, returning");
            System.exit(0);
            return;
        }

        click4(clickRect);
    }

    ArrayList<Word> getRectsViaEngine(final String filePath){

        final ArrayList<Word> resultRect = new ArrayList<>();

        synchronized (waitLock){

            OCRUtil.getInstance().getRect(new OCRUtil.RectCB() {
                @Override
                public void onGetRect(List<Word> rectList) {

                    if(rectList.size() == 0){

                        return;
                    }

//                    for (Word word : rectList){
//                        if(word.getWords().equals(text)){
//                            int left = word.getLocation().getLeft();
//                            int top = word.getLocation().getTop();
//                            int width = word.getLocation().getWidth();
//                            int height = word.getLocation().getHeight();
//
//                            resultRect.add(new ClickRect(left, top, width, height));
//                        }
//                    }

                    resultRect.addAll(rectList);

                    synchronized (waitLock){
                        try {
                            waitLock.notify();
                        }catch (Throwable e){
                            e.printStackTrace();
                        }
                    }
                }
            }, filePath, false);

            try {
                waitLock.wait();
            }catch (Throwable e){
                e.printStackTrace();
            }
        }
        return resultRect;
    }


    ClickRect getRectViaEngine(final String text, final String tag, final String filePath){

        final ArrayList<ClickRect> resultRect = new ArrayList<>();

        synchronized (waitLock){

            OCRUtil.getInstance().getRect(new OCRUtil.RectCB() {
                @Override
                public void onGetRect(List<Word> rectList) {

                    DebugUtil.e("found " + text + " size = " + rectList.size());
                    if(rectList.size() == 0){

                        return;
                    }

                    for (Word word : rectList){
                        if(word.getWords().equals(text)){
                            int left = word.getLocation().getLeft();
                            int top = word.getLocation().getTop();
                            int width = word.getLocation().getWidth();
                            int height = word.getLocation().getHeight();

                            ClickRect clickRect = new ClickRect(left, top, width, height);
                            clickRect.setButtonText(text);

                            resultRect.add(clickRect);
                        }
                    }


                    clickRectMap.put(tag, resultRect.get(0));

                    synchronized (waitLock){
                        try {
                            waitLock.notify();
                        }catch (Throwable e){
                            e.printStackTrace();
                        }
                    }
                }
            }, filePath, true);

            try {
                waitLock.wait();
            }catch (Throwable e){
                e.printStackTrace();
            }
        }
        return resultRect.get(0);
    }
}
