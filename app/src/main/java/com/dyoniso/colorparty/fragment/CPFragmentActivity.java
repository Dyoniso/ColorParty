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
import androidx.fragment.app.FragmentActivity;

import com.dyoniso.colorparty.R;
import com.dyoniso.colorparty.adapter.color.ColorAdapter;
import com.dyoniso.colorparty.model.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

interface CPViews {
    void setTextColor(Color color, int hex);
    void setScore(int score);
}

public class CPFragmentActivity extends FragmentActivity implements CPViews {
    private boolean zShowingBack;

    @BindView(R.id.score_view)
    TextView zScoreView;
    @BindView(R.id.color_view)
    TextView zColorView;

    private CPFragment4B zCPFragment4B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_trasition);
        ButterKnife.bind(this);

        zCPFragment4B = new CPFragment4B(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, zCPFragment4B)
                .commit();
            }
    }

    @OnClick(R.id.btn_start_game) void start() {
        zCPFragment4B.choseColor();
    }

    @OnClick(R.id.btn_trasition) void trasition() {
        flipFragment();
    }

    public void flipFragment() {
        if (zShowingBack) {
            getSupportFragmentManager().popBackStack();
            return;
        }

        zShowingBack = true;

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)

                .replace(R.id.container, new CPFragment6B())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void setTextColor(Color color, int hex) {
        zColorView.setText(color.getName());
        zColorView.setTextColor(hex);
    }

    @Override
    public void setScore(int score) {
        zScoreView.setText("Score: "+score);
    }

    public static class CPFragment4B extends Fragment {
        private final String TAG = CPFragment4B.class.getName();

        private int BLOCK_COLOR_ID_0 = 0;
        private int BLOCK_COLOR_ID_1 = 1;
        private int BLOCK_COLOR_ID_2 = 2;
        private int BLOCK_COLOR_ID_3 = 3;

        @BindView(R.id.color_block_0)
        CardView zBlockColor0;
        @BindView(R.id.color_block_1)
        CardView zBlockColor1;
        @BindView(R.id.color_block_2)
        CardView zBlockColor2;
        @BindView(R.id.color_block_3)
        CardView zBlockColor3;

        private CPViews zCPViews;
        private Color zChosenColor;
        private ColorAdapter zColorAdapter;
        private int zScore;

        private List<Color> zDifferentColor = new ArrayList<>();

        public CPFragment4B() {}

        public CPFragment4B(CPViews cpViews) {
            this.zCPViews = cpViews;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.cp_fragment_4b, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View r, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(r, savedInstanceState);
            ButterKnife.bind(this, r);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            zColorAdapter = new ColorAdapter(getContext());
            cScore();
        }

        private void cScore() {
            if (zScore < 0) {
                zScore = 0;
            }

            zCPViews.setScore(zScore);
        }

        private void addScore(int v) {
            zScore = zScore + v;
            cScore();
        }

        private void removeScore(int v) {
            zScore = zScore - v;
            cScore();
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

        public void choseColor() {
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

            zCPViews.setTextColor(zChosenColor, selectRandomColor().getHex());
        }
    }

    public static class CPFragment6B extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.cp_fragment_6b, container, false);
        }
    }

}
