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

    private int pk_id;
    private String username;
    private String password;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int op = intent.getIntExtra("operation",0);

        switch (op){
            case INSERT:
                username = intent.getStringExtra("username");
                password = intent.getStringExtra("password");
                curId = InsertUser(/*curId,*/username,password);
                //curId++;
                Intent intent1 = new Intent();
                intent1.putExtra("doesRegisterSuccess", true);
                setResult(190,intent1);
                finish();

                /******************级联操作****************/
                Intent intent_cascade = new Intent(TableUserActivity.this,TableUserInfoActivity.class);
                intent_cascade.putExtra("operation", TableUserInfoActivity.INSERT);
                intent_cascade.putExtra("userid", curId);
                startActivity(intent_cascade);

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
                intent3.putExtra("id", pk_id);
                System.out.println("in case GET_PASSWORD " +"password："+password);
                setResult(210, intent3);
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




    public  String queryUserPassword(String username){

        UserDBHelper dbHelper = new UserDBHelper(TableUserActivity.this, "tb_user", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String password="";
        Cursor cursor = db.query("tb_user", new String[]{"password","id"}, "username=?", new String[]{username}, null, null, null);
        /*if(cursor.getCount()==0){
            password="???";
        }s
        else{*/
           if((cursor.moveToFirst())){
                password = cursor.getString(cursor.getColumnIndex("password"));
                pk_id = cursor.getInt(cursor.getColumnIndex("id"));
               // System.out.println("query------->" +"password："+password);
            }
          // password = cursor.moveToNext()+"";
        /*}*/
        db.close();
        return password;
    }

    public int InsertUser(/*int id,*/String username,String password){

        UserDBHelper dbHelper = new UserDBHelper(TableUserActivity.this, "tb_user", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues cv = new ContentValues();
        //cv.put("id", id);
        cv.put("username", username);
        cv.put("password", password);
        db.insert("tb_user", null, cv);

        int insert_id = -1;
        Cursor cursor = db.rawQuery("select last_insert_rowid() from tb_user", null);
        if(cursor.moveToFirst()){
            insert_id = cursor.getInt(0);
        }

        db.close();
        return insert_id;
    }

    public void queryUser(int id){

        UserDBHelper dbHelper = new UserDBHelper(TableUserActivity.this, "tb_user", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("tb_user", new String[]{"id","username","password"}, "username=?", new String[]{String.valueOf(id)}, null, null, null);
        while(cursor.moveToNext()){
            //String id = cursor.getString(cursor.getColumnIndex("id"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            System.out.println("query------->" + "id："+id+" "+"username："+username+" "+"password："+password);
        }

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
