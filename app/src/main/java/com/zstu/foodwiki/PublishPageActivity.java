package com.zstu.foodwiki;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PublishPageActivity extends AppCompatActivity implements View.OnClickListener  {

    Button btn_publish;
    EditText et_title;
    EditText et_content;
    EditText et_comment;
    EditText et_phone;

    private String title;
    private String content_details;
    private String self_comment;
    private String phone_number;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        getId();
        bindingEvents();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == 120) {
                    Toast.makeText(this, "美食发布成功！", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirmpublish:
                if(getPublishInfo()){

                    Intent intent = new Intent(PublishPageActivity.this,TableFoodActivity.class);
                    intent.putExtra("operation", TableFoodActivity.INSERT);
                    intent.putExtra("title",title);
                    intent.putExtra("contentdetails", content_details);
                    intent.putExtra("selfcomment", self_comment);
                    intent.putExtra("phonenumber", phone_number);
                    startActivityForResult(intent,1);
                 //   finish();
                }
                else{
                    Toast.makeText(PublishPageActivity.this,"美食名称、内容详情、个人点评、联系方式均为必填项！",Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }
    }

    public boolean getPublishInfo(){
        title = et_title.getText().toString();
        content_details = et_content.getText().toString();
        self_comment = et_comment.getText().toString();
        phone_number = et_phone.getText().toString();
        if(TextUtils.isEmpty(title.trim()) || TextUtils.isEmpty(content_details.trim()) || TextUtils.isEmpty(self_comment.trim()) || TextUtils.isEmpty(phone_number.trim())){

            return false;
        }

        return true;
    }

    public void getId(){
        btn_publish = findViewById(R.id.btn_confirmpublish);
        et_title = findViewById(R.id.et_foodname);
        et_content = findViewById(R.id.et_contentdetails);
        et_comment = findViewById(R.id.et_comment);
        et_phone = findViewById(R.id.et_phonenumber_publish);
    }

    public void bindingEvents(){
        btn_publish.setOnClickListener(this);
    }
}
