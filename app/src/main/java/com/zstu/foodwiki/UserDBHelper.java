package com.zstu.foodwiki;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDBHelper extends SQLiteOpenHelper {

    private  static final  String TAG = "UserSQLite";
    public static final  int VERSION = 1;

    public static final String dbPath = "/data/data/com.zstu.foodwiki/databases/foodwiki.db";

    public UserDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table tb_user" +
                "(" +
                "id integer primary key autoincrement," +
                "username varchar(50)," +
                "password varchar(50)" +
                ")";
        Log.i(TAG, "create Database------------->");
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "update Database------------->");
    }
}
