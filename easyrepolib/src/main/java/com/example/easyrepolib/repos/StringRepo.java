package com.example.easyrepolib.repos;

import android.content.Context;
import android.util.Log;

import com.example.easyrepolib.abstracts.GRepo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by ali on 8/22/18.
 */

public class StringRepo extends GRepo {


    private Context _context;
    private String postFix = ".txt";

    /**
     * @param context context
     * @param mode    one of GRepo.LOCAL , GRepo.CACHE , GRepo.EXTERNAL
     */
    public StringRepo(Context context, Mode mode) {
        super(context, mode);
        _context = context;
    }

    public StringRepo(Context context, Mode mode, String postFix) {
        super(context, mode);
        _context = context;
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


    public boolean CheckExist(String fileName) {
        return Load(fileName) != null;
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

    public void Remove(String filename) {
        filename = ModeRootPath + "/" + filename + postFix;
        File f = new File(filename);
        if (f.exists()) f.delete();
    }

}
