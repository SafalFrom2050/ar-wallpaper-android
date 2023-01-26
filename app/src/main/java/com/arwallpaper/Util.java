package com.arwallpaper;

import android.view.View;

public class Util {
    public static void animateScaleUp(View view){
        view.animate()
                .scaleXBy(0.2f)
                .scaleYBy(0.2f)
                .setDuration(200)
                .withEndAction(()-> {
                    view.animate()
                            .scaleXBy(-0.2f)
                            .scaleYBy(-0.2f)
                            .setDuration(200);
                });
    }
}
