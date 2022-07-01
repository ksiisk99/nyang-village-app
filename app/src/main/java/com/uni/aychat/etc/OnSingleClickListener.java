package com.uni.aychat.etc;

import android.os.SystemClock;
import android.view.View;

public abstract class OnSingleClickListener implements View.OnClickListener {
    private static final long CLICK_INTERVAL=700;
    private long lastClickedTime;

    public abstract void onSingleClick(View v);

    @Override
    public void onClick(View view) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime=currentClickTime-lastClickedTime;
        lastClickedTime=currentClickTime;

        if(elapsedTime<=CLICK_INTERVAL)return;
        onSingleClick(view);
    }
}

