package com.bada.mydemo;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import java.io.File;

import static com.bada.mydemo.OCRManager.TESSBASE_PATH;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        try {

            ContextModel.getInstance().setContext(this);

            OCRManager.getInstance().init();

            File folder = new File(TESSBASE_PATH);
            if(!folder.exists()) {
                folder.mkdirs();

                new AssetCopier(this).copy("chi_sim.traineddata", folder);
            }

        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}
