package com.zstu.foodwiki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Message;

import com.mob.MobSDK;
import com.mob.OperationCallback;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity implements  View.OnClickListener{

    private EditText edit_phone;
    private EditText edit_code;
    private Button btn_to_login;
    private Button btn_getCode;
    private Button btn_register;
    EventHandler eventHandler;

    private String phone_number;
    private String confirm_code;
    private String TAG;
    //private boolean failToGetCode = true;
    int countdown = 60;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;

            if(msg.what == -1){
                btn_getCode.setText(countdown+ " s");
            }
            else if(msg.what == -2){
                btn_getCode.setText("重新发送");
                btn_getCode.setClickable(true);
                countdown = 60;
            }
            else{
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    Toast.makeText(getApplicationContext(), "验证码已经发送",
                            Toast.LENGTH_LONG).show();
                    /*if(result == SMSSDK.RESULT_COMPLETE) {
                        boolean smart = (Boolean)data;
                        if(smart) {
                            Toast.makeText(getApplicationContext(),"该手机号已经注册过，请重新输入",
                                    Toast.LENGTH_LONG).show();
                            edit_phone.requestFocus();
                            return;
                        }
                    }*/
                }
                else if(result == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE)
                {
                        Toast.makeText(getApplicationContext(), "验证码输入正确",
                                Toast.LENGTH_LONG).show();

                }
                /*else if(failToGetCode){
                    Toast.makeText(getApplicationContext(),"验证码获取失败请重新获取", Toast.LENGTH_LONG).show();
                    edit_phone.requestFocus();
                }
                else if(!failToGetCode){
                    Toast.makeText(getApplicationContext(),"验证码输入错误", Toast.LENGTH_LONG).show();
                }*/
                else if(result == SMSSDK.RESULT_ERROR){
                    try {
                        Throwable throwable = (Throwable) data;
                        throwable.printStackTrace();
                        JSONObject object = new JSONObject(throwable.getMessage());
                        String des = object.optString("detail");//错误描述
                        int status = object.optInt("status");//错误代码
                        if (status > 0 && !TextUtils.isEmpty(des)) {
                            Toast.makeText(RegisterActivity.this, des, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        }

    };

    private void submitPrivacyGrantResult(boolean granted) {
        MobSDK.submitPolicyGrantResult(granted, new OperationCallback<Void>() {
            @Override
            public void onComplete(Void data) {
                Log.d(TAG, "隐私协议授权结果提交：成功");
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "隐私协议授权结果提交：失败");
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getId();
        bindingEvents();

        String appKey = "3384bebc764a2";
        String appSecret = "8a567410c2f6527ffc6e2b2fe612759e";
       // SMSSDK.initSDK(RegisterActivity.this, appKey,appSecret );
        submitPrivacyGrantResult(true);

        eventHandler = new EventHandler(){
            public void afterEvent(int event, int result, Object data) {
                Message msg=new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_to_login:
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_get_confirmCode:
                if(judgePhone()){
                    SMSSDK.getVerificationCode("86",phone_number);
                    //failToGetCode = false;
                    btn_getCode.setClickable(false);
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            for (; countdown > 0; countdown--) {
                                handler.sendEmptyMessage(-1);
                                if (countdown<= 0) {
                                    break;
                                }
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            //倒计时结束执行
                            handler.sendEmptyMessage(-2);
                        }
                    }).start();

                    edit_code.requestFocus();
                }
                //failToGetCode = true;
                break;
            case R.id.btn_register:
                if(judgeCode()){
                    SMSSDK.submitVerificationCode("86",phone_number,confirm_code);

                }

                break;
        }
    }

    private void getId(){
        edit_phone = findViewById(R.id.et_phoneNumber);
        edit_code = findViewById(R.id.et_confirmCode);
        btn_getCode = findViewById(R.id.btn_get_confirmCode);
        btn_to_login = findViewById(R.id.btn_to_login);
        btn_register = findViewById(R.id.btn_register);
    }

    private  void bindingEvents(){
        btn_getCode.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_to_login.setOnClickListener(this);
    }


    private boolean judgePhone()
    {
        if(TextUtils.isEmpty(edit_phone.getText().toString().trim()))
        {
            Toast.makeText(RegisterActivity.this,"请输入您的电话号码",Toast.LENGTH_LONG).show();
            edit_phone.requestFocus();
            return false;
        }
        else if(edit_phone.getText().toString().trim().length()!=11)
        {
            Toast.makeText(RegisterActivity.this,"您的电话号码位数不正确",Toast.LENGTH_LONG).show();
            edit_phone.requestFocus();
            return false;
        }
        else
        {
            phone_number = edit_phone.getText().toString().trim();
            String reg="[1][358]\\d{9}";
            if(phone_number.matches(reg))
                return true;
            else
            {
                Toast.makeText(RegisterActivity.this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    private boolean judgeCode()
    {
        judgePhone();
        if(TextUtils.isEmpty(edit_code.getText().toString().trim()))
        {
            Toast.makeText(RegisterActivity.this,"请输入您的验证码",Toast.LENGTH_LONG).show();
            edit_code.requestFocus();
            return false;
        }
        else if(edit_code.getText().toString().trim().length()!=4)
        {
            Toast.makeText(RegisterActivity.this,"您的验证码位数不正确",Toast.LENGTH_LONG).show();
            edit_code.requestFocus();

            return false;
        }
        else
        {
            confirm_code = edit_code.getText().toString().trim();
            return true;
        }

    }


}
