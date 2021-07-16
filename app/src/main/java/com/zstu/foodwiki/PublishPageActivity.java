package com.zstu.foodwiki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PublishPageActivity extends AppCompatActivity implements View.OnClickListener  {

    Button btn_publish;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        getId();
        bindingEvents();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirmpublish:
                finish();
                break;
            default:
                break;
        }
    }


    public void getId(){
        btn_publish = findViewById(R.id.btn_confirmpublish);
    }

    public void bindingEvents(){
        btn_publish.setOnClickListener(this);
    }
}
