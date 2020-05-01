package com.dyoniso.colorparty.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.dyoniso.colorparty.R;
import com.dyoniso.colorparty.fragment.CPFragmentActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_start) void start() {
        startActivity(new Intent(getApplicationContext(), CPFragmentActivity.class));
    }
}
