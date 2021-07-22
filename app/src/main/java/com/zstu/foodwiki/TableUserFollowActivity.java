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

public class TableUserFollowActivity extends AppCompatActivity {

    public static final int INSERT = 1;
    public static final int QUERY = 2;

    private int pk_id;
    private int user_id;
    private int follow_id;
    private boolean isCancelFollow;
    private UserFollowEntity userFollowEntity;
    private List<UserFollowEntity> userFollowEntityList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int op = intent.getIntExtra("operation",0);

        switch (op){
            case INSERT:
                userFollowEntity = (UserFollowEntity)intent.getSerializableExtra("UserFollowEntity");
                insertUserFollow();
                break;
            case QUERY:
                user_id = intent.getIntExtra("userid", -1);
                if(queryById()){
                    Intent intent2 = new Intent();
                    intent2.putExtra("userFollowEntityList", (Serializable) userFollowEntityList);
                    setResult(200,intent2);
                    finish();
                }
                break;
                default:
                    break;
        }
    }

    public boolean queryById(){
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(UserFollowDBHelper.dbPath,null);

        Cursor cursor = db.query("tb_userfollow", null, null,null, null, null, "id");
        userFollowEntityList = new ArrayList<UserFollowEntity>();
        while (cursor.moveToNext()){
            isCancelFollow = (cursor.getInt(cursor.getColumnIndex("iscancelfollow")) == 0 ? false:true);
            if(!isCancelFollow){
                pk_id = cursor.getInt(cursor.getColumnIndex("id"));
                user_id = cursor.getInt(cursor.getColumnIndex("userid"));
                follow_id = cursor.getInt(cursor.getColumnIndex("followid"));
                UserFollowEntity userFollowEntity = new UserFollowEntity(pk_id,user_id,follow_id,isCancelFollow);
                userFollowEntityList.add(userFollowEntity);
            }
        }
        db.close();
        return  true;
    }

    public long insertUserFollow(){
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(UserFollowDBHelper.dbPath,null);

        ContentValues cv = new ContentValues();

        cv.put("userid", userFollowEntity.getFk_user_id());
        cv.put("followid", userFollowEntity.getFk_follow_id());
        cv.put("iscancelfollow", userFollowEntity.getIsCancelFollow());

        long resid =  db.insert("tb_userfollow", null, cv);

        db.close();
        return resid;
    }
}
