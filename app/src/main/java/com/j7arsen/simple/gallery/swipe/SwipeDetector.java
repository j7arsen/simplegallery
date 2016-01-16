package com.j7arsen.simple.gallery.swipe;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.j7arsen.simple.gallery.R;

/**
 * Created by arsen on 16.01.2016.
 */
public class SwipeDetector extends GestureDetector.SimpleOnGestureListener {

    private Context mContext;
    private ViewFlipper mViewFlipper;

    public SwipeDetector(Context context, ViewFlipper viewFlipper){
        this.mContext = context;
        this.mViewFlipper = viewFlipper;
    }

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_in));
                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));
                mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
                mViewFlipper.showNext();
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_in));
                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_out));
                mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
                mViewFlipper.showPrevious();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
        }
    };
}
