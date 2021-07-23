package com.example.easyrepolib.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ali on 9/17/18.
 */

public class KeyValDb {

    //todo defind id as primary key

    private final SQLiteDatabase DB;
    private final Gson gson;
    private final String table;
    private final String dateBaseName = "EasyRepoDataBase";

    /**
     * @param tableName create table if not exist
     */
    public KeyValDb(Context context, String tableName) {
        DB = context.openOrCreateDatabase(dateBaseName, MODE_PRIVATE, null);
        gson = new Gson();
        this.table = tableName;
        DB.execSQL(
                "CREATE TABLE IF NOT EXISTS " +
                        tableName + "(" +
                        "id VARCHAR ," +
                        "val VARCHAR" +
                        ");"
        );
    }

    public void insert(String id, Object object) {

        String strObj = gson.toJson(object);
        DB.execSQL(
                "INSERT INTO " + table + " VALUES('" + id + "','" + strObj + "');"
        );
    }

    public Object Read(String id, Class<?> type) {
        Cursor resultSet = DB.rawQuery("Select * from " + table + " where id='" + id + "';", null);
        resultSet.moveToFirst();
        String strObj = resultSet.getString(1);
        resultSet.close();
        return gson.fromJson(strObj, type);
    }

    public boolean IsEmpty() {
        Cursor resultSet = DB.rawQuery("Select * from " + table + ";", null);
        resultSet.moveToFirst();
        try {
            resultSet.getString(1);
            return false;
        } catch (CursorIndexOutOfBoundsException e) {
            return true;
        }

    }

    public List<Object> ReadAllOfType(Class<?> type) {

        List<Object> validObjs = new ArrayList<>();
        Cursor resultSet = DB.rawQuery("Select * from " + table + ";", null);
        resultSet.moveToFirst();
        String strObj;
        Object obj;
        while (!resultSet.isAfterLast()) {
            strObj = resultSet.getString(1);
            try {
                obj = gson.fromJson(strObj, type);
                validObjs.add(obj);
            } catch (Exception e) {/*ignore*/}

            resultSet.moveToNext();
        }

        resultSet.close();
        return validObjs;
    }

    public List<Object> ReadWithCondition(Condition condition, Class<?> type) {

        List<Object> validObjs = new ArrayList<>();
        Cursor resultSet = DB.rawQuery("Select * from " + table + ";", null);
        resultSet.moveToFirst();
        while (!resultSet.isAfterLast()) {
            String strObj = resultSet.getString(1);
            Object obj;
            try {
                obj = gson.fromJson(strObj, type);
            } catch (Exception e) {
                resultSet.moveToNext();
                continue;
            }

            if (condition.IsConditionTrue(obj)) {
                validObjs.add(obj);
            }
            resultSet.moveToNext();
        }

        resultSet.close();
        return validObjs;
    }

    public void Update(String id, Object object) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("val", gson.toJson(object));
        DB.update(table, contentValues, "id = ? ", new String[]{id});
    }

    public void Remove(String... ids) {
        DB.delete(table, "id = ? ", ids);
    }

    public void Drop() {
        DB.execSQL("DROP TABLE IF EXISTS " + table);
    }

    public interface Condition {
        /**
         * this method effect on your performance make it fast
         *
         * @param object readed object
         * @return true if your condition is true else return false
         */
        boolean IsConditionTrue(Object object);
    }
}
