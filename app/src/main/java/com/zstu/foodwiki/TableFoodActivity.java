package com.zstu.foodwiki;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TableFoodActivity extends AppCompatActivity {

    public static final int INSERT = 1;


    private int pk_id;
    private int file_id;
    private String title;
    private String contentdetails;
    private String selfcomment;
    private String phonenumber;
    private int likes;
    private int stars;
    private int shares;
    private int reads;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int op = intent.getIntExtra("operation",0);

        switch (op){
            case INSERT:
                title = intent.getStringExtra("title");
                contentdetails = intent.getStringExtra("contentdetails");
                selfcomment = intent.getStringExtra("selfcomment");
                phonenumber = intent.getStringExtra("phonenumber");
                insertFoodInfo(true);
                Intent intent1 = new Intent();
                setResult(120,intent1);
                finish();
                break;
                default:
                    break;
        }
    }



    public long insertFoodInfo(boolean setDefalutValue){
        FoodDBHelper dbHelper = new FoodDBHelper(TableFoodActivity.this, "tb_food", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues cv = new ContentValues();

        if(setDefalutValue){
            cv.put("fileid", 2);
            cv.put("title", title);
            cv.put("contentdetails", contentdetails);
            cv.put("selfcomment", selfcomment);
            cv.put("phonenumber", phonenumber);
            cv.put("likes", 0);
            cv.put("stars", 0);
            cv.put("shares", 0);
            cv.put("reads", 0);
        }
        else{
            cv.put("fileid", file_id);
            cv.put("title", title);
            cv.put("contentdetails", contentdetails);
            cv.put("selfcomment", selfcomment);
            cv.put("phonenumber", phonenumber);
            cv.put("likes", likes);
            cv.put("stars", stars);
            cv.put("shares", shares);
            cv.put("reads", reads);
        }

        long resid =  db.insert("tb_food", null, cv);


        db.close();
        return resid;
    }
}
