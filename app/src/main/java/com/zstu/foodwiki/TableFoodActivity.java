package com.zstu.foodwiki;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TableFoodActivity extends AppCompatActivity {

    public static final int INSERT = 1;
    public static final int QUERY_ALL = 2;

    private int pk_id;
    private int user_id;
    private int file_id;
    private String title;
    private String contentdetails;
    private String selfcomment;
    private String phonenumber;
    private int likes;
    private int stars;
    private int shares;
    private int reads;

   // private String bloger;
   // private byte[] foodbin;

    private List<FoodEntity> foodEntityList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Intent intent = getIntent();
        int op = intent.getIntExtra("operation",0);

        switch (op){
            case INSERT:
                user_id = intent.getIntExtra("userid", -1);
                title = intent.getStringExtra("title");
                contentdetails = intent.getStringExtra("contentdetails");
                selfcomment = intent.getStringExtra("selfcomment");
                phonenumber = intent.getStringExtra("phonenumber");
                insertFoodInfo(true);
                Intent intent1 = new Intent();
                setResult(120,intent1);
                finish();
                break;
            case QUERY_ALL:
                int count = intent.getIntExtra("count", 10);
                if(queryAll(count)){
                    Intent intent2 = new Intent();
                    //Bundle bundle = new Bundle();
                    //bundle.putSerializable("serial",foodEntityList);
                    intent2.putExtra("objectList", (Serializable) foodEntityList);
                    setResult(130,intent2);
                    finish();
                }
                break;
                default:
                    break;
        }
    }


 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                    if(resultCode==290){
                        bloger = data.getStringExtra("name");
                        foodEntityList.get(0).setBloger(bloger);
                    }
            case 2:
                    if(resultCode==300) {
                        foodbin = data.getByteArrayExtra("figure_bin");
                        foodEntityList.get(0).setFoodbin(foodbin);
                    }
        }
    }*/


    public long insertFoodInfo(boolean setDefalutValue){
       /* FoodDBHelper dbHelper = new FoodDBHelper(TableFoodActivity.this, "tb_food", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(FoodDBHelper.dbPath,null);

        ContentValues cv = new ContentValues();

        if(setDefalutValue){
            cv.put("userid", user_id);
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
            cv.put("userid", user_id);
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


    public boolean queryAll(int count){
        /*FoodDBHelper dbHelper = new FoodDBHelper(TableFoodActivity.this, "tb_food", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(FoodDBHelper.dbPath,null);

        Cursor cursor = db.query("tb_food", null, null,null, null, null, "id");
        foodEntityList = new ArrayList<FoodEntity>();
        while(cursor.moveToNext() && count!=0){
            pk_id = cursor.getInt(cursor.getColumnIndex("id"));
            user_id = cursor.getInt(cursor.getColumnIndex("userid"));
            file_id = cursor.getInt(cursor.getColumnIndex("fileid"));

            title = cursor.getString(cursor.getColumnIndex("title"));
            contentdetails = cursor.getString(cursor.getColumnIndex("contentdetails"));
            selfcomment = cursor.getString(cursor.getColumnIndex("selfcomment"));
            phonenumber = cursor.getString(cursor.getColumnIndex("phonenumber"));
            likes =  cursor.getInt(cursor.getColumnIndex("likes"));
            stars = cursor.getInt(cursor.getColumnIndex("stars"));
            shares = cursor.getInt(cursor.getColumnIndex("shares"));
            reads =  cursor.getInt(cursor.getColumnIndex("reads"));

           /* Intent intent = new Intent(TableFoodActivity.this,TableUserInfoActivity.class);
            intent.putExtra("userid", user_id);
            intent.putExtra("operation", TableUserInfoActivity.GET_ALL);
            startActivityForResult(intent,1);

            Intent intent2 = new Intent(TableFoodActivity.this,TableFileActivity.class);
            intent2.putExtra("figureid", file_id);
            intent2.putExtra("operation", TableFileActivity.GET_BIN);
            startActivityForResult(intent2,2);*/

            FoodEntity foodEntity = new FoodEntity(pk_id,user_id,file_id,title,contentdetails,selfcomment,phonenumber,likes, stars, shares, reads);
            foodEntityList.add(foodEntity);

            count--;
        }
        db.close();
        return  true;
    }
}
