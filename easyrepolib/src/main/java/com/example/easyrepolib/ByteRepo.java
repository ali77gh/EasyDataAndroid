package com.example.easyrepolib;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ali on 8/20/18.
 */

public class ByteRepo extends GRepo {

    /**
     * @param context context
     * @param mode    one of GRepo.LOCAL , GRepo.CACHE , GRepo.EXTERNAL
     */
    public ByteRepo(Context context, Mode mode) {
        super(context, mode);
    }

    public void Save(byte[] bytes, String filename) {
        filename = ModeRootPath + "/" + filename;
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean CheckExist(String fileName) {
        return Load(fileName) != null;
    }

    public byte[] Load(String filename) {
        filename = ModeRootPath + "/" + filename;
        try {
            int size = (int) new File(filename).length();
            byte[] bytes = new byte[size];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(filename));
                buf.read(bytes, 0, bytes.length);
                buf.close();
                return bytes;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
