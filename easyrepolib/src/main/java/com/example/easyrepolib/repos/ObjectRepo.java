package com.example.easyrepolib.repos;

import android.content.Context;

import com.example.easyrepolib.abstracts.GModel;
import com.example.easyrepolib.abstracts.GRepo;
import com.google.gson.Gson;

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

    public T Load(String filename) {
        String stringAccount = _stringRepo.Load(filename);

        try {
            return (T) _gson.fromJson(stringAccount, GModel.class);
        } catch (ClassCastException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }

    }

    public boolean CheckExist(String filename) {
        return (Load(filename) != null);
    }

    public void Save(T object, String filename) {
        _stringRepo.Save(_gson.toJson(object), filename);

    }


}
