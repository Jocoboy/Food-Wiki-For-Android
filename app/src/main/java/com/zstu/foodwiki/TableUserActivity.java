package com.zstu.foodwiki;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TableUserActivity extends AppCompatActivity {


    public static final int INSERT = 1;
    private static int curId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int op = intent.getIntExtra("operation",0);
        switch (op){
            case INSERT:
                String username = intent.getStringExtra("username");
                String password = intent.getStringExtra("password");
                InsertUser(curId,username,password);
                break;


                default:
                    break;

        }
    }

    public void queryUser(){

        UserDBHelper dbHelper = new UserDBHelper(TableUserActivity.this, "tb_user", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("tb_user", new String[]{"id","username","password"}, "username=?", new String[]{"1"}, null, null, null);
        while(cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            System.out.println("query------->" + "id："+id+" "+"年龄："+username+" "+"性别："+password);
        }

        db.close();
    }


    public  String queryUserPassword(String username){

        UserDBHelper dbHelper = new UserDBHelper(TableUserActivity.this, "tb_user", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("tb_user", new String[]{"id","username","password"}, "username=?", new String[]{username}, null, null, null);
        String password = cursor.getString(cursor.getColumnIndex("password"));

        db.close();
        return password;
    }

    public void InsertUser(int id,String username,String password){

        UserDBHelper dbHelper = new UserDBHelper(TableUserActivity.this, "tb_user", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("username", username);
        cv.put("password", password);
        db.insert("tb_user", null, cv);

        db.close();
    }

    public void modifyUserPassword(String username,String newPassword){

        UserDBHelper dbHelper = new UserDBHelper(TableUserActivity.this, "tb_user", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("password", newPassword);
        String whereClauses = "username=?";
        String[] whereArgs = {username};
        db.update("tb_user", cv, whereClauses, whereArgs);

        db.close();
    }



    public void deleteUser(int id){

        UserDBHelper dbHelper = new UserDBHelper(TableUserActivity.this, "tb_user", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String whereClauses = "id=?";
        String[] whereArgs = {String.valueOf(id)};
        db.delete("tb_user", whereClauses, whereArgs);
    }




}
