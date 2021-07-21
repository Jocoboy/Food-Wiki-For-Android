package com.zstu.foodwiki;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.io.ByteArrayOutputStream;

public class TableFileActivity extends AppCompatActivity {


    public static final int INSERT_FIGURE = 1;
    public static final int GET_BIN = 2;
    public static final int INSERT_FOOD = 3;
    public static final int INSERT_GENERAL = 4;

    public static final int TYPE_GENERAL= 200;
    public static final int TYPE_FIGURE = 201;
    public static final int TYPE_FOOD = 202;

    private  int pk_id;
    private String name;
    private String ext;
    private byte[] img;
    private String path;
    private String description;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int op = intent.getIntExtra("operation",0);

        switch (op) {
            case INSERT_FIGURE:
                insertFile(TYPE_FIGURE);
                break;
            case INSERT_FOOD:
                insertFile(TYPE_FOOD);
                break;
            case INSERT_GENERAL:
                insertFile(TYPE_GENERAL);
                break;
            case GET_BIN:
                int figureid = intent.getIntExtra("figureid",-1);
                int pos = intent.getIntExtra("mData_current_pos",-1);
                byte[] figure_bin = getBytes(figureid);
                Intent intent1 = new Intent();
                intent1.putExtra("figure_bin", figure_bin);
                if(pos!=-1){
                    intent1.putExtra("mData_current_pos", pos);
                }
                setResult(300, intent1);
                finish();
                break;
                default:
                    break;
        }
    }

    public byte[] ConvertDrawableToBytes(int id){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable)ResourcesCompat.getDrawable(getResources(),id, null)).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public byte[] getBytes(long id){
        /*FileDBHelper dbHelper = new FileDBHelper(TableFileActivity.this, "tb_file", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(FileDBHelper.dbPath,null);

        Cursor cursor = db.query("tb_file", null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        byte[] bin = null;
        if((cursor.moveToFirst())){
            bin = cursor.getBlob(cursor.getColumnIndex("bin"));
        }
        db.close();
        return bin;
    }

    public long insertFile(int type){
        /*FileDBHelper dbHelper = new FileDBHelper(TableFileActivity.this, "tb_file", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(FileDBHelper.dbPath,null);

        ContentValues cv = new ContentValues();

        if(type == TYPE_FIGURE){
            img = ConvertDrawableToBytes(R.drawable.img_figure_default);
            cv.put("name", "默认头像");
            cv.put("extension", "png");
            cv.put("bin", img);
         //   cv.put("path", "null");
            cv.put("descripiton", "用户尚未上传个人头像，使用默认头像");
        }
        else if(type == TYPE_FOOD){
            img = ConvertDrawableToBytes(R.drawable.img_food_default);
            cv.put("name", "默认封面");
            cv.put("extension", "png");
            cv.put("bin", img);
          //  cv.put("path", "null");
            cv.put("descripiton", "用户尚未上传美食封面，使用默认封面");
        }
        else{
            img = ConvertDrawableToBytes(R.drawable.img_figure_4);
            cv.put("name", "个人头像4");
            cv.put("extension", "jpg");
            cv.put("bin", img);
         //   cv.put("path", path);
            cv.put("descripiton", "用户上传个人头像之一");
        }
        long resid = db.insert("tb_file", null, cv);

        db.close();
        return  resid;
    }
}
