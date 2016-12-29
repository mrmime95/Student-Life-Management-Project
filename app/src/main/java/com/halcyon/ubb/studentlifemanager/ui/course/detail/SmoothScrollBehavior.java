package com.halcyon.ubb.studentlifemanager.ui.course.detail;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.ScrollerCompat;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * Created by Baroti Csaba on 12/27/2016.
 */

public class SmoothScrollBehavior extends AppBarLayout.Behavior {
    private static final String TAG = "SmoothScrollBehavior";
    //The higher this value is, the faster the user must scroll for the AppBarLayout to collapse by itself
    private static final int SCROLL_SENSIBILITY = 5;
    //The real fling velocity calculation seems complex, in this case it is simplified with a multiplier
    private static final int FLING_VELOCITY_MULTIPLIER = 60;

    private boolean alreadyFlung = false;
    private boolean request = false;
    private boolean expand = false;
    private int velocity = 0;
    private int nestedScrollViewId;

    public SmoothScrollBehavior(int nestedScrollViewId) {
        this.nestedScrollViewId = nestedScrollViewId;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child,
                                  View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        if(Math.abs(dy) >= SCROLL_SENSIBILITY) {
            request = true;
            expand = dy < 0;
            velocity = dy * FLING_VELOCITY_MULTIPLIER;
        } else {
            request = false;
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        request = false;
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View target) {
        if(request) {
            NestedScrollView nestedScrollView = (NestedScrollView) coordinatorLayout.findViewById(nestedScrollViewId);
            if (expand) {
                //No need to force expand if it is already ready expanding
                if (nestedScrollView.getScrollY() > 0) {
                    int finalY = getPredictedScrollY(nestedScrollView);
                    if (finalY <= 0) {
                        //since onNestedFling does not work to expand the AppBarLayout, we need to manually expand it
                        expandAppBarLayoutWithVelocity(coordinatorLayout, appBarLayout, velocity);
                    }
                }
            } else {
                //onNestedFling will collapse the AppBarLayout with an animation time relative to the velocity
                onNestedFling(coordinatorLayout, appBarLayout, target, 0, velocity, true);

                if(!alreadyFlung) {
                    //TODO wait for AppBarLayout to be collapsed before scrolling for even smoother visual
                    nestedScrollView.fling(velocity);
                }
            }
        }
        alreadyFlung = false;
        super.onStopNestedScroll(coordinatorLayout, appBarLayout, target);
    }

    private int getPredictedScrollY(NestedScrollView nestedScrollView) {
        int finalY = 0;
        try {
            //With reflection, we can get the ScrollerCompat from the NestedScrollView to predict where the scroll will end
            Field scrollerField = nestedScrollView.getClass().getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Object object = scrollerField.get(nestedScrollView);
            ScrollerCompat scrollerCompat = (ScrollerCompat) object;
            finalY = scrollerCompat.getFinalY();
        } catch (Exception e ) {
            e.printStackTrace();
            //If the reflection fails, it will return 0, which means the scroll has reached top
            Log.e(TAG, "Failed to get mScroller field from NestedScrollView through reflection. Will assume that the scroll reached the top.");
        }
        return finalY;
    }

    private void expandAppBarLayoutWithVelocity(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, float velocity) {
        try {
            //With reflection, we can call the private method of Behavior that expands the AppBarLayout with specified velocity
            Method animateOffsetTo = getClass().getSuperclass().getDeclaredMethod("animateOffsetTo", CoordinatorLayout.class, AppBarLayout.class, int.class, float.class);
            animateOffsetTo.setAccessible(true);
            animateOffsetTo.invoke(this, coordinatorLayout, appBarLayout, 0, velocity);
        } catch (Exception e) {
            e.printStackTrace();
            //If the reflection fails, we fall back to the public method setExpanded that expands the AppBarLayout with a fixed velocity
            Log.e(TAG, "Failed to get animateOffsetTo method from AppBarLayout.Behavior through reflection. Falling back to setExpanded.");
            appBarLayout.setExpanded(true, true);
        }
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
        alreadyFlung = true;
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }
}
