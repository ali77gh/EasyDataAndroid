package com.example.easyrepolib.repos;

import android.app.Activity;
import android.content.Context;

import com.example.easyrepolib.abstracts.GRepo;
import com.example.easyrepolib.abstracts.onSaveCompleted;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ali on 8/20/18.
 */

public class ByteDAO extends GRepo {

    public interface OnBytesLoad {
        void onBytesLoad(byte[] bytes);
    }

    /**
     * @param context context
     * @param mode    one of GRepo.LOCAL , GRepo.CACHE , GRepo.EXTERNAL
     */
    public ByteDAO(Context context, Mode mode) {
        super(context, mode);
        postFix=".bytes";
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

    public void LoadAsync(final String filename, final Activity activity, final OnBytesLoad callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final byte[] bytes = Load(filename);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onBytesLoad(bytes);
                    }
                });
            }
        }).start();

    }

    public void Save(String filename, byte[] bytes) {
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

    public void SaveAsync(final String filename, final byte[] bytes, final Activity activity, final onSaveCompleted callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Save(filename, bytes);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSaveComplete();
                    }
                });
            }
        }).start();

    }

    public void SaveAsync(final String filename, final byte[] bytes) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Save(filename, bytes);
            }
        }).start();
    }

}
