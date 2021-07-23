package com.zstu.foodwiki;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;
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
    private List<UserFollowEntity> mFollowData;
    private List<FoodLikeEntity> mLikeData;
    private List<FoodStarEntity> mStarData;
    private List<CommentEntity> mCommentData;
   // private int mData_current_pos;
    //private List<UserEntity> nData;
   // private int nData_current_pos;

    private boolean isFigureEmpty = false;
    private boolean isFoodEmpty = false;
    private boolean isAssist = false;


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

    public void InsertGeneral(){
        Intent intent = new Intent(HomePageActivity.this, TableFileActivity.class);
        intent.putExtra("operation", TableFileActivity.INSERT_GENERAL);
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
        if(isAssist){
            InsertGeneral();
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

        updateData(7);


        updateData(10);
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
            case 7:
                Intent intent7 = new Intent(HomePageActivity.this,TableUserFollowActivity.class);
                intent7.putExtra("userid", userid);
                intent7.putExtra("operation", TableUserFollowActivity.QUERY);
                startActivityForResult(intent7, requestCode);
                break;
            case 8:
                for(int i = 0 ; i < mFollowData.size() ; i++){
                    int followid = mFollowData.get(i).getFk_follow_id();
                    Intent intent8 = new Intent(HomePageActivity.this,TableUserInfoActivity.class);
                    intent8.putExtra("userid", followid);
                    intent8.putExtra("operation", TableUserInfoActivity.GET_ALL);
                    intent8.putExtra("mData_current_pos", i);
                    startActivityForResult(intent8, requestCode);
                }
                break;
            case 10:
                Intent intent10 = new Intent(HomePageActivity.this,TableCommentActivity.class);
                intent10.putExtra("userid", userid);
                intent10.putExtra("operation", TableCommentActivity.QUERY_BY_TARGETID);
                startActivityForResult(intent10, requestCode);
                break;
            case 11:
                for(int i = 0 ; i < mCommentData.size(); i++){
                    int userid = mCommentData.get(i).getFk_user_id();
                    Intent intent11 = new Intent(HomePageActivity.this,TableUserInfoActivity.class);
                    intent11.putExtra("userid", userid);
                    intent11.putExtra("operation", TableUserInfoActivity.GET_ALL);
                    intent11.putExtra("mData_current_pos", i);
                    startActivityForResult(intent11,requestCode);
                }
                break;
            case 13:
                for(int i = 0 ; i < mCommentData.size(); i++){
                    int foodid = mCommentData.get(i).getFk_food_id();
                    Intent intent13= new Intent(HomePageActivity.this,TableFoodActivity.class);
                    intent13.putExtra("foodid", foodid);
                    intent13.putExtra("operation", TableFoodActivity.QUERY_SINGLE);
                    intent13.putExtra("mData_current_pos", i);
                    startActivityForResult(intent13,requestCode);
                }
                break;
            default:
                break;
        }

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


                        if(mLikeData==null){
                            mLikeData = new ArrayList<FoodLikeEntity>();
                            for(int i = 1, j = 1 ; i <= 3 ; i++,j++){
                                FoodLikeEntity foodLikeEntity = new FoodLikeEntity(j,userid,mData.get(i).getPk_food_id());
                                foodLikeEntity.setFoodEntity(mData.get(i));
                                mLikeData.add(foodLikeEntity);
                            }
                        }

                        if(mStarData==null){
                            mStarData = new ArrayList<FoodStarEntity>();
                            for(int i = 0, j = 1 ; i <= 4 ; i+=4,j++){
                                FoodStarEntity foodStarEntity = new FoodStarEntity(j,userid,mData.get(i).getPk_food_id());
                                foodStarEntity.setFoodEntity(mData.get(i));
                                mStarData.add(foodStarEntity);
                            }
                        }

                      //  initTabView();
                    }
                }
                break;
            case 7:
                if(resultCode==200){
                    mFollowData = (List<UserFollowEntity>)data.getSerializableExtra("userFollowEntityList");

                    updateData(8);
                }
                break;
            case 8:
                if(resultCode==290){
                    int userid = data.getIntExtra("userid", -1);
                    int figureid = data.getIntExtra("figureid", -1);
                    String name  = data.getStringExtra("name");
                    String remark = data.getStringExtra("remark");
                    int follows = data.getIntExtra("follows", -1);
                    int followers = data.getIntExtra("followers", -1);
                    int readers = data.getIntExtra("readers", -1);

                    int pos =  data.getIntExtra("mData_current_pos", -1);

                    UserEntity userEntity = new UserEntity(userid,figureid,name,remark,follows,followers,readers);

                    mFollowData.get(pos).setUserEntity(userEntity);

                    Intent intent9 = new Intent(HomePageActivity.this,TableFileActivity.class);
                    intent9.putExtra("figureid", figureid);
                    intent9.putExtra("operation", TableFileActivity.GET_BIN);
                    intent9.putExtra("mData_current_pos", pos);
                    startActivityForResult(intent9,9);

                }
                break;
            case 9:
                if(resultCode==300){
                    byte[] mFollowData_figure = data.getByteArrayExtra("figure_bin");
                    int pos =  data.getIntExtra("mData_current_pos", -1);
                    mFollowData.get(pos).getUserEntity().setFigurebin(mFollowData_figure);

                }
                break;
            case 10:
                if(resultCode==233){
                    mCommentData = (List<CommentEntity>)data.getSerializableExtra("commentEntityList");
                    updateData(11);
                    updateData(13);
                }
                break;
            case 11:
                if(resultCode==290){
                    int userid = data.getIntExtra("userid", -1);
                    int figureid = data.getIntExtra("figureid", -1);
                    String name  = data.getStringExtra("name");
                    String remark = data.getStringExtra("remark");
                    int follows = data.getIntExtra("follows", -1);
                    int followers = data.getIntExtra("followers", -1);
                    int readers = data.getIntExtra("readers", -1);

                    int pos =  data.getIntExtra("mData_current_pos", -1);

                    UserEntity userEntity = new UserEntity(userid,figureid,name,remark,follows,followers,readers);

                    mCommentData.get(pos).setCommentUser(userEntity);

                    Intent intent9 = new Intent(HomePageActivity.this,TableFileActivity.class);
                    intent9.putExtra("figureid", figureid);
                    intent9.putExtra("operation", TableFileActivity.GET_BIN);
                    intent9.putExtra("mData_current_pos", pos);
                    startActivityForResult(intent9,12);
                }
                break;
            case 12:
                if(resultCode==300){
                    byte[] mComment_figure = data.getByteArrayExtra("figure_bin");
                    int pos =  data.getIntExtra("mData_current_pos", -1);
                    mCommentData.get(pos).getCommentUser().setFigurebin(mComment_figure);
                }
                break;
            case 13:
                if(resultCode==140){
                    int pos =  data.getIntExtra("mData_current_pos", -1);
                    FoodEntity foodEntity = (FoodEntity)data.getSerializableExtra("foodEntity");
                    int figureid = foodEntity.getFk_file_id();
                    mCommentData.get(pos).setCommentFood(foodEntity);



                    Intent intent14 = new Intent(HomePageActivity.this,TableFileActivity.class);
                    intent14.putExtra("figureid", figureid);
                    intent14.putExtra("operation", TableFileActivity.GET_BIN);
                    intent14.putExtra("mData_current_pos", pos);
                    startActivityForResult(intent14,14);
                }
                break;
            case 14:
                if(resultCode==300){
                    byte[] mComment_food= data.getByteArrayExtra("figure_bin");
                    int pos =  data.getIntExtra("mData_current_pos", -1);
                    mCommentData.get(pos).getCommentFood().setFoodbin(mComment_food);
                    if(pos == mFollowData.size() - 1 ){
                        initTabView();

                    }
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



  /*  public void loadData(){

    }
*/


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
                        for (int i = 0; i < mFollowData.size(); i++) {
                            final int index = i;

                            View view = View.inflate(HomePageActivity.this, R.layout.item_bloger, null);

                            byte[] figure_bin = mFollowData.get(i).getUserEntity().getFigurebin();
                            Bitmap figure_bmp = BitmapFactory.decodeByteArray(figure_bin, 0, figure_bin.length);
                            ((ImageView) view.findViewById(R.id.iv_bloger)).setImageBitmap(figure_bmp);

                            ((TextView) view.findViewById(R.id.tv_name)).setText(mFollowData.get(index).getUserEntity().getName());
                            ((TextView) view.findViewById(R.id.tv_description_bloger)).setText(mFollowData.get(index).getUserEntity().getRemark());

                            boolean isCancelFollow = mFollowData.get(i).getIsCancelFollow();
                            ((CheckBox) view.findViewById(R.id.cb_remember_password)).setChecked(!isCancelFollow);

                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   CheckBox cb = v.findViewById(R.id.cb_remember_password);
                                   boolean state = cb.isChecked();
                                   cb.setChecked(!state);
                                   cb.setText(!state ? "取消关注":"关注");
                                }
                            });

                            layout.addView(view, i);
                    }
                break;
            case MY_STAR:
                    for (int i = 0; i < mStarData.size(); i++) {
                        final int index = i;

                        View view = View.inflate(HomePageActivity.this, R.layout.item_food, null);

                        ((TextView) view.findViewById(R.id.tv_bloger)).setText(mStarData.get(i).getFoodEntity().getUserEntity().getName());

                        byte[] food_bin = mStarData.get(i).getFoodEntity().getFoodbin();
                        Bitmap food_bmp = BitmapFactory.decodeByteArray(food_bin, 0, food_bin.length);
                        ((ImageView) view.findViewById(R.id.iv_food)).setImageBitmap(food_bmp);

                        ((TextView) view.findViewById(R.id.tv_title)).setText(mStarData.get(i).getFoodEntity().getTitle());
                        ((TextView) view.findViewById(R.id.tv_content)).setText('"'+mStarData.get(i).getFoodEntity().getSelfcomment()+'"');
                        ((TextView) view.findViewById(R.id.tv_phonenumber)).setText(mStarData.get(i).getFoodEntity().getPhonenumber());
                        ((TextView) view.findViewById(R.id.tv_like)).setText(mStarData.get(i).getFoodEntity().getLikes()+"");
                        ((TextView) view.findViewById(R.id.tv_share)).setText(mStarData.get(i).getFoodEntity().getShares()+"");
                        ((TextView) view.findViewById(R.id.tv_star)).setText(mStarData.get(i).getFoodEntity().getStars()+"");



                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HomePageActivity.this,DetailPageActivity.class);
                                intent.putExtra("foodEntity", (Serializable) mStarData.get(index).getFoodEntity());
                                startActivity(intent);
                            }
                        });
                        layout.addView(view, i);
                    }
                break;
            case MY_LIKE:
                    for (int i = 0; i < mLikeData.size(); i++) {
                        final int index = i;

                        View view = View.inflate(HomePageActivity.this, R.layout.item_food, null);

                        ((TextView) view.findViewById(R.id.tv_bloger)).setText(mLikeData.get(i).getFoodEntity().getUserEntity().getName());

                        byte[] food_bin = mLikeData.get(i).getFoodEntity().getFoodbin();
                        Bitmap food_bmp = BitmapFactory.decodeByteArray(food_bin, 0, food_bin.length);
                        ((ImageView) view.findViewById(R.id.iv_food)).setImageBitmap(food_bmp);

                        ((TextView) view.findViewById(R.id.tv_title)).setText(mLikeData.get(i).getFoodEntity().getTitle());
                        ((TextView) view.findViewById(R.id.tv_content)).setText('"'+mLikeData.get(i).getFoodEntity().getSelfcomment()+'"');
                        ((TextView) view.findViewById(R.id.tv_phonenumber)).setText(mLikeData.get(i).getFoodEntity().getPhonenumber());
                        ((TextView) view.findViewById(R.id.tv_like)).setText(mLikeData.get(i).getFoodEntity().getLikes()+"");
                        ((TextView) view.findViewById(R.id.tv_share)).setText(mLikeData.get(i).getFoodEntity().getShares()+"");
                        ((TextView) view.findViewById(R.id.tv_star)).setText(mLikeData.get(i).getFoodEntity().getStars()+"");



                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HomePageActivity.this,DetailPageActivity.class);
                                intent.putExtra("foodEntity", (Serializable) mLikeData.get(index).getFoodEntity());
                                startActivity(intent);
                            }
                        });
                        layout.addView(view, i);
                    }
                break;
            case MY_COMMENT:
                for (int i = 0; i < mCommentData.size(); i++) {
                    View view = View.inflate(HomePageActivity.this, R.layout.item_comment, null);

                    byte[] figure_bin = mCommentData.get(i).getCommentUser().getFigurebin();
                    Bitmap figure_bmp = BitmapFactory.decodeByteArray(figure_bin, 0, figure_bin.length);
                    ((ImageView) view.findViewById(R.id.iv_bloger)).setImageBitmap(figure_bmp);

                    byte[] food_bin = mCommentData.get(i).getCommentFood().getFoodbin();
                    Bitmap food_bmp = BitmapFactory.decodeByteArray(food_bin, 0, food_bin.length);
                    ((ImageView) view.findViewById(R.id.iv_whichfood)).setImageBitmap(food_bmp);

                    ((TextView) view.findViewById(R.id.tv_name)).setText(mCommentData.get(i).getCommentUser().getName());
                    ((TextView) view.findViewById(R.id.tv_comment_item)).setText(mCommentData.get(i).getComment());
                    ((TextView) view.findViewById(R.id.tv_time)).setText(mCommentData.get(i).getDt());

                    layout.addView(view, i);
                }
                break;
            default:
                break;

        }


    }
}
