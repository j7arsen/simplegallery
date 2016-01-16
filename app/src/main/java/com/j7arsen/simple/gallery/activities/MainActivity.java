package com.j7arsen.simple.gallery.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.j7arsen.simple.gallery.R;
import com.j7arsen.simple.gallery.directorychoose.DirectoryChooserDialog;
import com.j7arsen.simple.gallery.swipe.SwipeDetector;
import com.j7arsen.simple.gallery.utils.AppConstants;
import com.j7arsen.simple.gallery.utils.AppSettings;
import com.j7arsen.simple.gallery.utils.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener, View.OnTouchListener {

    private TextView mTvTitle;
    private ImageView mIvLoadFromDevice;
    private ImageView mIvLoadFromInternet;
    private ImageView mIvSettings;

    private ViewFlipper mViewFlipper;
    private ProgressBar mPbLoadImages;
    private ArrayList<String> mImagesPaths;

    private String mChoosenDir = "";

    private ArrayList<Bitmap> mBitmapList;

    private ProgressDialog simpleWaitDialog;

    private GestureDetector mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActionBar();
        setActionBarTitle();

        initUI();
        setListeners();

    }

    private void initUI() {
        mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        mPbLoadImages = (ProgressBar) findViewById(R.id.pbLoadImages);
    }

    private void setListeners() {
        mIvLoadFromDevice.setOnClickListener(this);
        mIvLoadFromInternet.setOnClickListener(this);
        mIvSettings.setOnClickListener(this);
        mViewFlipper.setOnTouchListener(this);
        mViewFlipper.setOnClickListener(this);
    }

    private void initData() {
        mDetector = new GestureDetector(MainActivity.this, new SwipeDetector(MainActivity.this, mViewFlipper));
        Utils utils = new Utils(this);
        if (mImagesPaths == null) {
            mImagesPaths = new ArrayList<>();
        }
        mImagesPaths = utils.getFilePaths(mChoosenDir);
        initViewFlipper();
    }

    private void initViewFlipper() {
        if(mViewFlipper.isFlipping()){
            mViewFlipper.stopFlipping();
            mViewFlipper.removeAllViews();
        }
        if(mImagesPaths != null && !mImagesPaths.isEmpty()){
            for (int i = 0; i < mImagesPaths.size(); i++) {
                ImageView imageView = new ImageView(this);
                Drawable image = Drawable.createFromPath(mImagesPaths.get(i));
                imageView.setImageDrawable(image);
                mViewFlipper.addView(imageView);
                setPathImage(imageView, mImagesPaths.get(i));
            }
        }
        startSlideShow();
    }

    private void initInternetViewFlipper(){
        if(mViewFlipper.isFlipping()){
            mViewFlipper.stopFlipping();
            mViewFlipper.removeAllViews();
        }
        if(mBitmapList != null){
            for(Bitmap b : mBitmapList){
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(b);
                mViewFlipper.addView(imageView);
            }
        }
        startSlideShow();
    }

    private void startSlideShow() {
        if (mViewFlipper.isFlipping()) {
            mViewFlipper.stopFlipping();
        }
        int timePeriod = AppSettings.getIntValue(AppConstants.KEY_TIME_PERIOD_SAVE, this);
        if (timePeriod < 1) {
            timePeriod = 1;
            AppSettings.saveIntValue(AppConstants.KEY_TIME_PERIOD_SAVE, timePeriod, this);
        }
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(timePeriod * 1000);
        mViewFlipper.startFlipping();
    }

    private void initActionBar() {
        View customNav = getLayoutInflater().inflate(R.layout.action_bar_layout, null);
        getActionBar().setTitle("");
        getActionBar().setCustomView(customNav);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setTitle("");
        mTvTitle = (TextView) customNav.findViewById(R.id.tvTitle);
        mIvLoadFromDevice = (ImageView) customNav.findViewById(R.id.ivLoadFromDevice);
        mIvLoadFromInternet = (ImageView) customNav.findViewById(R.id.ivLoadFromInternet);
        mIvSettings = (ImageView) customNav.findViewById(R.id.ivSettings);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Toolbar toolbar = (Toolbar) customNav.getParent();
            toolbar.setContentInsetsAbsolute(0, 0);
        }


    }


    private void setActionBarTitle() {
        setTitle(getResources().getString(R.string.app_name));
    }

    private void setTitle(String title) {
        mTvTitle.setText(title);
    }

    private void showChooseDialog() {
        mChoosenDir = "";
        DirectoryChooserDialog directoryChooserDialog =
                new DirectoryChooserDialog(MainActivity.this,
                        new DirectoryChooserDialog.ChosenDirectoryListener() {
                            @Override
                            public void onChosenDir(String chosenDir) {
                                mChoosenDir = chosenDir;
                                initData();
                            }
                        });
        directoryChooserDialog.chooseDirectory(mChoosenDir);
    }

    private void showSettings() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivityForResult(intent, AppConstants.SETTINGS_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstants.SETTINGS_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                startSlideShow();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSettings:
                showSettings();
                break;
            case R.id.ivLoadFromDevice:
                showChooseDialog();
                break;
            case R.id.ivLoadFromInternet:
                runDownloadImages();
                break;

        }
    }

    private void runDownloadImages(){
        for(int i = 0; i < AppConstants.IMAGE_URLS.length; i ++ ){
            new ImageDownloader().execute(AppConstants.IMAGE_URLS[i]);
        }
    }

    private void addDownloadedImages(Bitmap result){
        if(mBitmapList == null){
            mBitmapList = new ArrayList<>();
        }
        mBitmapList.add(result);
        initInternetViewFlipper();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }

    private void setPathImage(final ImageView imageView, final String path) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FullScreenImageActivity.class);
                intent.putExtra(FullScreenImageActivity.KEY_PATH_IMAGE, path);
                startActivity(intent);

            }
        });
    }

    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... param) {
            return downloadBitmap(param[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPbLoadImages.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            mPbLoadImages.setVisibility(View.GONE);
            addDownloadedImages(result);
        }

        private Bitmap downloadBitmap(String url) {
            final DefaultHttpClient client = new DefaultHttpClient();

            final HttpGet getRequest = new HttpGet(url);
            try {

                HttpResponse response = client.execute(getRequest);

                final int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode != HttpStatus.SC_OK) {
                    return null;

                }

                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try {
                        inputStream = entity.getContent();

                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        return bitmap;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (Exception e) {
                getRequest.abort();
            }

            return null;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewFlipper.removeAllViews();
        mBitmapList.clear();
        mBitmapList = null;
    }
}
