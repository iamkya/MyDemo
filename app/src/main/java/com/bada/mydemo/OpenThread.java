package com.bada.mydemo;

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
import java.util.List;

public class OpenThread extends Thread {

    Process shell = null;
    DataOutputStream os = null;
    InputStream is = null;

    private static final String screen_group1 = "/sdcard/bada/group1.png";

    @Override
    public void run() {

        try {

            shell = Runtime.getRuntime().exec("su", null,null);
            os = new DataOutputStream(shell.getOutputStream());
            is = shell.getInputStream();

            while (true) {

                Mat Main = Imgcodecs.imread(screen_group1);
                Mat rgb = new Mat();

                Imgproc.pyrDown(Main, rgb);

                Mat small = new Mat();

                Imgproc.cvtColor(rgb, small, Imgproc.COLOR_RGB2GRAY);

                Mat grad = new Mat();

                Mat morphKernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3, 3));

                Imgproc.morphologyEx(small, grad, Imgproc.MORPH_GRADIENT , morphKernel);

                Mat bw = new Mat();

                Imgproc.threshold(grad, bw, 0.0, 255.0, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

                Mat connected = new Mat();

                morphKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(9,1));

                Imgproc.morphologyEx(bw, connected, Imgproc.MORPH_CLOSE  , morphKernel);


                Mat mask = Mat.zeros(bw.size(), CvType.CV_8UC1);

                List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

                Mat hierarchy = new Mat();

                Imgproc.findContours(connected, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

                for(int idx = 0; idx < contours.size(); idx++)
                {
                    Rect rect = Imgproc.boundingRect(contours.get(idx));

                    Mat maskROI = new Mat(mask, rect);
                    maskROI.setTo(new Scalar(0, 0, 0));

                    Imgproc.drawContours(mask, contours, idx, new Scalar(255, 255, 255), Core.FILLED);

                    double r = (double)Core.countNonZero(maskROI)/(rect.width*rect.height);

                    if (r > .45 && (rect.height > 8 && rect.width > 8))
                    {
                        Imgproc.rectangle(rgb, rect.br() , new Point( rect.br().x-rect.width ,rect.br().y-rect.height),  new Scalar(0, 255, 0));
                    }

                    String outputfile = "/sdcard/trovato.png";
                    Imgcodecs.imwrite(outputfile,rgb);

                }

                DebugUtil.e("111222333444");

                break;
            }
        }catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
