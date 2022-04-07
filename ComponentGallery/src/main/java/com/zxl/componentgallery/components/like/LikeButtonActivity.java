package com.zxl.componentgallery.components.like;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zxl.componentgallery.R;

public class LikeButtonActivity extends AppCompatActivity implements OnLikeListener,
        OnAnimationEndListener {

    public static final String TAG = "MainActivity";


    LikeButton starButton;

    LikeButton likeButton;

    LikeButton thumbButton;

    LikeButton smileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likebutton);
        starButton = findViewById(R.id.star_button);
        starButton.setOnAnimationEndListener(this);
        starButton.setOnLikeListener(this);

        likeButton = findViewById(R.id.heart_button);
        likeButton.setOnLikeListener(this);
        likeButton.setOnAnimationEndListener(this);

        smileButton = findViewById(R.id.smile_button);
        smileButton.setOnLikeListener(this);
        smileButton.setOnAnimationEndListener(this);

        thumbButton = findViewById(R.id.thumb_button);
        thumbButton.setOnLikeListener(this);
        thumbButton.setOnAnimationEndListener(this);

        thumbButton.setLiked(true);

        usingCustomIcons();

    }

    public void usingCustomIcons() {

//        //shown when the button is in its default state or when unLiked.
//        smileButton.setUnlikeDrawable(new BitmapDrawable(getResources(), new IconicsDrawable(this, CommunityMaterial.Icon.cmd_emoticon).colorRes(android.R.color.darker_gray).sizeDp(25).toBitmap()));
//
//        //shown when the button is liked!
//        smileButton.setLikeDrawable(new BitmapDrawable(getResources(), new IconicsDrawable(this, CommunityMaterial.Icon.cmd_emoticon).colorRes(android.R.color.holo_purple).sizeDp(25).toBitmap()));
    }


    @Override
    public void liked(LikeButton likeButton) {
        Toast.makeText(this, "Liked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        Toast.makeText(this, "Disliked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAnimationEnd(LikeButton likeButton) {
        Log.d(TAG, "Animation End for %s" + likeButton);
    }

}
