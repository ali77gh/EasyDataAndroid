package com.example.easyrepolib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ali on 8/20/18.
 */

public class BitmapRepo extends GRepo {


    /**
     * @param context context
     * @param mode    one of GRepo.LOCAL , GRepo.CACHE , GRepo.EXTERNAL
     */
    public BitmapRepo(Context context, Mode mode) {
        super(context, mode);
    }

    /**
     * @param fileName name of file you want to load
     * @return return null if file not exist & on exception (checkout logs)
     **/
    public Bitmap Load(String fileName) {
        fileName = ModeRootPath + "/" + fileName;
        File f = new File(fileName);
        if (!f.exists()) {
            return null;
        }
        return BitmapFactory.decodeFile(fileName);
    }

    /**
     * @return true if file exist
     * */
    public boolean CheckExist(String fileName) {
        return Load(fileName) != null;
    }

    /**
     * @param filename name of file you want to load
     * @param bmp      bitmap you want to write on file
     **/
    public void Save(String filename, Bitmap bmp) {
        filename = ModeRootPath + "/" + filename;
        try {
            FileOutputStream out = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
