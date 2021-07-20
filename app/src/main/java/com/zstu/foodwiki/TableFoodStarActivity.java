package com.zstu.foodwiki;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TableFoodStarActivity extends AppCompatActivity {

    public static final int INSERT = 1;

    private FoodStarEntity foodStarEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int op = intent.getIntExtra("operation", 0);

        switch (op){
            case INSERT:
                foodStarEntity = (FoodStarEntity)intent.getSerializableExtra("foodStarEntity");
                if(foodStarEntity!=null){
                    insertFoodStar();
                }

                break;
            default:
                break;
        }
    }


    public void insertFoodStar(){
        FoodLikeDBHelper dbHelper = new FoodLikeDBHelper(TableFoodStarActivity.this, "tb_foodstar", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues cv = new ContentValues();
        //cv.put("id", id);
        cv.put("userid", foodStarEntity.getFk_user_id());
        cv.put("foodid", foodStarEntity.getFk_food_id());
        db.insert("tb_foodstar", null, cv);

        db.close();
    }
}
