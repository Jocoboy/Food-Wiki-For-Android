package com.zstu.foodwiki;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener{

    public static  final  String TAG = "TEST_HomePageActivity";

    TextView tv_name;
    TextView tv_uid;
    TextView tv_remark;
    TextView tv_follows;
    TextView tv_followers;
    TextView tv_readers;
    ImageView iv_figure;
    ImageButton ibtn_setting;

    private  int userid;
    private int figureid;
    private byte[] figure_bin;
    private String username;
    private String name;
    private String remark;
    private int follows;
    private int followers;
    private int readers;

    private List<String> mData;



    private boolean isFileTableEmpty = false;

    public void InsertDegaultFigure(){
        Intent intent = new Intent(HomePageActivity.this,FileActivity.class);
        intent.putExtra("operation", FileActivity.INSERT);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getId();
        bindingEvents();

        /*********A Small Trick Begin**********/
        if(isFileTableEmpty){
            InsertDegaultFigure();
        }
        /*********A Small Trick End**********/

        loadData();
        //processData();

        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==290){
                    name = data.getStringExtra("name");
                    remark = data.getStringExtra("remark");
                    follows =  data.getIntExtra("follows", 0);
                    followers = data.getIntExtra("followers", 0);
                    readers = data.getIntExtra("readers", 0);
                    figureid = data.getIntExtra("figureid", -1);
                   // System.out.println("onActivityResult -- name: "+ name);
                    Log.d(TAG, "figureid --->> "+figureid);
                    processData();
                   //renderPage();
                }
            case 2:
                if(resultCode==300){
                    figure_bin = data.getByteArrayExtra("figure_bin");
                    renderPage();
                }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtn_setting:
                Intent intent = new Intent(HomePageActivity.this,SettingPageActivity.class);
                intent.putExtra("userid",userid);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }

    public void getId(){

        tv_name = findViewById(R.id.tv_name);
        tv_uid = findViewById(R.id.tv_uid);
        tv_remark = findViewById(R.id.tv_remark);
        tv_follows = findViewById(R.id.tv_follows);
        tv_followers = findViewById(R.id.tv_followers);
        tv_readers = findViewById(R.id.tv_readers);
        iv_figure = findViewById(R.id.iv_figure);
        ibtn_setting = findViewById(R.id.ibtn_setting);
    }

    public void bindingEvents(){
        ibtn_setting.setOnClickListener(this);
    }

    public void loadData(){
        Intent intent = getIntent();
        userid = intent.getIntExtra("userid",0);
        username = intent.getStringExtra("username");

        Intent intent2 = new Intent(HomePageActivity.this,TableUserInfoActivity.class);
        intent2.putExtra("userid", userid);
        intent2.putExtra("operation", 1);
        startActivityForResult(intent2,1);


    }

    public void processData(){
        Intent intent = new Intent(HomePageActivity.this,FileActivity.class);
        intent.putExtra("figureid", figureid);
        intent.putExtra("operation", FileActivity.GET_BIN);
        startActivityForResult(intent, 2);
    }

    public void renderPage(){
        tv_name.setText(name);
        tv_uid.setText("@"+username);
        tv_remark.setText(remark);
        tv_follows.setText(String.valueOf(follows));
        tv_followers.setText(String.valueOf(followers));
        tv_readers.setText(String.valueOf(readers));

        //System.out.println("figure byte[] --->> "+figure_bin);
        Log.d(TAG, "figure byte[] --->> "+figure_bin);
        Bitmap figure_bmp = BitmapFactory.decodeByteArray(figure_bin, 0, figure_bin.length);
        iv_figure.setImageBitmap(figure_bmp);

    }


    public void initView(){
        TabLayout tabLayout = findViewById(R.id.toolbar_tab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
              @Override
              public void onTabSelected(TabLayout.Tab tab) {
                  setScrollViewContent();
              }

               @Override
               public void onTabUnselected(TabLayout.Tab tab) {

              }

              @Override
              public void onTabReselected(TabLayout.Tab tab) {

              }
        });
        setScrollViewContent();
    }

    public void setScrollViewContent(){
        LinearLayout layout = findViewById(R.id.ll_sc_content);
        layout.removeAllViews();
        for (int i = 0; i < /*mData.size()*/6; i++) {
            View view = View.inflate(HomePageActivity.this, R.layout.item_layout, null);
          //  ((TextView) view.findViewById(R.id.tv_info)).setText(mData.get(i));
            layout.addView(view, i);
        }
    }
}
