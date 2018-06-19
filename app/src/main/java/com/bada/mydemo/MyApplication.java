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

            File folder = new File(TESSBASE_PATH + "/tessdata");

            DebugUtil.e("folder " + folder.getAbsolutePath());
            if(!folder.exists()) {
                folder.mkdirs();

                new AssetCopier(this).copy("chi_sim.traineddata", folder);
                new AssetCopier(this).copy("eng.traineddata", folder);
            }

            OCRManager.getInstance().init();



        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}
