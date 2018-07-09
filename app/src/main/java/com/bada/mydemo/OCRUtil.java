package com.bada.mydemo;

import android.content.Context;
import android.graphics.Rect;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.Location;
import com.baidu.ocr.sdk.model.Word;
import com.baidu.ocr.sdk.model.WordSimple;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OCRUtil {

    private static OCRUtil instance = null;
    public static synchronized OCRUtil getInstance() {
        if(instance == null) {
            instance = new OCRUtil();
        }
        return instance;
    }

    interface RectCB {
        void onGetRect(List<Word> rectList);
    }

    public void getRect(final String text, final RectCB cb, String filePath, final boolean isEqual) {
        // 通用文字识别参数设置
        GeneralParams param = new GeneralParams();
        param.setDetectDirection(false);
        param.setLanguageType("CHN_ENG");
//        param.getStringParams().put("+words", "ECHELON");

        param.setImageFile(new File(filePath));

        Context context = ContextModel.getInstance().getContext();
        OCR.getInstance(context).recognizeGeneral(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
//                StringBuilder sb = new StringBuilder();
                /*
                List<Rect> rectList = new ArrayList<>();

                for (WordSimple wordSimple : result.getWordList()) {

                    Word word = (Word) wordSimple;
//                    sb.append(word.getWords());
//                    sb.append("\n");

//                    if(word.getWords().equals("梯队")){
//                        DebugUtil.e("found 梯队 at " + word.getLocation().getLeft() + " " + word.getLocation().getTop() + " " + word.getLocation().getWidth() + " " + word.getLocation().getHeight());
//                    }
//
//                    if(word.getWords().contains("生命")){
                        DebugUtil.debug("found: " +word.getWords() + " at " + word.getLocation().getLeft() + " " + word.getLocation().getTop() + " " + word.getLocation().getWidth() + " " + word.getLocation().getHeight());
//                    }
                    if(isEqual){
                        if(word.getWords().equals(text)){
                            Location location = word.getLocation();
                            int right = location.getLeft() + location.getWidth();
                            int bottom = location.getTop() + location.getHeight();

                            Rect rect = new Rect(location.getLeft(), location.getTop(), right, bottom );

                            rectList.add(rect);
                        }
                    }else{
                        if(word.getWords().contains(text)){
                            Location location = word.getLocation();
                            int right = location.getLeft() + location.getWidth();
                            int bottom = location.getTop() + location.getHeight();

                            Rect rect = new Rect(location.getLeft(), location.getTop(), right, bottom );

                            rectList.add(rect);
                        }
                    }
                }
*/
                ArrayList<Word> list = new ArrayList<>();
                for (WordSimple wordSimple : result.getWordList()){
                    Word word = (Word)wordSimple;
                    list.add(word);
                }

                if(cb != null){
                    cb.onGetRect(list);
                }

                // 调用成功，返回GeneralResult对象，通过getJsonRes方法获取API返回字符串
//                listener.onResult(result.getJsonRes());
//                DebugUtil.e("onResult: " + sb.toString());
//                DebugUtil.e(result.getJsonRes());
            }
            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
                DebugUtil.e("onError " + error.toString());
                error.printStackTrace();
            }
        });
    }

    public void setImage(String path){

    }
}
