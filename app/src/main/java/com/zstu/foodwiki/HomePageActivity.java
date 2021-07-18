package com.zstu.foodwiki;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener{

    public static  final  String TAG = "TEST_HomePageActivity";
    public static final int LATEST_FOOD = 0;
    public static final int MY_FOLLOW = 1;
    public static final int MY_STAR = 2;
    public static final int MY_LIKE = 3;
    public static final int MY_COMMENT = 4;


    TextView tv_name;
    TextView tv_uid;
    TextView tv_remark;
    TextView tv_follows;
    TextView tv_followers;
    TextView tv_readers;
    ImageView iv_figure;
    ImageButton ibtn_setting;
    ImageButton ibtn_publish;

    private  int userid;
    private int figureid;
    private byte[] figure_bin;
    private String username;
    private String name;
    private String remark;
    private int follows;
    private int followers;
    private int readers;

    private List<FoodEntity> mData;
    private int mData_current_pos;


    private boolean isFigureEmpty = false;
    private boolean isFoodEmpty = false;


    public void InsertDefaultFigure(){
        Intent intent = new Intent(HomePageActivity.this, TableFileActivity.class);
        intent.putExtra("operation", TableFileActivity.INSERT_FIGURE);
        startActivity(intent);
        finish();
    }

    public void InsertDefaultFood(){
        Intent intent = new Intent(HomePageActivity.this, TableFileActivity.class);
        intent.putExtra("operation", TableFileActivity.INSERT_FOOD);
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
        if(isFigureEmpty){
            InsertDefaultFigure();
        }
        if(isFoodEmpty){
            InsertDefaultFood();
        }
        /*********A Small Trick End**********/

        loadData();
        //processData();


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

            case 3:
                if(resultCode == 130){
                    mData = (List<FoodEntity>)data.getSerializableExtra("objectList");

                   /***************访问数据库：加载美食推送信息(推送者)***************/
                    for(int i = 0 ; i < mData.size(); i++){
                        mData_current_pos = i;
                        int userid = mData.get(mData_current_pos).getFk_user_id();
                        // mData.get(mData_current_pos).setBloger(userid+"");
                        Intent intent4 = new Intent(HomePageActivity.this,TableUserInfoActivity.class);
                        intent4.putExtra("userid", userid);
                        intent4.putExtra("operation", TableUserInfoActivity.GET_ALL);
                        startActivityForResult(intent4,4);

                    }

                    /***************访问数据库：加载美食推送信息(美食封面)***************/
                    for(int i = 0 ; i < mData.size(); i++){
                        mData_current_pos = i;
                        int fileid = mData.get(mData_current_pos).getFk_file_id();
                        Intent intent5 = new Intent(HomePageActivity.this,TableFileActivity.class);
                        intent5.putExtra("figureid", fileid);
                        intent5.putExtra("operation", TableFileActivity.GET_BIN);
                        startActivityForResult(intent5,5);

                    }

                    initView();

                    //Log.d(TAG, "mData size  --->> "+mData.size());
                }

            case 4:
                if(resultCode==290){
                    String mData_bloger= data.getStringExtra("name");
                    mData.get(mData_current_pos).setBloger(mData_bloger);

                    //  mData_username = "???";
                }

            case 5:
                if(resultCode==300){
                    byte[] mData_food = data.getByteArrayExtra("figure_bin");
                    mData.get(mData_current_pos).setFoodbin(mData_food);
                   /* if(mData_current_pos==mData.size()){
                        initView();
                    }*/
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
            case R.id.ibtn_publish:
                Intent intent2 = new Intent(HomePageActivity.this,PublishPageActivity.class);
                intent2.putExtra("userid", userid);
                startActivity(intent2);
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
        ibtn_publish = findViewById(R.id.ibtn_publish);
    }

    public void bindingEvents(){

        ibtn_setting.setOnClickListener(this);
        ibtn_publish.setOnClickListener(this);
    }

    public void loadData(){
        Intent intent = getIntent();
        userid = intent.getIntExtra("userid",0);
        username = intent.getStringExtra("username");

        /*************访问数据库：加载用户信息**************/
        Intent intent2 = new Intent(HomePageActivity.this,TableUserInfoActivity.class);
        intent2.putExtra("userid", userid);
        intent2.putExtra("operation", 1);
        startActivityForResult(intent2,1);

        /*************访问数据库：加载美食推送信息**************/
        Intent intent3 = new Intent(HomePageActivity.this,TableFoodActivity.class);
        intent3.putExtra("count", 20);
        intent3.putExtra("operation", TableFoodActivity.QUERY_ALL);
        startActivityForResult(intent3, 3);



    }

    public void processData(){
        Intent intent = new Intent(HomePageActivity.this, TableFileActivity.class);
        intent.putExtra("figureid", figureid);
        intent.putExtra("operation", TableFileActivity.GET_BIN);
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
                  int pos = tab.getPosition();
                  setScrollViewContent(pos);

              }

               @Override
               public void onTabUnselected(TabLayout.Tab tab) {

              }

              @Override
              public void onTabReselected(TabLayout.Tab tab) {

              }
        });
        setScrollViewContent(0);
    }

    public void setScrollViewContent(int pos){
        LinearLayout layout = findViewById(R.id.ll_sc_content);
        layout.removeAllViews();


        switch (pos){
            case LATEST_FOOD:
                for (int i = 0; i < mData.size(); i++) {
                    View view = View.inflate(HomePageActivity.this, R.layout.item_food, null);

                   /* byte[] food_bin = mData.get(i).getFoodbin();
                    Bitmap food_bmp = BitmapFactory.decodeByteArray(food_bin, 0, food_bin.length);
                    ((ImageView) view.findViewById(R.id.iv_food)).setImageBitmap(food_bmp);*/

                    ((TextView) view.findViewById(R.id.tv_bloger)).setText(mData.get(i).getBloger());

                    ((TextView) view.findViewById(R.id.tv_title)).setText(mData.get(i).getTitle());
                    ((TextView) view.findViewById(R.id.tv_content)).setText('"'+mData.get(i).getSelfcomment()+'"');
                    ((TextView) view.findViewById(R.id.tv_phonenumber)).setText(mData.get(i).getPhonenumber());
                    ((TextView) view.findViewById(R.id.tv_like)).setText(mData.get(i).getLikes()+"");
                    ((TextView) view.findViewById(R.id.tv_share)).setText(mData.get(i).getShares()+"");
                    ((TextView) view.findViewById(R.id.tv_star)).setText(mData.get(i).getStars()+"");



                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(HomePageActivity.this,DetailPageActivity.class);
                            startActivity(intent);
                        }
                    });
                    layout.addView(view, i);
                }
                break;
            case MY_FOLLOW:
                for (int i = 0; i < /*mData.size()*/2; i++) {
                View view = View.inflate(HomePageActivity.this, R.layout.item_bloger, null);
                //  ((TextView) view.findViewById(R.id.tv_info)).setText(mData.get(i));
                layout.addView(view, i);
            }
                break;
            case MY_STAR:
                    for (int i = 0; i < /*mData.size()*/4; i++) {
                        View view = View.inflate(HomePageActivity.this, R.layout.item_food, null);
                        //  ((TextView) view.findViewById(R.id.tv_info)).setText(mData.get(i));
                        layout.addView(view, i);
                    }
                break;
            case MY_LIKE:
                    for (int i = 0; i < /*mData.size()*/5; i++) {
                        View view = View.inflate(HomePageActivity.this, R.layout.item_food, null);
                        //  ((TextView) view.findViewById(R.id.tv_info)).setText(mData.get(i));
                        layout.addView(view, i);
                    }
                break;
            case MY_COMMENT:
                for (int i = 0; i < /*mData.size()*/7; i++) {
                    View view = View.inflate(HomePageActivity.this, R.layout.item_comment, null);
                    //  ((TextView) view.findViewById(R.id.tv_info)).setText(mData.get(i));
                    layout.addView(view, i);
                }
                break;
            default:
                break;

        }


    }
}
