package com.zstu.foodwiki;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class TableUserInfoActivity extends AppCompatActivity {

    public static final int GET_ALL = 1;
    public static final int INSERT = 2;

    private int pk_id;
    private int userid;
    private String name;
    private String remark;
    private int follows;
    private int followers;
    private int readers;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int op = intent.getIntExtra("operation",0);

        switch (op){
            case GET_ALL:
                userid = intent.getIntExtra("userid",0);
                if(queryAllById(userid)){
                    Intent intent1 = new Intent();
                    intent1.putExtra("name", name);
                    intent1.putExtra("follows", follows);
                    intent1.putExtra("followers", followers);
                    intent1.putExtra("readers", readers);
                    intent1.putExtra("remark", remark);
                    setResult(290,intent1);
                    finish();
                }
                break;
            case INSERT:
                InsertUserInfo();
                finish();
                break;
            default:
                break;

        }
    }

    public void InsertUserInfo(){

        System.out.println("InsertUserInfo function called!!!!!!!");

        UserInfoDBHelper dbHelper = new UserInfoDBHelper(TableUserInfoActivity.this, "tb_userinfo", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        ContentValues cv = new ContentValues();

        /* cv.put("userid", userid);
        cv.put("name", name);
        cv.put("remark", remark);
        cv.put("follows", follows);
        cv.put("followers", followers);
        cv.put("readers", readers);*/

        /*****************仅测试*******************/
        cv.put("userid", "1");
        cv.put("name", "半杯咖啡享一世");
        cv.put("remark", "这世间，唯爱与美食不可辜负（关注我了解更多本地美食）");
        cv.put("follows", 111);
        cv.put("followers", 233);
        cv.put("readers", 9999);
        /*****************仅测试*******************/
        db.insert("tb_userinfo", null, cv);

        db.close();
    }

    public boolean queryAllById(int id){
        UserInfoDBHelper dbHelper = new UserInfoDBHelper(TableUserInfoActivity.this, "tb_userinfo", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("tb_userinfo", null, "userid=?", new String[]{String.valueOf(id)}, null, null, null);

        if((cursor.moveToFirst())){
            name = cursor.getString(cursor.getColumnIndex("name"));
            remark = cursor.getString(cursor.getColumnIndex("remark"));
            follows = cursor.getInt(cursor.getColumnIndex("follows"));
            followers = cursor.getInt(cursor.getColumnIndex("followers"));
            readers = cursor.getInt(cursor.getColumnIndex("readers"));
        }
        db.close();
        return true;
    }
}
