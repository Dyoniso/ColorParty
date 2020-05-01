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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dyoniso.colorparty.R;
import com.dyoniso.colorparty.adapter.color.ColorAdapter;
import com.dyoniso.colorparty.model.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

interface CPViews {
    void setTextColor(Color color, int hex);
    void setScore(int score);
}

public class CPFragmentActivity extends FragmentActivity implements CPViews {
    @BindView(R.id.score_view)
    TextView zScoreView;
    @BindView(R.id.color_view)
    TextView zColorView;

    private CPFragment4B zCPFragment4B;
    private CPFragment6B zCPFragment6B;
    private CPFragment8B zCPFragment8B;
    private AtomicInteger zCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_trasition);
        ButterKnife.bind(this);

        zCPFragment4B = new CPFragment4B(this);
        zCPFragment6B = new CPFragment6B(this);
        zCPFragment8B = new CPFragment8B(this);
        zCount = new AtomicInteger(2);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, zCPFragment4B)
                .commit();
            }
    }

    @OnClick(R.id.btn_start_game) void start() {
        switch (zCount.get()) {
            case 1:
                zCPFragment8B.choseColor();
                break;
            case 2:
                zCPFragment4B.choseColor();
                break;
            case 3:
                zCPFragment6B.choseColor();
                break;
        }
    }

    @OnClick(R.id.btn_trasition) void trasition() {
        flipFragment();
    }

    public void flipFragment() {
        FragmentTransaction fT = getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out);

        switch (zCount.get()) {
            case 1:
                fT.replace(R.id.container, zCPFragment4B);
                fT.addToBackStack(null);
                zCount.set(2);
                break;

            case 2:
                fT.replace(R.id.container, zCPFragment6B);
                zCount.set(3);
                break;

            case 3:
                fT.replace(R.id.container, zCPFragment8B);
                zCount.set(1);
                break;

            default:
                getSupportFragmentManager().popBackStack();
        }
        fT.commit();
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
        private static final String TAG = CPFragment6B.class.getName();

        @BindView(R.id.color_block_0)
        CardView zBlockColor0;
        @BindView(R.id.color_block_1)
        CardView zBlockColor1;
        @BindView(R.id.color_block_2)
        CardView zBlockColor2;
        @BindView(R.id.color_block_3)
        CardView zBlockColor3;
        @BindView(R.id.color_block_4)
        CardView zBlockColor4;
        @BindView(R.id.color_block_5)
        CardView zBlockColor5;

        private int BLOCK_COLOR_ID_0 = 0;
        private int BLOCK_COLOR_ID_1 = 1;
        private int BLOCK_COLOR_ID_2 = 2;
        private int BLOCK_COLOR_ID_3 = 3;
        private int BLOCK_COLOR_ID_4 = 4;
        private int BLOCK_COLOR_ID_5 = 5;

        private CPViews zCPViews;
        private Color zChosenColor;
        private ColorAdapter zColorAdapter;
        private int zScore;

        private List<Color> zDifferentColor = new ArrayList<>();

        public CPFragment6B() {}

        public CPFragment6B(CPViews cpViews) {
            this.zCPViews = cpViews;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.cp_fragment_6b, container, false);
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
        }

        @OnClick(R.id.color_block_0) void block0() {
            if (zChosenColor.getID() == BLOCK_COLOR_ID_0) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_ID_0);

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_1) void block1() {
            if (zChosenColor.getID() == BLOCK_COLOR_ID_1) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_ID_1);

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_2) void block2() {
            if (zChosenColor.getID() == BLOCK_COLOR_ID_2) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_ID_2);

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_3) void block3() {
            if (zChosenColor.getID() == BLOCK_COLOR_ID_3) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_ID_3);

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_4) void block4() {
            if (zChosenColor.getID() == BLOCK_COLOR_ID_4) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_ID_4);

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_5) void block5() {
            if (zChosenColor.getID() == BLOCK_COLOR_ID_5) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_ID_5);

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
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
            Color block4 = selectDifferentRandomColor();
            Color block5 = selectDifferentRandomColor();

            BLOCK_COLOR_ID_0 = block0.getID();
            BLOCK_COLOR_ID_1 = block1.getID();
            BLOCK_COLOR_ID_2 = block2.getID();
            BLOCK_COLOR_ID_3 = block3.getID();
            BLOCK_COLOR_ID_4 = block4.getID();
            BLOCK_COLOR_ID_5 = block5.getID();

            zBlockColor0.setCardBackgroundColor(block0.getHex());
            zBlockColor1.setCardBackgroundColor(block1.getHex());
            zBlockColor2.setCardBackgroundColor(block2.getHex());
            zBlockColor3.setCardBackgroundColor(block3.getHex());
            zBlockColor4.setCardBackgroundColor(block4.getHex());
            zBlockColor5.setCardBackgroundColor(block5.getHex());
            zDifferentColor.clear();

            zCPViews.setTextColor(zChosenColor, selectRandomColor().getHex());
        }
    }

    public static class CPFragment8B extends Fragment {
        private static final String TAG = CPFragment6B.class.getName();

        @BindView(R.id.color_block_0)
        CardView zBlockColor0;
        @BindView(R.id.color_block_1)
        CardView zBlockColor1;
        @BindView(R.id.color_block_2)
        CardView zBlockColor2;
        @BindView(R.id.color_block_3)
        CardView zBlockColor3;
        @BindView(R.id.color_block_4)
        CardView zBlockColor4;
        @BindView(R.id.color_block_5)
        CardView zBlockColor5;
        @BindView(R.id.color_block_6)
        CardView zBlockColor6;
        @BindView(R.id.color_block_7)
        CardView zBlockColor7;

        private int BLOCK_COLOR_ID_0 = 0;
        private int BLOCK_COLOR_ID_1 = 1;
        private int BLOCK_COLOR_ID_2 = 2;
        private int BLOCK_COLOR_ID_3 = 3;
        private int BLOCK_COLOR_ID_4 = 4;
        private int BLOCK_COLOR_ID_5 = 5;
        private int BLOCK_COLOR_ID_6 = 6;
        private int BLOCK_COLOR_ID_7 = 7;

        private CPViews zCPViews;
        private Color zChosenColor;
        private ColorAdapter zColorAdapter;
        private int zScore;

        private List<Color> zDifferentColor = new ArrayList<>();

        public CPFragment8B() {}

        public CPFragment8B(CPViews cpViews) {
            this.zCPViews = cpViews;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.cp_fragment_8b, container, false);
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
        }

        @OnClick(R.id.color_block_0) void block0() {
            if (zChosenColor.getID() == BLOCK_COLOR_ID_0) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_ID_0);

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_1) void block1() {
            if (zChosenColor.getID() == BLOCK_COLOR_ID_1) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_ID_1);

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_2) void block2() {
            if (zChosenColor.getID() == BLOCK_COLOR_ID_2) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_ID_2);

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_3) void block3() {
            if (zChosenColor.getID() == BLOCK_COLOR_ID_3) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_ID_3);

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_4) void block4() {
            if (zChosenColor.getID() == BLOCK_COLOR_ID_4) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_ID_4);

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_5) void block5() {
            if (zChosenColor.getID() == BLOCK_COLOR_ID_5) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_ID_5);

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }


        @OnClick(R.id.color_block_6) void block6() {
            if (zChosenColor.getID() == BLOCK_COLOR_ID_6) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_ID_6);

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }


        @OnClick(R.id.color_block_7) void block7() {
            if (zChosenColor.getID() == BLOCK_COLOR_ID_6) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_ID_6);

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
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
            Color block4 = selectDifferentRandomColor();
            Color block5 = selectDifferentRandomColor();
            Color block6 = selectDifferentRandomColor();
            Color block7 = selectDifferentRandomColor();

            BLOCK_COLOR_ID_0 = block0.getID();
            BLOCK_COLOR_ID_1 = block1.getID();
            BLOCK_COLOR_ID_2 = block2.getID();
            BLOCK_COLOR_ID_3 = block3.getID();
            BLOCK_COLOR_ID_4 = block4.getID();
            BLOCK_COLOR_ID_5 = block5.getID();
            BLOCK_COLOR_ID_6 = block6.getID();
            BLOCK_COLOR_ID_7 = block7.getID();

            zBlockColor0.setCardBackgroundColor(block0.getHex());
            zBlockColor1.setCardBackgroundColor(block1.getHex());
            zBlockColor2.setCardBackgroundColor(block2.getHex());
            zBlockColor3.setCardBackgroundColor(block3.getHex());
            zBlockColor4.setCardBackgroundColor(block4.getHex());
            zBlockColor5.setCardBackgroundColor(block5.getHex());
            zBlockColor6.setCardBackgroundColor(block6.getHex());
            zBlockColor7.setCardBackgroundColor(block7.getHex());
            zDifferentColor.clear();

            zCPViews.setTextColor(zChosenColor, selectRandomColor().getHex());
        }
    }

}
