package com.bada.mydemo;

import android.content.Context;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.Word;
import com.baidu.ocr.sdk.model.WordSimple;

import java.io.File;

public class OCRManager2 {
    private static OCRManager2 instance;
    public static synchronized  OCRManager2 getInstance() {
        if(instance == null){
            instance = new OCRManager2();
        }

        return instance;
    }

    private OCRManager2(){

    }

    public void init(){

        Context context = ContextModel.getInstance().getContext();
        OCR.getInstance(context).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                // 调用成功，返回AccessToken对象
                String token = result.getAccessToken();

                DebugUtil.e("onResult " + token);
            }
            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError子类SDKError对象

                try {
                    DebugUtil.e("onError " + error.getMessage());
                    error.printStackTrace();
                }catch (Throwable e){
                    e.printStackTrace();
                }
            }
        }, context, "2WlelgwUtrpD4Sxb6ybHGUf1", "wkHuyiBHo9vnRFolVNDDLiiFA8CRKuk9");
    }

    public void doOcr(){
        // 通用文字识别参数设置
        GeneralParams param = new GeneralParams();
        param.setDetectDirection(true);
        param.setImageFile(new File(ClickThread.screen_group1));

        Context context = ContextModel.getInstance().getContext();
        OCR.getInstance(context).recognizeGeneral(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                StringBuilder sb = new StringBuilder();
                for (WordSimple wordSimple : result.getWordList()) {

                    Word word = (Word) wordSimple;
                    sb.append(word.getWords());
                    sb.append("\n");

//                    if(word.getWords().equals("梯队")){
//                        DebugUtil.e("found 梯队 at " + word.getLocation().getLeft() + " " + word.getLocation().getTop() + " " + word.getLocation().getWidth() + " " + word.getLocation().getHeight());
//                    }
//
                    if(word.getWords().contains("生命")){
                        DebugUtil.e("found" +word.getWords() + " at " + word.getLocation().getLeft() + " " + word.getLocation().getTop() + " " + word.getLocation().getWidth() + " " + word.getLocation().getHeight());
                    }

                }

                // 调用成功，返回GeneralResult对象，通过getJsonRes方法获取API返回字符串
//                listener.onResult(result.getJsonRes());
                DebugUtil.e("onResult: " + sb.toString());
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
}
