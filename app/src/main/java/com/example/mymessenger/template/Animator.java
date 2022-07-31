package com.example.mymessenger.template;

import android.view.View;
import android.view.animation.Animation;

public class Animator {
    View view, rView;

    public Animator(View view) {
        this.view = view;

    }

    Animation lAnim, fAnim;

    public void setPrimAnim(Animation lAnim, Animation fAnim) {
        this.lAnim = lAnim;
        this.fAnim = fAnim;
    }

    public void setRefVIew(View pView) {
        this.rView = pView;
    }

    public void loadPrimAnimation(int vis) {
        view.setVisibility(vis);

        switch (vis) {
            case View.VISIBLE:
                view.setAnimation(lAnim);
                break;
            case View.INVISIBLE:
                view.setAnimation(fAnim);
                break;
            default:
                view.setVisibility(View.INVISIBLE);
        }


    }
}
