package com.zstu.foodwiki;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserFollowDBHelper extends SQLiteOpenHelper {

    private  static final  String TAG = "UserFollowSQLite";
    public static final  int VERSION = 1;
    public static final String dbPath = "/data/data/com.zstu.foodwiki/databases/foodwiki.db";

    public UserFollowDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table tb_userfollow" +
                "(" +
                "id integer primary key autoincrement," +
                "userid integer," +
                "followid integer,"+
                "iscancelfollow boolean,"+
                "foreign key(userid) references tb_user(id),"+
                "foreign key(followid) references tb_user(id)"+
                ")";
        Log.i(TAG, "create Database------------->");
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "update Database------------->");
    }
}
