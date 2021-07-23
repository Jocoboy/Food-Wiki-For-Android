package com.zstu.foodwiki;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TableFoodLikeActivity extends AppCompatActivity {
    public static final int INSERT = 1;

    private FoodLikeEntity foodLikeEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int op = intent.getIntExtra("operation", 0);

        switch (op){
            case INSERT:
                foodLikeEntity = (FoodLikeEntity)intent.getSerializableExtra("foodLikeEntity");
                if(foodLikeEntity!=null){
                    insertFoodLike();
                }

                break;
                default:
                    break;
        }
    }

    public void insertFoodLike(){
        /*FoodLikeDBHelper dbHelper = new FoodLikeDBHelper(TableFoodLikeActivity.this, "tb_foodlike", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(FoodLikeDBHelper.dbPath,null);

        ContentValues cv = new ContentValues();
        //cv.put("id", id);
        cv.put("userid", foodLikeEntity.getFk_user_id());
        cv.put("foodid", foodLikeEntity.getFk_food_id());
        db.insert("tb_foodlike", null, cv);

        db.close();
    }
}
