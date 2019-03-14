package com.example.easyrepolib.repos;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.easyrepolib.abstracts.GRepo;
import com.example.easyrepolib.abstracts.onSaveCompleted;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by ali on 8/22/18.
 */

public class StringDAO extends GRepo {

    public interface OnStringLoad {
        void onStringLoad(String string);
    }

    /**
     * @param context context
     * @param mode    one of GRepo.LOCAL , GRepo.CACHE , GRepo.EXTERNAL
     */
    public StringDAO(Context context, Mode mode) {
        super(context, mode);
        postFix = ".txt";
    }

    public StringDAO(Context context, Mode mode, String postFix) {
        super(context, mode);
        this.postFix = postFix;
    }

    /**
     * @return null if not exist
     */
    public String Load(String fileName) {

        fileName = ModeRootPath + "/" + fileName + postFix;

        try {
            FileInputStream inputStream = new FileInputStream(new File(fileName));

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                return stringBuilder.toString();
            }
            return null;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void LoadAsync(final String filename, final Activity activity, final OnStringLoad callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String string = Load(filename);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onStringLoad(string);
                    }
                });
            }
        }).start();

    }

    public void Save(String fileName, String data) {

        fileName = ModeRootPath + "/" + fileName + postFix;
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void SaveAsync(final String filename, final String string, final Activity activity, final onSaveCompleted callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Save(filename, string);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSaveComplete();
                    }
                });
            }
        }).start();

    }

    public void SaveAsync(final String filename, final String string) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Save(filename, string);
            }
        }).start();
    }

}
