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
    public static  final int CHECK_CURSOR_EXIST = 2;
    public static final  int GET_PASSWORD = 3;
    private static int curId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int op = intent.getIntExtra("operation",0);
        String username;
        String password;
        switch (op){
            case INSERT:
                username = intent.getStringExtra("username");
                password = intent.getStringExtra("password");
                InsertUser(curId,username,password);
                Intent intent1 = new Intent();
                intent1.putExtra("doesRegisterSuccess", true);
                setResult(200,intent1);
                finish();
                break;

            case CHECK_CURSOR_EXIST:
                username = intent.getStringExtra("username");
                boolean doesExist = checkColumnExist(username);
                Intent intent2 = new Intent();
                intent2.putExtra("doesExist", doesExist);
                setResult(200, intent2);
                finish();
                break;
            case GET_PASSWORD:
                username = intent.getStringExtra("username");
                password = queryUserPassword(username);
                Intent intent3 = new Intent();
                intent3.putExtra("password", password);
                setResult(200, intent3);
                finish();
                break;
                default:
                    break;

        }
    }


    public boolean checkColumnExist(String username) {

        UserDBHelper dbHelper = new UserDBHelper(TableUserActivity.this, "tb_user", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.beginTransaction();
        boolean res = false;
        Cursor cursor = null;
        try {
            cursor = db.query("tb_user", null, "username=?", new String[]{username}, null, null, null);
            if(cursor!=null && cursor.getCount()!=0){
                res = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            if(cursor!=null && !cursor.isClosed()){
                    cursor.close();
            }
        }
        return  res;
    }

    public void queryUser(){

        UserDBHelper dbHelper = new UserDBHelper(TableUserActivity.this, "tb_user", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("tb_user", new String[]{"id","username","password"}, "username=?", new String[]{"1"}, null, null, null);
        while(cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            System.out.println("query------->" + "id："+id+" "+"username："+username+" "+"password："+password);
        }

        db.close();
    }


    public  String queryUserPassword(String username){

        UserDBHelper dbHelper = new UserDBHelper(TableUserActivity.this, "tb_user", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String password="";
        Cursor cursor = db.query("tb_user", new String[]{"id","username","password"}, "username=?", new String[]{username}, null, null, null);
        /*if(cursor.getCount()==0){
            password="???";
        }
        else{*/
            if((cursor.moveToFirst())){
                password = cursor.getString(cursor.getColumnIndex("password"));
            }
        /*}*/
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
