package com.example.easyrepolib.repos;

import android.app.Activity;
import android.content.Context;

import com.example.easyrepolib.abstracts.GModel;
import com.example.easyrepolib.abstracts.GRepo;
import com.example.easyrepolib.abstracts.onSaveCompleted;
import com.google.gson.Gson;

/**
 * Created by ali on 8/22/18.
 */

public class ObjectRepo<T extends GModel> extends GRepo {

    private Gson _gson;
    private StringRepo _stringRepo;

    public interface OnObjectLoad {
        void onObjectLoad(Object obj);
    }

    /**
     * @param context context
     * @param mode    one of GRepo.LOCAL , GRepo.CACHE , GRepo.EXTERNAL
     */
    public ObjectRepo(Context context, Mode mode) {
        super(context, mode);
        _gson = new Gson();
        postFix = ".json";
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

    public void LoadAsync(final String filename,final Class<T> type, final Activity activity, final OnObjectLoad callback ) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Object obj = Load(filename,type);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onObjectLoad(obj);
                    }
                });
            }
        }).start();

    }

    public void Save(String filename,T object) {
        _stringRepo.Save(filename, _gson.toJson(object));
    }

    public void SaveAsync(final String filename, final T  obj, final Activity activity,final onSaveCompleted callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Save(filename, obj);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSaveComplete();
                    }
                });
            }
        }).start();

    }

    public void SaveAsync(final String filename, final T obj) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Save(filename, obj);
            }
        }).start();
    }

}
