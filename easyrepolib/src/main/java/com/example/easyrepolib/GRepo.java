package com.example.easyrepolib;

import android.content.Context;

import java.io.File;

/**
 * Created by ali on 8/20/18.
 */

public abstract class GRepo {

    File ModeRootPath;

    public enum Mode {
        LOCAL,
        CACHE,
        EXTERNAL,//this need permission
    }

    /**
     * @param context context
     * @param mode    one of GRepo.LOCAL , GRepo.CACHE , GRepo.EXTERNAL
     */
    public GRepo(Context context, Mode mode) {
        switch (mode) {
            case LOCAL:
                ModeRootPath = context.getFilesDir();
                break;
            case CACHE:
                ModeRootPath = context.getCacheDir();
                break;
            case EXTERNAL:
                ModeRootPath = context.getExternalFilesDir(null);
                break;
        }
    }

    public void Remove(String filename) {
        filename = ModeRootPath + "/" + filename;
        File f = new File(filename);
        if (f.exists()) f.delete();
    }
}