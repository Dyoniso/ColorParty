package com.dyoniso.colorparty.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.dyoniso.colorparty.fragment.CPFragment;

public class CPInterfaceActivity extends AppCompatActivity {
    private static final String TAG = CPInterfaceActivity.class.getName();
    private static final int CONTENT_ID = 10001;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_ID);

        setContentView(frame, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        FragmentTransaction fT = getSupportFragmentManager().beginTransaction();
        fT.add(CONTENT_ID, new CPFragment());
        fT.commit();

        Log.i(TAG, "New fragment created! ("+CPFragment.class.getSimpleName()+")");
    }
}
