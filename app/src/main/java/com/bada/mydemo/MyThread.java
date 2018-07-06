package com.bada.mydemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Debug;
import android.os.SystemClock;
import android.util.Log;

import com.bada.mydemo.dataType.ClickRect;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static org.opencv.imgproc.Imgproc.TM_CCOEFF_NORMED;

@SuppressWarnings("unchecked")
public class MyThread extends BaseThread {

    HashMap<String, ClickRect> rectMap;
    private static final String fileClickRectData = "clickRectData.dat";

    private void init() {
        rectMap = (HashMap<String, ClickRect>)SerializeObject.read(fileClickRectData);
        if(rectMap == null){
            rectMap = new HashMap<>();
        }
    }

    @Override
    public void run() {
        super.run();

        init();

        try {
            sleep(10 * 1000);
//            clickText("COMBAT", "combatRect", "/sdcard/bada/main1.jpg");
//
//            mySleep(3);
//
//            clickText("黑色情报", "0-2", "/sdcard/bada/2.png");
//
//            mySleep(4);
//
//            clickText("普通作战", "normalCombat", "/sdcard/bada/3.png");
//
//            mySleep(4);

//            findRound222();
//            findLine();
//            findRoundRed();

//            findLine();
//            findLine33();
//            findLineWhite();
//            findLine222();
//            clickText("指挥部", "command_center", "/sdcard/bada/4.png");

//            findLineWhite();
//            findLineWhiteTemplate();

//            findRoundBlue();
//            findRoundWhite();
//            findRoundRed();
            String zoomIn = "input tap 200 200 & PIDTAP=$!\n" +
                    "sleep 0.1\n" +
                    "input swipe 200 200 200 100 1000 & PIDSWIPE=$!\n" +
                    "wait $PIDTAP\n" +
                    "wait $PIDSWIPE\n";

            Intent broadcast = new Intent("com.bada.mydemo");
            broadcast.putExtra("command", zoomIn);
//            ContextModel.getInstance().getContext().sendBroadcast(broadcast);
//            exec("input tap 200 200 & PIDTAP=$!\n" +
//                    "sleep 0.1\n" +
//                    "input swipe 200 200 200 100 1000 & PIDSWIPE=$!\n" +
//                    "wait $PIDTAP\n" +
//                    "wait $PIDSWIPE\n");
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
    private void findRound() {

        try {

            Mat src = Imgcodecs.imread("/sdcard/bada/5.png");

            Mat gray = new Mat();
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

            Imgproc.medianBlur(gray, gray, 5);

            Mat circles = new Mat();
            Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                    200.0, // change this value to detect circles with different distances to each other
                    100.0, 30.0, 45, 90); // change the last two parameters
            // (min_radius & max_radius) to detect larger circles
            for (int x = 0; x < circles.cols(); x++) {
                double[] c = circles.get(0, x);
                Point center = new Point(Math.round(c[0]), Math.round(c[1]));
                // circle center
                Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );
                // circle outline
                int radius = (int) Math.round(c[2]);
                Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );
            }

            Imgcodecs.imwrite("/sdcard/test2.jpg", src);

            DebugUtil.e("1111");
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    private void findRound222() {

        try {

            Mat src = Imgcodecs.imread("/sdcard/bada/5.png");

            Mat gray = new Mat();
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

            Imgproc.medianBlur(gray, gray, 5);

            Mat circles = new Mat();
            Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                    200.0, // change this value to detect circles with different distances to each other
                    100.0, 45.0, 45, 90); // change the last two parameters
            // (min_radius & max_radius) to detect larger circles
            for (int x = 0; x < circles.cols(); x++) {
                double[] c = circles.get(0, x);
                Point center = new Point(Math.round(c[0]), Math.round(c[1]));
                // circle center
                Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );
                // circle outline
                int radius = (int) Math.round(c[2]);
                Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );
            }

            Imgcodecs.imwrite("/sdcard/test2.jpg", src);

            DebugUtil.e("1111");
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    private void findRoundRed() {

        try {

            Mat src = Imgcodecs.imread("/sdcard/bada/4.png");

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

            DebugUtil.e("circles = " + circles.cols());

            for (int x = 0; x < circles.cols(); x++) {
                double[] c = circles.get(0, x);
                Point center = new Point(Math.round(c[0]), Math.round(c[1]));
                // circle center
                Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );
                // circle outline
                int radius = (int) Math.round(c[2]);
                Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );
            }


            Imgcodecs.imwrite("/sdcard/red_hue_image.jpg", red_hue_image);
            Imgcodecs.imwrite("/sdcard/findRoundRed.jpg", src);

            DebugUtil.e("1111");
        }catch (Throwable e){
            e.printStackTrace();
        }
    }


    private void findLine(){
        // Declare the output variables
        Mat dst = new Mat(), cdst = new Mat(), cdstP;
        String default_file = "/sdcard/bada/5.png";

        // Load an image
        Mat src = Imgcodecs.imread(default_file, Imgcodecs.IMREAD_GRAYSCALE);
        // Check if image is loaded fine
        if( src.empty() ) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- default "
                    + default_file +"] \n");
            System.exit(-1);
        }
        // Edge detection
        Imgproc.Canny(src, dst, 50, 200, 3, false);
        // Copy edges to the images that will display the results in BGR
        Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);
        cdstP = cdst.clone();
        // Standard Hough Line Transform
        Mat lines = new Mat(); // will hold the results of the detection
        Imgproc.HoughLines(dst, lines, 1, Math.PI/180, 150); // runs the actual detection
        // Draw the lines
        for (int x = 0; x < lines.rows(); x++) {
            double rho = lines.get(x, 0)[0],
                    theta = lines.get(x, 0)[1];
            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a*rho, y0 = b*rho;
            Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
            Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
            Imgproc.line(cdst, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }
        // Probabilistic Line Transform
        Mat linesP = new Mat(); // will hold the results of the detection
        Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, 50, 50, 10); // runs the actual detection
        // Draw the lines
        for (int x = 0; x < linesP.rows(); x++) {
            double[] l = linesP.get(x, 0);
            Imgproc.line(cdstP, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }
        // Show results
        Imgcodecs.imwrite("/sdcard/Standard.jpg", cdst);
        Imgcodecs.imwrite("/sdcard/Probabilistic.jpg", cdstP);

        DebugUtil.e("findLine finish");
    }

    private void findLine222(){
        // Declare the output variables
        Mat dst = new Mat(), cdst = new Mat();
        String default_file = "/sdcard/bada/5.png";

        // Load an image
        Mat src = Imgcodecs.imread(default_file, Imgcodecs.IMREAD_GRAYSCALE);
        // Check if image is loaded fine
        if( src.empty() ) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- default "
                    + default_file +"] \n");
            System.exit(-1);
        }
        // Edge detection
       // Imgproc.Canny(src, dst, 50, 200, 3, false);
        // Copy edges to the images that will display the results in BGR
        Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);

        // Standard Hough Line Transform
        Mat lines = new Mat(); // will hold the results of the detection
        Imgproc.HoughLines(dst, lines, 1, Math.PI/180, 150); // runs the actual detection
        // Draw the lines
        for (int x = 0; x < lines.rows(); x++) {
            double rho = lines.get(x, 0)[0],
                    theta = lines.get(x, 0)[1];
            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a*rho, y0 = b*rho;
            Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
            Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
            Imgproc.line(cdst, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }

        // Show results
        Imgcodecs.imwrite("/sdcard/Standard.jpg", cdst);


        DebugUtil.e("findLine finish");
    }

    public static double distance(Point a, Point b){

        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    void findLineWhite(){

        try {

            File file = new File("/sdcard/findLine33.jpg");
            if(file.exists()){
                boolean b = file.delete();
                if(b)
                    DebugUtil.e("/sdcard/findLine33.jpg deleteded");
            }

            Mat src = Imgcodecs.imread("/sdcard/bada/5.png");

            Mat converted = new Mat();
            Imgproc.cvtColor(src, converted, Imgproc.COLOR_BGR2HSV); //COLOR_RGB2HSV

            Mat white = new Mat();
//            Core.inRange(converted, new Scalar(0, 0, 200), new Scalar(255, 255, 255), white);
            Core.inRange(converted, new Scalar(0,0,0), new Scalar(0,0,255), white);

            Imgproc.GaussianBlur(white, white, new Size(9, 9), 2, 2);

            Mat lines = new Mat();
            Imgproc.HoughLinesP(white, lines, 1, Math.PI/180, 30, 10, 20);
            for (int x = 0; x < lines.rows(); x++){

                double[] l = lines.get(x, 0);
                Point a = new Point(l[0], l[1]);
                Point b = new Point(l[2], l[3]);

                if(distance(a, b) > 20){
                    continue;
                }

                Imgproc.line(src, a, b, new Scalar(0, 0, 255), 1, Imgproc.LINE_AA, 0);
            }

//            Imgproc.HoughLines(white, lines, 1, Math.PI/180, 150, 10, 30, 30, 50); // runs the actual detection
//            // Draw the lines
//            for (int x = 0; x < lines.rows(); x++) {
//                double rho = lines.get(x, 0)[0],
//                        theta = lines.get(x, 0)[1];
//                double a = Math.cos(theta), b = Math.sin(theta);
//                double x0 = a*rho, y0 = b*rho;
//                Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
//                Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
//                Imgproc.line(src, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
//            }

            Imgcodecs.imwrite("/sdcard/findLine33.jpg", src);
            Imgcodecs.imwrite("/sdcard/white.jpg", white);

            DebugUtil.e("1111");
        }catch (Throwable e){
            e.printStackTrace();
        }


    }

    void findLineWhiteTemplate(){

        try {

            File file = new File("/sdcard/findLine33.jpg");
            if(file.exists()){
                boolean b = file.delete();
                if(b)
                    DebugUtil.e("/sdcard/findLine33.jpg deleteded");
            }

            Mat src = Imgcodecs.imread("/sdcard/bada/5.png");

            Mat converted = new Mat();
            Imgproc.cvtColor(src, converted, Imgproc.COLOR_BGR2HSV); //COLOR_RGB2HSV

//            Mat sample = new Mat();
//
//            Bitmap bitmap = Bitmap.createBitmap(8, 12, ARGB_8888);
//            bitmap.eraseColor(ContextModel.getInstance().getContext().getResources().getColor(R.color.white));
//
//            Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//            Utils.bitmapToMat(bmp32, sample);


            Mat white = new Mat();
//            Core.inRange(converted, new Scalar(0, 0, 200), new Scalar(255, 255, 255), white);
            Core.inRange(converted, new Scalar(0,0,0), new Scalar(0,0,255), white);

            Imgproc.GaussianBlur(white, white, new Size(9, 9), 2, 2);

            Mat lines = new Mat();

//            Imgproc.matchTemplate(white, sample, lines, TM_CCOEFF_NORMED);

            Imgproc.HoughLinesP(white, lines, 1, Math.PI/180, 30, 10, 20);
            for (int x = 0; x < lines.rows(); x++){

                double[] l = lines.get(x, 0);
                Point a = new Point(l[0], l[1]);
                Point b = new Point(l[2], l[3]);

                double distance = distance(a, b);
                if(distance > 20){
                    continue;
                }

//                DebugUtil.e("distance =" + String.valueOf(distance));
                Imgproc.line(src, a, b, new Scalar(0, 0, 255), 1, Imgproc.LINE_AA, 0);
            }
//            Imgproc.HoughLines(white, lines, 1, Math.PI/180, 150, 10, 30, 30, 50); // runs the actual detection
//            // Draw the lines
//            for (int x = 0; x < lines.rows(); x++) {
//                double rho = lines.get(x, 0)[0],
//                        theta = lines.get(x, 0)[1];
//                double a = Math.cos(theta), b = Math.sin(theta);
//                double x0 = a*rho, y0 = b*rho;
//                Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
//                Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
//                Imgproc.line(src, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
//            }

            Imgcodecs.imwrite("/sdcard/findLine33.jpg", src);
            Imgcodecs.imwrite("/sdcard/white.jpg", white);

            DebugUtil.e("1111");
        }catch (Throwable e){
            e.printStackTrace();
        }


    }


    void findRoundWhite() {

        try {

            Mat src = Imgcodecs.imread("/sdcard/bada/4.png");

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

            DebugUtil.e("circles " + circles.cols());

            for (int x = 0; x < circles.cols(); x++) {
                double[] c = circles.get(0, x);
                Point center = new Point(Math.round(c[0]), Math.round(c[1]));
                // circle center
                Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );
                // circle outline
                int radius = (int) Math.round(c[2]);
                DebugUtil.e(" radius = " + radius);
                Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );
            }

            Imgcodecs.imwrite("/sdcard/white.jpg", white);
            Imgcodecs.imwrite("/sdcard/test3.jpg", src);

            DebugUtil.e("1111");
        }catch (Throwable e){
            e.printStackTrace();
        }

    }

    void findRoundBlue(){
        try {

            Mat src = Imgcodecs.imread("/sdcard/bada/4.png");

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

            DebugUtil.e("circles " + circles.cols());

            for (int x = 0; x < circles.cols(); x++) {
                double[] c = circles.get(0, x);
                Point center = new Point(Math.round(c[0]), Math.round(c[1]));
                // circle center
                Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );
                // circle outline
                int radius = (int) Math.round(c[2]);
                Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );
            }

            Imgcodecs.imwrite("/sdcard/blue.jpg", blue);
            Imgcodecs.imwrite("/sdcard/test3.jpg", src);

            DebugUtil.e("1111");
        }catch (Throwable e){
            e.printStackTrace();
        }

    }

    void findLine33(){

        Mat src = Imgcodecs.imread("/sdcard/bada/5.png");
        Mat gray = new Mat();

        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

        Mat lines = new Mat();

        Imgproc.HoughLinesP(gray, lines, 1, Math.PI/180, 150);
        for (int x = 0; x < lines.rows(); x++){

            double[] l = lines.get(x, 0);
            Imgproc.line(src, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }

        Imgcodecs.imwrite("/sdcard/findLine33.jpg", src);

        DebugUtil.e("findLine33 ");
    }

    private final Object waitLock = new Object();

    void clickText(final String text, final String tag, String filePath) throws Throwable{

        ClickRect clickRect = rectMap.get(tag);
        if(clickRect == null) {
            clickRect = getRectViaEngine(text, tag, filePath);
        }

        if(clickRect == null){
            DebugUtil.e("ClickRect == null, returning");
            System.exit(0);
            return;
        }

        DebugUtil.e(clickRect.getClickPoint());
//        click2(clickRect);
    }


    ClickRect getRectViaEngine(final String text, final String tag, final String filePath){

        final ClickRect[] resultRect = {null};

        synchronized (waitLock){

        OCRUtil.getInstance().getRect(text, new OCRUtil.RectCB() {
            @Override
            public void onGetRect(List<Rect> rectList) {

                DebugUtil.e("found " + text + " size = " + rectList.size());
                if(rectList.size() == 0){

                    return;
                }

                Rect rect = rectList.get(0);

                resultRect[0] = new ClickRect(rect.left, rect.top, rect.width(), rect.height(), tag);
                resultRect[0].setButtonText(text);

                rectMap.put(tag, resultRect[0]);

                synchronized (waitLock){
                    try {
                        waitLock.notify();
                    }catch (Throwable e){
                        e.printStackTrace();
                    }
                }
            }
        }, filePath);

            try {
                waitLock.wait();
            }catch (Throwable e){
                e.printStackTrace();
            }
        }
        return resultRect[0];
    }

//    static CompletableFuture<ClickRect> getRect(String text, String tag){
//        HashMap<String, ClickRect> rectMap = new HashMap<>();
//
//        ClickRect rect = rectMap.get(tag);
//        if(rect != null)
//            return CompletableFuture.completedFuture(rect);
//
//        return completedFuture(null);
//    }
}
