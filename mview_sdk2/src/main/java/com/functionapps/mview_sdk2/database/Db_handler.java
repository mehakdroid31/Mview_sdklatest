package com.functionapps.mview_sdk2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Db_handler extends SQLiteOpenHelper {
    private  Context context;
    private SQLiteDatabase db;

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public Db_handler open() {
        try {
            db = getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return this;
    }
    private static final String CREATE_APP_INFO="CREATE TABLE app_info " +
            "(dbId INTEGER, "+
            "graphId INTEGER, " +
            "graphName VARCHAR(256)); ";

    public Db_handler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, Context context1) {
        super(context, name, factory, version);
        this.context = context1;
        open();
    }
}
