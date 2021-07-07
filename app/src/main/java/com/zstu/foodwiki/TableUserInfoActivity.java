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
    public static final int UPDATE = 3;

    private int pk_id;
    private int userid;
    private int figureid;
    private String name;
    private String remark;
    private int follows;
    private int followers;
    private int readers;

    private String new_name;
    private String new_remark;


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
                    intent1.putExtra("figureid", figureid);
                    setResult(290,intent1);
                    finish();
                }
                break;
            case INSERT:
                userid = intent.getIntExtra("userid",-1);
                insertUserInfo(true);
                finish();
                break;
            case UPDATE:
                userid = intent.getIntExtra("userid",-1);
                new_name = intent.getStringExtra("new_name");
                new_remark = intent.getStringExtra("new_remark");
                updateUserInfoByUserId(userid);
                Intent intent2 = new Intent();
                setResult(120,intent2);
                finish();
                break;
            default:
                break;

        }
    }

    public void insertUserInfo(boolean setDefalutValue){

        System.out.println("InsertUserInfo function called!!!!!!!");

        UserInfoDBHelper dbHelper = new UserInfoDBHelper(TableUserInfoActivity.this, "tb_userinfo", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        ContentValues cv = new ContentValues();


        if(setDefalutValue){
            /*****************默认值*******************/
            cv.put("userid", userid);
            cv.put("figureid", 1);
            cv.put("name", "尚未填写昵称");
            cv.put("remark", "这家伙很懒，什么都没写~");
            cv.put("follows", 0);
            cv.put("followers", 0);
            cv.put("readers", 0);
            /*****************默认值*******************/
        }
        else{

            cv.put("userid", userid);
            cv.put("figureid", figureid);
            cv.put("name", name);
            cv.put("remark", remark);
            cv.put("follows", follows);
            cv.put("followers", followers);
            cv.put("readers", readers);
        }

        db.insert("tb_userinfo", null, cv);

        db.close();
    }

    public boolean updateUserInfoByUserId(int id){
        UserInfoDBHelper dbHelper = new UserInfoDBHelper(TableUserInfoActivity.this, "tb_userinfo", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        ContentValues cv = new ContentValues();
        cv.put("name", new_name);
        cv.put("remark", new_remark);

        String whereClause = "userid=?";
        String[] whereArgs = {String.valueOf(id)};

        db.update("tb_userinfo",cv,whereClause, whereArgs);

        db.close();
        return false;
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
            figureid = cursor.getInt(cursor.getColumnIndex("figureid"));
        }
        db.close();
        return true;
    }
}
