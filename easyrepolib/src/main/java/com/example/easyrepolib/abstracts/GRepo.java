package com.example.easyrepolib.abstracts;

import android.content.Context;

import java.io.File;

import static android.os.Environment.getDataDirectory;
import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by ali on 8/20/18.
 */

public abstract class GRepo {

    protected File ModeRootPath;

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
                ModeRootPath = getExternalStorageDirectory();
                break;
        }
    }
}
