package com.bada.mydemo;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;

import com.googlecode.tesseract.android.TessBaseAPI;

public class OCRManager {
    private static OCRManager instance = null;
    public static synchronized OCRManager getInstance() {
        if(instance == null) {
            instance = new OCRManager();
        }
        return instance;
    }

    public static final String TESSBASE_PATH = Environment.getExternalStorageDirectory() + "/" + "tessdata";

    TessBaseAPI baseApi = null;
    public void init() {
        baseApi = new TessBaseAPI();
        baseApi.init(TESSBASE_PATH, "chi_sim");

        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
    }

    public String doOcr(Bitmap bitmap) {

        try {
            if(bitmap == null)
                return null;

            baseApi.setImage(bitmap);

            String text= baseApi.getUTF8Text();

            DebugUtil.e("doOcr " + text);

            baseApi.clear();

            return text;
        }catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    public String doOcrRect(Bitmap bitmap, Rect rect) {

        try {
            if(bitmap == null)
                return null;

            baseApi.setImage(bitmap);
            baseApi.setRectangle(rect);

            String text= baseApi.getUTF8Text();

            DebugUtil.e("doOcrRect " + text);

            baseApi.clear();

            return text;
        }catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

}
