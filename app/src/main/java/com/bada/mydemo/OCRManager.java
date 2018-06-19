package com.bada.mydemo;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.text.Html;

import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.Pixa;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

import static com.googlecode.tesseract.android.TessBaseAPI.OEM_TESSERACT_ONLY;

public class OCRManager {
    private static OCRManager instance = null;
    public static synchronized OCRManager getInstance() {
        if(instance == null) {
            instance = new OCRManager();
        }
        return instance;
    }

    public static final String TESSBASE_PATH = Environment.getExternalStorageDirectory() + "/" + "tess";

    TessBaseAPI baseApi = null;
    public void init() {
        baseApi = new TessBaseAPI();
        baseApi.init(TESSBASE_PATH, "chi_sim");
//        baseApi.init(TESSBASE_PATH, "eng");
//        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_BLOCK);
    }

    public String doOcr(Bitmap bitmap, Rect rect) {

        try {
            if(bitmap == null)
                return null;

            baseApi.setImage(bitmap);
            if(rect != null)
                baseApi.setRectangle(rect);

            String text= baseApi.getUTF8Text();


//            final String hOcr = baseApi.getHOCRText(0);
//            final String outputText = Html.fromHtml(hOcr).toString().trim();
//

            DebugUtil.e("output = " + new String(text.getBytes(), StandardCharsets.UTF_8));

            baseApi.clear();

            return text;
        }catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

//    public String doOcrRect(Bitmap bitmap, Rect rect) {
//
//        try {
//            if(bitmap == null)
//                return null;
//
//            baseApi.setImage(bitmap);
//            baseApi.setRectangle(rect);
//
//            String text= baseApi.getUTF8Text();
//
//            DebugUtil.e("doOcrRect " + text);
//
//            baseApi.clear();
//
//            return text;
//        }catch (Throwable e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

}
