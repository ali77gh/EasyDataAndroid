package com.example.easyrepolib.repos;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.easyrepolib.abstracts.GRepo;
import com.example.easyrepolib.abstracts.onSaveCompleted;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ali on 8/20/18.
 */

public class BitmapDAO extends GRepo {

    public interface OnBitmapLoad {
        void onBitmapLoad(Bitmap bitmap);
    }

    /**
     * @param context context
     * @param mode    one of GRepo.LOCAL , GRepo.CACHE , GRepo.EXTERNAL
     */
    public BitmapDAO(Context context, Mode mode) {
        super(context, mode);
        postFix = ".png";
    }

    /**
     * @param fileName name of file you want to load
     * @return return null if file not exist & on exception (checkout logs)
     **/
    public Bitmap Load(String fileName) {
        fileName = ModeRootPath + "/" + fileName + postFix;
        File f = new File(fileName);
        if (!f.exists()) {
            return null;
        }
        return BitmapFactory.decodeFile(fileName);
    }

    public void LoadAsync(final String filename, final Activity activity, final OnBitmapLoad callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = Load(filename);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onBitmapLoad(bitmap);
                    }
                });
            }
        }).start();

    }

    /**
     * @param filename name of file you want to load
     * @param bmp      bitmap you want to write on file
     **/
    public void Save(String filename, Bitmap bmp) {
        filename = ModeRootPath + "/" + filename + postFix;
        try {
            FileOutputStream out = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void SaveAsync(final String filename, final Bitmap bmp, final Activity activity, final onSaveCompleted callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Save(filename, bmp);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSaveComplete();
                    }
                });
            }
        }).start();

    }

    public void SaveAsync(final String filename, final Bitmap bmp) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Save(filename, bmp);
            }
        }).start();
    }


}

