package com.zstu.foodwiki;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
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
    //private List<UserEntity> nData;
   // private int nData_current_pos;

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

        //loadData();
        //processData();

        Intent intent = getIntent();
        userid = intent.getIntExtra("userid",0);
        username = intent.getStringExtra("username");

        updateData(1);
        //updateData(2);

    }

    @Override
    protected void onStart() {
        super.onStart();

        updateData(3);


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

                    updateData(2);
                   // processData();
                   //renderPage();

                }
                break;
            case 2:
                if(resultCode==300){
                    figure_bin = data.getByteArrayExtra("figure_bin");
                    renderPage();
                }
                break;
            case 3:
                if(resultCode == 130){
                    mData = (List<FoodEntity>)data.getSerializableExtra("objectList");

                    updateData(4);
                    updateData(5);

                    //Log.d(TAG, "mData size  --->> "+mData.size());

                }
                break;
            case 4:
                if(resultCode==290){
                    /*String mData_bloger= data.getStringExtra("name");
                    mData.get(mData_current_pos).setBloger(mData_bloger);*/

                    int userid = data.getIntExtra("userid", -1);
                    int figureid = data.getIntExtra("figureid", -1);
                    String name  = data.getStringExtra("name");
                    String remark = data.getStringExtra("remark");
                    int follows = data.getIntExtra("follows", -1);
                    int followers = data.getIntExtra("followers", -1);
                    int readers = data.getIntExtra("readers", -1);

                    int pos =  data.getIntExtra("mData_current_pos", -1);

                    UserEntity userEntity = new UserEntity(userid,figureid,name,remark,follows,followers,readers);

                    mData.get(pos).setUserEntity(userEntity);

                    Intent intent6 = new Intent(HomePageActivity.this,TableFileActivity.class);
                    intent6.putExtra("figureid", figureid);
                    intent6.putExtra("operation", TableFileActivity.GET_BIN);
                    intent6.putExtra("mData_current_pos", pos);
                    startActivityForResult(intent6,6);


                    if(pos == mData.size() - 1  ){
                        //Toast.makeText(this, "用户名 "+mData.get(mData_current_pos-1).getUserEntity().getName() +"mData size " + mData.size(), Toast.LENGTH_SHORT).show();
                       // initTabView();
                      // updateData(6);
                    }
                    //  mData_username = "???";
                }
                break;
            case 5:
                if(resultCode==300){
                    byte[] mData_food = data.getByteArrayExtra("figure_bin");
                    int pos =  data.getIntExtra("mData_current_pos", -1);
                    mData.get(pos).setFoodbin(mData_food);
                    if(pos == mData.size() - 1 ){
                       // updateData(6);
                      //initTabView();
                    }
                }
                break;
            case 6:
                if(resultCode==300){
                    byte[] mData_figure = data.getByteArrayExtra("figure_bin");
                    int pos =  data.getIntExtra("mData_current_pos", -1);
                    mData.get(pos).getUserEntity().setFigurebin(mData_figure);
                    if(pos == mData.size() - 1 ){
                        initTabView();
                    }
                }
                break;
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

    }

    public void updateData(int requestCode){
        switch (requestCode){
            case 1:
                /*************访问数据库：加载用户基本信息**************/
                Intent intent = new Intent(HomePageActivity.this,TableUserInfoActivity.class);
                intent.putExtra("userid", userid);
                intent.putExtra("operation", 1);
                startActivityForResult(intent,requestCode);
                break;
            case 2:
                /*************访问数据库：加载用户级联信息（用户头像）**************/
                Intent intent2 = new Intent(HomePageActivity.this, TableFileActivity.class);
                intent2.putExtra("figureid", figureid);
                intent2.putExtra("operation", TableFileActivity.GET_BIN);
                startActivityForResult(intent2, requestCode);
                break;
            case 3:
                /*************访问数据库：加载美食推送基本信息**************/
                Intent intent3 = new Intent(HomePageActivity.this,TableFoodActivity.class);
                intent3.putExtra("count", 20);
                intent3.putExtra("operation", TableFoodActivity.QUERY_ALL);
                startActivityForResult(intent3, requestCode);
                break;
            case 4:
                /***************访问数据库：加载美食推送级联信息(推送者)***************/
                for(int i = 0 ; i < mData.size(); i++){
                  //  mData_current_pos = i;
                    int userid = mData.get(i).getFk_user_id();
                    // mData.get(mData_current_pos).setBloger(userid+"");
                    Intent intent4 = new Intent(HomePageActivity.this,TableUserInfoActivity.class);
                    intent4.putExtra("userid", userid);
                    intent4.putExtra("operation", TableUserInfoActivity.GET_ALL);
                    intent4.putExtra("mData_current_pos", i);
                    startActivityForResult(intent4,requestCode);

                }
                break;
            case 5:
                /***************访问数据库：加载美食推送级联信息(美食封面)***************/
                for(int i = 0 ; i < mData.size(); i++){
                   // mData_current_pos = i;
                    int fileid = mData.get(i).getFk_file_id();
                    Intent intent5 = new Intent(HomePageActivity.this,TableFileActivity.class);
                    intent5.putExtra("figureid", fileid);
                    intent5.putExtra("operation", TableFileActivity.GET_BIN);
                    intent5.putExtra("mData_current_pos", i);
                    startActivityForResult(intent5,requestCode);

                }
                break;
/*            case 6:
                /***************访问数据库：加载美食详情信息(推送者头像)***************/
 /*               for(int i = 0 ; i < mData.size(); i++){
                    //mData_current_pos = i;
                    int fileid = mData.get(i).getUserEntity().getFk_figure_id();
                    Intent intent6 = new Intent(HomePageActivity.this,TableFileActivity.class);
                    intent6.putExtra("figureid", fileid);
                    intent6.putExtra("operation", TableFileActivity.GET_BIN);
                    intent6.putExtra("mData_current_pos", i);
                    startActivityForResult(intent6,requestCode);

                }

                break;
*/
                default:
                    break;
        }

    }


   /* public void processData(){

    }
*/
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


    public void initTabView(){
        TabLayout tabLayout = findViewById(R.id.toolbar_tab);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
              @Override
              public void onTabSelected(TabLayout.Tab tab) {
                  //updateData(3);
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

        //setScrollViewContent(0);
    }

    public void setScrollViewContent(int pos){
        LinearLayout layout = findViewById(R.id.ll_sc_content);
        layout.removeAllViews();


        switch (pos){
            case LATEST_FOOD:
                for (int i = 0; i < mData.size(); i++) {
                    final int index = i;

                    View view = View.inflate(HomePageActivity.this, R.layout.item_food, null);

                   ((TextView) view.findViewById(R.id.tv_bloger)).setText(mData.get(i).getUserEntity().getName());

                   byte[] food_bin = mData.get(i).getFoodbin();
                    Bitmap food_bmp = BitmapFactory.decodeByteArray(food_bin, 0, food_bin.length);
                    ((ImageView) view.findViewById(R.id.iv_food)).setImageBitmap(food_bmp);

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
                            intent.putExtra("foodEntity", (Serializable) mData.get(index));
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
