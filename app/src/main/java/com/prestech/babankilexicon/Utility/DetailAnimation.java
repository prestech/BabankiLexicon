package com.prestech.babankilexicon.Utility;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

// Adapted from https://github.com/Mustufa786/recyclerViewCollapase
public class DetailAnimation {
    static final String TAG = "DetailAnimation";

    public static void expand(final View view) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int actualHeight = view.getMeasuredHeight();

        view.getLayoutParams().height = 0;
        view.setVisibility((View.VISIBLE));

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                view.getLayoutParams().height = interpolatedTime == 1 ?
                        ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (actualHeight * interpolatedTime);
                view.requestLayout();
            }
        };

        animation.setDuration((long) (actualHeight /
                view.getContext().getResources().getDisplayMetrics().density));

        view.setVisibility((View.VISIBLE));
        view.startAnimation(animation);
        Log.d(TAG, "expanded detail");
    }

    public static void collapse(final View view) {
        final int actualHeight = view.getMeasuredHeight();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height =
                            actualHeight - (int) (actualHeight * interpolatedTime);
                    view.requestLayout();
                }
            }
        };

        animation.setDuration((long) (actualHeight/ view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(animation);
        Log.d(TAG, "collapsed detail");
    }
}
