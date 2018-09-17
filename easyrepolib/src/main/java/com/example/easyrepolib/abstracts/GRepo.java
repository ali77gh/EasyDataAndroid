package com.example.easyrepolib.abstracts;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by ali on 8/20/18.
 */

public abstract class GRepo {

    protected File ModeRootPath;

    protected String postFix;

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

    /**
     * @return true if file exist
     */
    public boolean CheckExist(String fileName) {
        fileName = ModeRootPath + "/" + fileName + postFix;
        File f = new File(fileName);
        if (f.exists()) return true;
        else return false;
    }

    /**
     * @param filename be careful about user files in external storage mode
     * */
    public void Remove(String filename) {
        filename = ModeRootPath + "/" + filename + postFix;
        File f = new File(filename);
        if (f.exists()) f.delete();
    }

    public List<File> GetAll() {
        File[] files = ModeRootPath.listFiles();

        List<File> matches = new ArrayList<>();
        for (File file : files) {
            try {
                String n = file.getName();
                n = n.substring(n.indexOf(".")+1);
                boolean t = n.equals(postFix.substring(1));
                if (t) {
                    matches.add(file);
                }
            } catch (IndexOutOfBoundsException ignore) {
            }
        }
        return matches;
    }

    public void RemoveAll() {

        if (ModeRootPath.equals(getExternalStorageDirectory())){
            throw new RuntimeException("don't use remove all in external mode ");
        }
        List<File> files = GetAll();
        for (File file : files) {
            file.delete();
        }
    }
}
