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

public class LoginActivity extends AppCompatActivity implements  View.OnClickListener{

    private EditText edit_username;
    private EditText edit_password;
    private Button btn_to_register;
    private Button btn_login;

    private boolean doesUsernameExist = false;
    private String realPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getId();
        bindingEvents();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==200){

                    doesUsernameExist = data.getBooleanExtra("doesExist",false);
                    if(!doesUsernameExist){
                        Toast.makeText(this,"用户不存在",Toast.LENGTH_SHORT).show();
                    }

                }
            case 2:
                if(resultCode==200){
                    realPassword = data.getStringExtra("password");
                    if(realPassword==null){
                        realPassword = "123456";
                    }
                }
        }
    }

    public void getId(){

        edit_username = findViewById(R.id.et_account);
        edit_password = findViewById(R.id.et_password_login);
        btn_to_register = findViewById(R.id.btn_to_register);
        btn_login = findViewById(R.id.btn_login);
    }

    private  void bindingEvents(){

        btn_to_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_to_register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_login:
                if(doesUsernameExist()){
                    if(TextUtils.isEmpty(edit_password.getText().toString().trim())){
                        Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_LONG).show();
                        edit_password.requestFocus();
                    }
                    else{
                        String username = edit_username.getText().toString().trim();
                        String password = edit_password.getText().toString().trim();
                        Intent intent_user = new Intent(LoginActivity.this,TableUserActivity.class);
                        intent_user.putExtra("username", username);
                        intent_user.putExtra("operation", 3);
                        startActivityForResult(intent_user,2);
                        if(password.equals(realPassword)){
                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                            Intent newIntent = new Intent(LoginActivity.this,HomePageActivity.class);
                            startActivity(newIntent);
                            finish();
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"用户密码错误",Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this,"password: "+ password + " realPassword: "+ realPassword,Toast.LENGTH_LONG).show();
                        }
                    }
                }

                break;
        }
    }

    public boolean doesUsernameExist(){
        if(TextUtils.isEmpty(edit_username.getText().toString().trim()))
        {
            Toast.makeText(LoginActivity.this,"请输入用户名",Toast.LENGTH_LONG).show();
            edit_username.requestFocus();
            return false;
        }
        else{
            String username = edit_username.getText().toString().trim();
            Intent intent_user = new Intent(LoginActivity.this,TableUserActivity.class);
            intent_user.putExtra("username", username);
            intent_user.putExtra("operation", 2);
            startActivityForResult(intent_user,1);
            return doesUsernameExist;
        }
    }
}
