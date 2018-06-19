package com.bada.mydemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;

import com.bada.mydemo.rect.FocusRect;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class BadaThread extends Thread {

    Process shell = null;

    private static final String screen_cap_path = "/sdcard/colorPickerTemp.png";
    //private static final String screen_test_path = "/sdcard/bada/main2.jpg";

    @Override
    public void run() {

        try {
            shell = Runtime.getRuntime().exec("su", null,null);
            os = new DataOutputStream(shell.getOutputStream());
            is = shell.getInputStream();

            while (true) {

                Thread.sleep(15 * 1000 );

                capture();
                findStr();

            }

        }catch (Throwable e){
            e.printStackTrace();
        }


    }

    static final FocusRect friendInfo = new FocusRect(0, 0, 1000, 200);
    DataOutputStream os = null;
    InputStream is = null;

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


    boolean findStr() {
        Bitmap screen = BitmapFactory.decodeFile(screen_cap_path);

        Rect rect = new Rect(1300, 0, 1920, 75 + 130);

        Bitmap part = getPart(screen, rect);

        Bitmap grey = convertGray(part);
//        Bitmap grey = part;

        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "part.jpg");
            FileOutputStream fo = new FileOutputStream(file);

            grey.compress(Bitmap.CompressFormat.JPEG, 100, fo);
            fo.flush();
            fo.close();

        }catch (Throwable e) {
            e.printStackTrace();
        }

        OCRManager.getInstance().doOcr(grey, null);

        part.recycle();
        screen.recycle();

        return true;
    }
    //new Rect(1237, 70 , 1709, 150)
    private Bitmap getPart(Bitmap screen, Rect rect) {

        return Bitmap.createBitmap(screen, rect.left, rect.top, rect.width(), rect.height());
    }
}
