package com.zstu.foodwiki;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserInfoDBHelper extends SQLiteOpenHelper {
    private  static final  String TAG = "UserInfoSQLite";
    public static final  int VERSION = 1;
    public static final String dbPath = "/data/data/com.zstu.foodwiki/databases/foodwiki.db";

    public UserInfoDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table tb_userinfo" +
                "(" +
                "id integer primary key autoincrement," +
                "userid integer," +
                "figureid integer,"+
                "name varchar(50)," +
                "follows int," +
                "followers int," +
                "readers int," +
                "remark varchar(200)," +
                "foreign key(userid) references tb_user(id),"+
                "foreign key(figureid) references tb_file(id)"+
                ")";
        Log.i(TAG, "create Database------------->");
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "update Database------------->");
    }
}
