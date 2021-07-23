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

public class TableCommentActivity extends AppCompatActivity {

    public static final int INSERT= 1;
    public static final int QUERY_BY_TARGETID = 2;

    private int pk_id;
    private int userid;
    private int foodid;
    private int targetuserid;
    private String comment;
    private String dt;

    private CommentEntity commentEntity;
    private List<CommentEntity> commentEntityList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int op = intent.getIntExtra("operation", 0);

        switch (op) {
            case INSERT:
                commentEntity = (CommentEntity)intent.getSerializableExtra("commentEntity");
                if(commentEntity!=null){
                    insertComment();
                }
                break;
            case QUERY_BY_TARGETID:
                targetuserid = intent.getIntExtra("userid", -1);

                if(queryAllByTargetId()){
                    Intent intent1 = new Intent();
                    intent1.putExtra("commentEntityList", (Serializable) commentEntityList);
                    setResult(233,intent1);
                    finish();
                }

                break;
            default:
                break;
        }

    }

    public boolean queryAllByTargetId(){
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(CommentDBHelper.dbPath,null);

        String selection = "tageruserid=?";
        String[] selectionArgs = new String[]{String.valueOf(targetuserid)};
        Cursor cursor = db.query("tb_comment", null, selection,selectionArgs, null, null, "dt");
        commentEntityList = new ArrayList<CommentEntity>();
        while(cursor.moveToNext()){
            pk_id = cursor.getInt(cursor.getColumnIndex("id"));
            userid = cursor.getInt(cursor.getColumnIndex("userid"));
            foodid = cursor.getInt(cursor.getColumnIndex("foodid"));
            comment = cursor.getString(cursor.getColumnIndex("comment"));
            dt = cursor.getString(cursor.getColumnIndex("dt"));

            CommentEntity commentEntity = new CommentEntity(pk_id, userid, foodid, targetuserid, comment, dt);
            commentEntityList.add(commentEntity);
        }
        db.close();

        return true;
    }


    public void insertComment(){
        /*FoodLikeDBHelper dbHelper = new FoodLikeDBHelper(TableFoodLikeActivity.this, "tb_foodlike", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(CommentDBHelper.dbPath,null);

        ContentValues cv = new ContentValues();

        cv.put("id", commentEntity.getPk_id());
        cv.put("userid", commentEntity.getFk_user_id());
        cv.put("foodid", commentEntity.getFk_food_id());
        cv.put("comment", commentEntity.getComment());
        cv.put("dt", commentEntity.getDt());
        db.insert("tb_comment", null, cv);

        db.close();
    }
}
