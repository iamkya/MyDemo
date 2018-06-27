package com.bada.mydemo;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.text.Html;

import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.Pixa;
import com.googlecode.tesseract.android.ResultIterator;
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
        baseApi.init(TESSBASE_PATH, "eng+chi_sim+Mohave");
        baseApi.setVariable("OMP_THREAD_LIMIT", "4");
//        baseApi.init(TESSBASE_PATH, "eng");
//        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_BLOCK);
//        baseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "/.,0123456789");
        //baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
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

    public void setImage(Bitmap bitmap) {
        baseApi.setImage(bitmap);
    }

    public String doOcrRect(Rect rect){
        return doOcrRect(rect, false);
    }

    public String doOcrRect(Rect rect, boolean isNumber) {

        if(isNumber){
            baseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "/0123456789");
            baseApi.setVariable("classify_bln_numeric_mode", "1");

        }

        baseApi.setRectangle(rect);
        String text= baseApi.getUTF8Text();
        return text;
    }

    public void getWords(Bitmap bitmap) {
        try {
            if(bitmap == null)
                return;

            baseApi.setImage(bitmap);

            baseApi.getUTF8Text();

            ResultIterator resultIterator = null;

            resultIterator = baseApi.getResultIterator();
            resultIterator.begin();


            do {
                String word = resultIterator.getUTF8Text(TessBaseAPI.PageIteratorLevel.RIL_WORD);
                Rect rect = resultIterator.getBoundingRect(TessBaseAPI.PageIteratorLevel.RIL_WORD);
                double confidence = resultIterator.confidence(TessBaseAPI.PageIteratorLevel.RIL_WORD);

                if (word == null
                        || !rect.intersects(0, 0, bitmap.getWidth(), bitmap.getHeight())) {

                    // No results return invalid rect, check for this case and continue
                    continue;
                }

                DebugUtil.e("word:" + word + " rect:" + rect);

            } while (resultIterator.next(TessBaseAPI.PageIteratorLevel.RIL_WORD));

            DebugUtil.e("e.printStackTrace();");
        }catch (Throwable e) {
            e.printStackTrace();
        }

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
