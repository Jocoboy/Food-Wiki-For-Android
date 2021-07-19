package com.zstu.foodwiki;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailPageActivity extends AppCompatActivity implements View.OnClickListener {


    TextView tv_title;
    TextView tv_content;
    TextView tv_selfcomment;
    TextView tv_reads;
    TextView tv_shares;
    TextView tv_stars;
    TextView tv_likes;
    ImageView iv_food;

    TextView tv_bloger;
    TextView tv_follows;
    TextView tv_followers;
    ImageView iv_figure;


    FoodEntity currentFoodEntity;
    UserEntity currentUserEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        getId();
        bindingEvents();

        loadData();
        renderpage();
    }

    public void loadData(){
        Intent intent = getIntent();
        currentFoodEntity = (FoodEntity) intent.getSerializableExtra("foodEntity");
        currentUserEntity = currentFoodEntity.getUserEntity();
    }

    public void renderpage(){
        tv_title.setText(currentFoodEntity.getTitle());
        tv_content.setText(currentFoodEntity.getContentdetails());
        tv_selfcomment.setText(currentFoodEntity.getSelfcomment());
        tv_reads.setText(currentFoodEntity.getReads()+"");
        tv_likes.setText(currentFoodEntity.getLikes()+"");
        tv_shares.setText(currentFoodEntity.getShares()+"");
        tv_stars.setText(currentFoodEntity.getStars()+"");
        byte[]  food_bin = currentFoodEntity.getFoodbin();
        iv_food.setImageBitmap(BitmapFactory.decodeByteArray(food_bin, 0, food_bin.length));

        tv_bloger.setText(currentUserEntity.getName());
        tv_follows.setText(currentUserEntity.getFollows()+"");
        tv_followers.setText(currentUserEntity.getFollowers()+"");
        byte[] figure_bin = currentUserEntity.getFigurebin();
       // iv_figure.setImageBitmap(BitmapFactory.decodeByteArray(figure_bin, 0, figure_bin.length));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            default:
                break;
        }
    }


    public void getId(){
        tv_title = findViewById(R.id.tv_foodname);
        tv_content = findViewById(R.id.tv_detail_description);
        tv_selfcomment = findViewById(R.id.tv_detail_comment);
        tv_reads = findViewById(R.id.tv_reads);
        tv_likes = findViewById(R.id.tv_like_detail);
        tv_shares = findViewById(R.id.tv_share_details);
        tv_stars = findViewById(R.id.tv_star_detail);
        iv_food = findViewById(R.id.iv_space_publish);

        tv_bloger = findViewById(R.id.tv_name);
        tv_follows = findViewById(R.id.tv_follows);
        tv_followers = findViewById(R.id.tv_followers);
        iv_figure = findViewById(R.id.iv_bloger);
    }

    public void bindingEvents(){

    }
}
