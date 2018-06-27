package com.bada.mydemo;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

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
                new AssetCopier(this).copy("Mohave.traineddata", folder);
            }

            OCRManager.getInstance().init();


            boolean initAsync = OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, new LoaderCallbackInterface() {
                @Override
                public void onManagerConnected(int status) {

                    DebugUtil.e("OpenCVLoader onManagerConnected " + status);
                }

                @Override
                public void onPackageInstall(int operation, InstallCallbackInterface callback) {

                    DebugUtil.e("OpenCVLoader onPackageInstall " + operation);
                }
            });
            DebugUtil.e("OpenCVLoader init result " + initAsync);

        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}
