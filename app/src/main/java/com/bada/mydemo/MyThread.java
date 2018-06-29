package com.bada.mydemo;

import android.graphics.Rect;

import com.bada.mydemo.dataType.ClickRect;

import java.util.HashMap;
import java.util.List;

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

            clickText("COMBAT", "combatRect");

            Thread.sleep(5000);

            clickText("COMBAT", "combatRect");

        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    private final Object waitLock = new Object();

    void clickText(final String text, final String tag) throws Throwable{

        ClickRect clickRect = rectMap.get(tag);
        if(clickRect == null) {
            clickRect = getRectViaEngine(text, tag);
        }

        if(clickRect == null){
            DebugUtil.e("ClickRect == null, returning");
            System.exit(0);
            return;
        }

        DebugUtil.e(clickRect.getClickPoint());
//        click2(clickRect);
    }


    ClickRect getRectViaEngine(final String text, final String tag){

        final ClickRect[] resultRect = {null};

        synchronized (waitLock){

        OCRUtil.getInstance().getRect(text, new OCRUtil.RectCB() {
            @Override
            public void onGetRect(List<Rect> rectList) {

                DebugUtil.e("onGetRect size = " + rectList.size());
                if(rectList.size() == 0){

                    return;
                }

                if(rectList.size() > 1) {
                    DebugUtil.e("onGetRect size > 1");
                }else{
                    DebugUtil.e("onGetRect size == 1");
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
        });

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
