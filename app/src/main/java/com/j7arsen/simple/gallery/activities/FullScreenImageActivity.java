package com.j7arsen.simple.gallery.activities;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j7arsen.simple.gallery.R;

/**
 * Created by arsen on 11.01.2016.
 */
public class FullScreenImageActivity extends Activity implements View.OnClickListener {

    public static final String KEY_PATH_IMAGE = "FullScreenImageActivity.PATH_IMAGE";

    private LinearLayout mLlContent;
    private ImageView mIvFullScreenImage;
    private TextView mTvTitle;
    private String mPathImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_layout);
        setActionBar();
        setActionBarTitle(getResources().getString(R.string.back_btn));
        initUI();
        setListeners();
        getArgumentsData();
        initImage();
    }

    private void initUI(){
        mIvFullScreenImage = (ImageView) findViewById(R.id.ivFullScreenImage);
    }

    private void setListeners(){
        mLlContent.setOnClickListener(this);
    }

    private void initImage(){
        Drawable image = Drawable.createFromPath(mPathImage);
        mIvFullScreenImage.setImageDrawable(image);
    }

    private void setActionBar() {
        View customNav = getLayoutInflater().inflate(R.layout.action_bar_back_layout, null);
        getActionBar().setTitle("");
        getActionBar().setCustomView(customNav);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setTitle("");
        mTvTitle = (TextView) customNav.findViewById(R.id.tvTitle);
        mLlContent = (LinearLayout) customNav.findViewById(R.id.llActionBarBackContent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Toolbar toolbar = (Toolbar) customNav.getParent();
            toolbar.setContentInsetsAbsolute(0, 0);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setActionBarTitle(String title) {
        super.setTitle(title);
    }

    private void getArgumentsData(){
        if(getIntent() != null && getIntent().getExtras() != null &&
                !getIntent().getExtras().isEmpty()){
            mPathImage = getIntent().getExtras().getString(KEY_PATH_IMAGE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llActionBarBackContent:
                onBackPressed();
                break;
        }

    }
}
