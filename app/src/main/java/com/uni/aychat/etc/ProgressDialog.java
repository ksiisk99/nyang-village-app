package com.uni.aychat.etc;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import androidx.annotation.NonNull;

import com.uni.aychat.R;

public class ProgressDialog extends Dialog {

    public ProgressDialog(@NonNull Context context){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_progress);
    }
}
