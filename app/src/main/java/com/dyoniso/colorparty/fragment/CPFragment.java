package com.dyoniso.colorparty.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dyoniso.colorparty.R;
import com.dyoniso.colorparty.adapter.color.ColorAdapter;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CPFragment extends Fragment {

    @BindView(R.id.color_view)
    TextView zColorView;

    private ColorAdapter zColorAdapter;

    public CPFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cp_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View r, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(r, savedInstanceState);
        ButterKnife.bind(this, getActivity());

        zColorAdapter = new ColorAdapter();
    }

    @OnClick(R.id.btn_start_game) void start() {
        zColorView.setText(zColorAdapter.getColor(new Random().nextInt(zColorAdapter.colorCount())).getName());
    }
}
