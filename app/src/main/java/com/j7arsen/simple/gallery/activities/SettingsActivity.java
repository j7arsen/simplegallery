package com.j7arsen.simple.gallery.activities;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.SyncStateContract;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.j7arsen.simple.gallery.R;
import com.j7arsen.simple.gallery.utils.AppConstants;
import com.j7arsen.simple.gallery.utils.AppSettings;

/**
 * Created by arsen on 12.01.2016.
 */
public class SettingsActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{

    private LinearLayout mLlContent;
    private TextView mTvTitle;
    private TextView mTvTimePeriod;
    private SeekBar mSbTimePeriod;
    private Button mSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_layout);
        setActionBar();
        setActionBarTitle(getResources().getString(R.string.back_btn));

        initUI();
        initData();
        setListeners();
    }

    private void initUI(){
        mTvTimePeriod = (TextView) findViewById(R.id.tvTimePeriod);
        mSbTimePeriod = (SeekBar) findViewById(R.id.seekBar);
        mSave = (Button) findViewById(R.id.btnSaveSettings);

    }

    private void initData(){
        int timePeriod = AppSettings.getIntValue(AppConstants.KEY_TIME_PERIOD_SAVE, this);
        mSbTimePeriod.setProgress(timePeriod);
        mTvTimePeriod.setText(timePeriod + " " + getResources().getString(R.string.second));
    }


    private void setListeners(){
        mLlContent.setOnClickListener(this);
        mSave.setOnClickListener(this);
        mSbTimePeriod.setOnSeekBarChangeListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llActionBarBackContent:
                onBackPressed();
                break;
            case R.id.btnSaveSettings:
                AppSettings.saveIntValue(AppConstants.KEY_TIME_PERIOD_SAVE, mSbTimePeriod.getProgress(), this);
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mTvTimePeriod.setText(progress + " " + getResources().getString(R.string.second));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
