package com.dyoniso.colorparty.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.dyoniso.colorparty.R;
import com.dyoniso.colorparty.adapter.color.ColorAdapter;
import com.dyoniso.colorparty.model.Color;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CPFragment extends Fragment {
    private static final String TAG = CPFragment.class.getName();

    @BindView(R.id.score_view)
    TextView zScoreView;
    @BindView(R.id.color_block_1)
    CardView zBlockColor1;
    @BindView(R.id.color_block_2)
    CardView zBlockColor2;
    @BindView(R.id.color_block_3)
    CardView zBlockColor3;
    @BindView(R.id.color_block_4)
    CardView zBlockColor4;
    @BindView(R.id.color_view)
    TextView zColorView;

    private ColorAdapter zColorAdapter;
    private Color zChosenColor;
    private int zScore;

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

        cScore();
    }

    private void cScore() {
        if (zScore < 0) {
            zScore = 0;
        }

        zScoreView.setText("Score: "+zScore);
    }

    private void addScore(int v) {
        zScore = zScore + v;
        cScore();
    }

    private void removeScore(int v) {
        zScore = zScore - v;
        cScore();
    }

    @OnClick(R.id.btn_start_game) void start() {
        choseColor();
    }

    @OnClick(R.id.color_block_1) void block1() {
        int COLOR_BLOCK_ID = zColorAdapter.getColor(0).getID();

        if (zChosenColor.getID() == COLOR_BLOCK_ID) {
            addScore(1);
            Log.e(TAG, "Correct "+COLOR_BLOCK_ID);

        } else {
            Log.e(TAG, "Error");
            removeScore(5);
        }

        choseColor();
    }

    @OnClick(R.id.color_block_2) void block2() {
        int COLOR_BLOCK_ID = zColorAdapter.getColor(1).getID();

        if (zChosenColor.getID() == COLOR_BLOCK_ID) {
            addScore(1);
            Log.e(TAG, "Correct "+COLOR_BLOCK_ID);

        } else {
            Log.e(TAG, "Error");
            removeScore(5);
        }

        choseColor();
    }

    @OnClick(R.id.color_block_3) void block3() {
        int COLOR_BLOCK_ID = zColorAdapter.getColor(2).getID();

        if (zChosenColor.getID() == COLOR_BLOCK_ID) {
            addScore(1);
            Log.e(TAG, "Correct "+COLOR_BLOCK_ID);

        } else {
            Log.e(TAG, "Error");
            removeScore(5);
        }

        choseColor();
    }

    @OnClick(R.id.color_block_4) void block4() {
        int COLOR_BLOCK_ID = zColorAdapter.getColor(3).getID();

        if (zChosenColor.getID() == COLOR_BLOCK_ID) {
            addScore(1);
            Log.e(TAG, "Correct "+COLOR_BLOCK_ID);

        } else {
            Log.e(TAG, "Error");
            removeScore(5);
        }

        choseColor();
    }

    private void choseColor() {
        zChosenColor = zColorAdapter.getColor(new Random().nextInt(zColorAdapter.colorCount()));

        zColorView.setText(zChosenColor.getName());
    }

}
