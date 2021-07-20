package com.zstu.foodwiki;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FoodLikeDBHelper extends SQLiteOpenHelper {

    private  static final  String TAG = "FoodLikeSQLite";
    public static final  int VERSION = 1;

    public FoodLikeDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table tb_foodlike" +
                "(" +
                "id integer primary key autoincrement," +
                "userid integer," +
                "foodid integer,"+
                "foreign key(userid) references tb_user(id),"+
                "foreign key(foodid) references tb_food(id)"+
                ")";
        Log.i(TAG, "create Database------------->");
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "update Database------------->");
    }
}
