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

public class FileActivity extends AppCompatActivity {


    public static final int INSERT = 1;
    public static final int GET_BIN = 2;

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
            case INSERT:
                insertFile(true);

                break;
            case GET_BIN:
                int figureid = intent.getIntExtra("figureid",-1);
                byte[] figure_bin = getBytes(figureid);
                Intent intent1 = new Intent();
                intent1.putExtra("figure_bin", figure_bin);
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

    public byte[] getBytes(int id){
        FileDBHelper dbHelper = new FileDBHelper(FileActivity.this, "tb_file", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("tb_file", null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        byte[] bin = null;
        if((cursor.moveToFirst())){
            bin = cursor.getBlob(cursor.getColumnIndex("bin"));
        }
        return bin;
    }

    public long insertFile(boolean setDefalutValue){
        FileDBHelper dbHelper = new FileDBHelper(FileActivity.this, "tb_file", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues cv = new ContentValues();

        if(setDefalutValue){
            img = ConvertDrawableToBytes(R.drawable.figure_default);
            cv.put("name", "默认头像");
            cv.put("extension", "png");
            cv.put("bin", img);
            cv.put("path", "null");
            cv.put("descripiton", "用户初始头像");
        }
        else{
            cv.put("name", name);
            cv.put("extension", ext);
            cv.put("bin", img);
            cv.put("path", path);
            cv.put("descripiton", description);
        }
        long resid = db.insert("tb_file", null, cv);

        db.close();
        return  resid;
    }
}
