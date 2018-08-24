package com.example.easyrepolib.repos;

import android.content.Context;

import com.example.easyrepolib.abstracts.GModel;
import com.example.easyrepolib.abstracts.GRepo;
import com.google.gson.Gson;

import java.io.File;

/**
 * Created by ali on 8/22/18.
 */

public class ObjectRepo<T extends GModel> extends GRepo {

    private Gson _gson;
    private StringRepo _stringRepo;
    private String postFix = ".json";

    /**
     * @param context context
     * @param mode    one of GRepo.LOCAL , GRepo.CACHE , GRepo.EXTERNAL
     */
    public ObjectRepo(Context context, Mode mode) {
        super(context, mode);
        _gson = new Gson();
        _stringRepo = new StringRepo(context, mode, postFix);
    }

    public T Load(String filename, Class<T> type) {
        String stringAccount = _stringRepo.Load(filename);

        try {
            return _gson.fromJson(stringAccount, type);
        } catch (ClassCastException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }

    }

    public boolean CheckExist(String filename, Class<T> type) {
        return (Load(filename, type) != null);
    }

    public void Save(T object, String filename) {
        _stringRepo.Save(filename, _gson.toJson(object));
    }

    public void Remove(String filename) {
        filename = ModeRootPath + "/" + filename + postFix;
        File f = new File(filename);
        if (f.exists()) f.delete();
    }

}
