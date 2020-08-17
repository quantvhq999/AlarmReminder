package com.delaroystudios.alarmreminder;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.delaroystudios.alarmreminder.R;

import java.util.logging.Level;

public class AlarmActivity extends AppCompatActivity {
    private LinearLayout background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        overridePendingTransition(R.anim.animation, R.anim.animation);

        background = (LinearLayout) findViewById(R.id.background);
        final ViewTreeObserver viewTreeObserver = background.getViewTreeObserver();

        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onGlobalLayout() {
                    circularRevealActivity();
                    background.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

            });
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void circularRevealActivity() {
        int cx = background.getLeft() - getDips(0);
        int cy = background.getBottom() - getDips(110);

        float finalRadius = Math.max(background.getWidth(), background.getHeight());
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(background, cx, cy, 0, finalRadius);
        circularReveal.setDuration(300);
        circularReveal.start();

    }

    private int getDips(int dps) {
        Resources resources = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, resources.getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = background.getLeft();
            int cy = background.getBottom();

            float finalRadius = Math.max(background.getWidth(), background.getHeight());
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(background, cx, cy, finalRadius, 0);

            circularReveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    background.setVisibility(View.GONE);
                    finish();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            circularReveal.setDuration(300);
            circularReveal.start();
        }
        else {
            super.onBackPressed();
        }
    }
}