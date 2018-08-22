package com.example.easyrepolib;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by ali on 8/22/18.
 */

public class StringRepo extends GRepo {


    private Context _context;
    /**
     * @param context context
     * @param mode    one of GRepo.LOCAL , GRepo.CACHE , GRepo.EXTERNAL
     */
    public StringRepo(Context context, Mode mode) {
        super(context, mode);
        _context = context;
    }

    /**
     * @return null if not exist
     * */
    public String Load(String fileName) {

        fileName = ModeRootPath + "/" + fileName;

        try {
            InputStream inputStream = _context.openFileInput(fileName);

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

    public void Save(String data, String fileName) {

        fileName = ModeRootPath + "/" + fileName;
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(_context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
