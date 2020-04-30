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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CPFragment extends Fragment {
    private static final String TAG = CPFragment.class.getName();

    private static int BLOCK_COLOR_ID_0 = 0;
    private static int BLOCK_COLOR_ID_1 = 1;
    private static int BLOCK_COLOR_ID_2 = 2;
    private static int BLOCK_COLOR_ID_3 = 3;

    @BindView(R.id.score_view)
    TextView zScoreView;
    @BindView(R.id.color_block_0)
    CardView zBlockColor0;
    @BindView(R.id.color_block_1)
    CardView zBlockColor1;
    @BindView(R.id.color_block_2)
    CardView zBlockColor2;
    @BindView(R.id.color_block_3)
    CardView zBlockColor3;
    @BindView(R.id.color_view)
    TextView zColorView;

    private Color zChosenColor;
    private ColorAdapter zColorAdapter;
    private int zScore;

    private List<Color> zDifferentColor = new ArrayList<>();

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

        zColorAdapter = new ColorAdapter(getContext());

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

    @OnClick(R.id.color_block_0) void block1() {
        if (zChosenColor.getID() == BLOCK_COLOR_ID_0) {
            addScore(1);
            Log.e(TAG, "Correct "+BLOCK_COLOR_ID_0);

        } else {
            Log.e(TAG, "Error");
            removeScore(5);
        }

        choseColor();
    }

    @OnClick(R.id.color_block_1) void block2() {
        if (zChosenColor.getID() == BLOCK_COLOR_ID_1) {
            addScore(1);
            Log.e(TAG, "Correct "+BLOCK_COLOR_ID_1);

        } else {
            Log.e(TAG, "Error");
            removeScore(5);
        }

        choseColor();
    }

    @OnClick(R.id.color_block_2) void block3() {
        if (zChosenColor.getID() == BLOCK_COLOR_ID_2) {
            addScore(1);
            Log.e(TAG, "Correct "+BLOCK_COLOR_ID_2);

        } else {
            Log.e(TAG, "Error");
            removeScore(5);
        }

        choseColor();
    }

    @OnClick(R.id.color_block_3) void block4() {
        if (zChosenColor.getID() == BLOCK_COLOR_ID_3) {
            addScore(1);
            Log.e(TAG, "Correct "+BLOCK_COLOR_ID_3);

        } else {
            Log.e(TAG, "Error");
            removeScore(5);
        }

        choseColor();
    }

    private Color selectRandomColor() {
        return zColorAdapter.getColor(new Random().nextInt(zColorAdapter.colorCount()));
    }

    private Color selectDifferentRandomColor() {
        Color rndColor = selectRandomColor();

        if (zDifferentColor.isEmpty()) {
            zDifferentColor.add(rndColor);
            return rndColor;
        }

        if (!zDifferentColor.contains(rndColor)) {
            zDifferentColor.add(rndColor);
            return rndColor;
        }

        do {
            rndColor = selectRandomColor();

        } while (zDifferentColor.contains(rndColor));

        zDifferentColor.add(rndColor);

        return rndColor;
    }

    private void choseColor() {
        zChosenColor =  selectRandomColor();

        Color block0 = selectDifferentRandomColor();
        Color block1 = selectDifferentRandomColor();
        Color block2 = selectDifferentRandomColor();
        Color block3 = selectDifferentRandomColor();

        BLOCK_COLOR_ID_0 = block0.getID();
        BLOCK_COLOR_ID_1 = block1.getID();
        BLOCK_COLOR_ID_2 = block2.getID();
        BLOCK_COLOR_ID_3 = block3.getID();

        zBlockColor0.setCardBackgroundColor(block0.getHex());
        zBlockColor1.setCardBackgroundColor(block1.getHex());
        zBlockColor2.setCardBackgroundColor(block2.getHex());
        zBlockColor3.setCardBackgroundColor(block3.getHex());
        zDifferentColor.clear();

        zColorView.setText(zChosenColor.getName());
        zColorView.setTextColor(selectRandomColor().getHex());
    }

}
