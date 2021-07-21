package com.zstu.foodwiki;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mob.tools.utils.SQLiteHelper;

public class FoodDBHelper extends SQLiteOpenHelper {

    private  static final  String TAG = "FoodSQLite";
    public static final  int VERSION = 1;
    public static final String dbPath = "/data/data/com.zstu.foodwiki/databases/foodwiki.db";

    public FoodDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table tb_food" +
                "(" +
                "id integer primary key autoincrement," +
                "userid integer,"+
                "fileid integer," +
                "title varchar(50)," +
                "contentdetails varchar(1000)," +
                "selfcomment varchar(300)," +
                "phonenumber varchar(50)," +
                "likes integer," +
                "stars integer," +
                "shares integer," +
                "reads integer," +
                "foreign key(userid) references tb_user(id),"+
                "foreign key(fileid) references tb_file(id)" +
                ")";
        Log.i(TAG, "create Database------------->");
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "update Database------------->");
    }
}
