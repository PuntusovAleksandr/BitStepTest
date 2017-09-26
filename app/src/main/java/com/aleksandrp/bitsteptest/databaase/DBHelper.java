package com.aleksandrp.bitsteptest.databaase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aleksandrp.bitsteptest.api.model.NewUserModel;
import com.aleksandrp.bitsteptest.api.model._UserModel;

/**
 * Created by AleksandrP on 26.09.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private ContentValues cv;

    private static final String NAME_DB = "testDB";

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, NAME_DB, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // создаем таблицу с полями
        db.execSQL("create table USER ("
                + "email text,"
                + "company text,"
                + "address text,"
                + "site text,"
                + "phone text,"
                + "avatar text);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void initSqlDb(DBHelper dbHelper) {
        db = dbHelper.getWritableDatabase();
        cv = new ContentValues();
    }

    public void putUser(_UserModel mData) {
        db.delete("USER", null, null);

        cv.put("email", mData.email);
        cv.put("company", mData.company);
        cv.put("address", mData.address);
        cv.put("site", mData.site);
        cv.put("phone", mData.phone);
        cv.put("avatar", mData.avatar);
        db.insert("USER", null, cv);
    }

    public NewUserModel getUser() {
        NewUserModel model = null;
        Cursor c = db.query("USER", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                model = new NewUserModel(
                        c.getString(c.getColumnIndex("email")),
                        c.getString(c.getColumnIndex("company")),
                        c.getString(c.getColumnIndex("address")),
                        c.getString(c.getColumnIndex("site")),
                        "",
                        c.getString(c.getColumnIndex("phone")),
                        c.getString(c.getColumnIndex("avatar")));

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        return model;
    }

}


