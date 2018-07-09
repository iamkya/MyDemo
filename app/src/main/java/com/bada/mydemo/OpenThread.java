package com.bada.mydemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bada.mydemo.dataType.ClickRect;
import com.bada.mydemo.dataType.RandomRect;
import com.baidu.ocr.sdk.model.Word;

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
import java.util.Collections;
import java.util.Comparator;
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

    private void doCombat(int index) throws Throwable {

        clickText("COMBAT", "combatRect");

        clickText("黑色情报", "0-2");

        clickText("普通作战", "normalCombat");

        prepareGroup(index);
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
        //then battle should start
//        battleBegan(index);
    }

    void prepareGroup(int index){

        ArrayList<ClickRect> blues = getColorRect(RectColor.blue);

        ClickRect right = findMostRight(blues);

        ClickRect left = findMostLeft(blues);

        int fightGroup = index % 2;

        click4(right);

        clickText("队伍编成", "groupFormation");

        ArrayList<ClickRect> deploy = findGroupNumberRect(index == 0, "deploy");

        ArrayList<ClickRect> formation = findGroupNumberRect(index == 0, "formation");
    }

    void testFunc(){

        findGroupNumberRect(true, null);
    }

    ArrayList<ClickRect> findGroupNumberRect(boolean shouldCapture, String tag){

        DebugUtil.e("findGroupNumberRect");

        if(shouldCapture){
            //capture();

            ArrayList<ClickRect> clickRects1 = getRectsViaEngine("梯队", null, "/sdcard/bada/10.png");

            for (ClickRect rect: clickRects1){

            }

            Collections.sort(clickRects1, new Comparator<ClickRect>() {
                @Override
                public int compare(ClickRect o1, ClickRect o2) {

                    return o1.getTopY() - o2.getTopY();
                }
            });

            ArrayList<ClickRect> clickRects2 = getRectsViaEngine("ECHELON", null, "/sdcard/bada/10.png");
            Collections.sort(clickRects2, new Comparator<ClickRect>() {
                @Override
                public int compare(ClickRect o1, ClickRect o2) {

                    return o1.getTopY() - o2.getTopY();
                }
            });

            if(clickRects1.size() > clickRects2.size()){
                return clickRects1;
            }else
                return clickRects2;

        }

        return null;
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
                clickRect.setCenter(center);

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
                clickRect.setCenter(center);

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
                clickRect.setCenter(center);

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

        return clickRect;
    }



    private final Object waitLock = new Object();

    void clickText(final String text, final String tag){

        capture();

        ClickRect clickRect = rectMap.get(tag);
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

    ArrayList<ClickRect> getRectsViaEngine(final String text, final String tag, final String filePath){

        final ArrayList<ClickRect> resultRect = new ArrayList<>();

        synchronized (waitLock){

            OCRUtil.getInstance().getRect(text, new OCRUtil.RectCB() {
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

                            resultRect.add(new ClickRect(left, top, width, height));
                        }
                    }

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

            OCRUtil.getInstance().getRect(text, new OCRUtil.RectCB() {
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

                            resultRect.add(new ClickRect(left, top, width, height));
                        }
                    }


                    rectMap.put(tag, resultRect.get(0));

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
