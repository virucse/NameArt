package com.formationapps.nameart.helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by Virendra on 3/14/2018.
 */

public class Styleable {
    public interface Clicked{
        public void onClicked(View view);
    }
    public static void onClick(final View view,final Clicked clicked){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            View myView = view;
            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;
            float finalRadius = (float) Math.hypot(cx, cy);
            Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if(clicked!=null){
                        clicked.onClicked(view);
                    }
                }
            });
            anim.start();
        }else{
            if(clicked!=null){
                clicked.onClicked(view);
            }
        }
    }
}
