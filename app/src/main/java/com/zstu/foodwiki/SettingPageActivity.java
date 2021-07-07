package com.zstu.foodwiki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingPageActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_petname;
    EditText et_description;
    Button btn_save;

    String petname;
    String description;
    String new_petname;
    String new_description;


    int userid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Intent intent = getIntent();
        userid = intent.getIntExtra("userid",0);

        getId();
        bindingEvents();

        loadData();
        renderPage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==120){
                    Toast.makeText(this, "用户信息修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                if(getSettingChanges()){
                    petname = new_petname;
                    description = new_description;
                    Intent intent = new Intent(SettingPageActivity.this,TableUserInfoActivity.class);
                    intent.putExtra("userid",userid);
                    intent.putExtra("operation", TableUserInfoActivity.UPDATE);
                    intent.putExtra("new_name", petname);
                    intent.putExtra("new_remark", description);
                    startActivityForResult(intent,1);
                    //finish();
                }
                break;
            default:
                break;
        }
    }

    public boolean getSettingChanges(){
        new_petname = et_petname.getText().toString();
        new_description = et_description.getText().toString();
        return true;
    }

    public void getId(){
        et_petname = findViewById(R.id.et_petname);
        et_description = findViewById(R.id.et_descripiton);
        btn_save = findViewById(R.id.btn_save);
    }


    public void bindingEvents(){
        btn_save.setOnClickListener(this);
    }

    public void loadData(){
        petname = this.getString(R.string.item_1_default);
        description = this.getString(R.string.item_2_default);
    }

    public void renderPage(){

    }
}
