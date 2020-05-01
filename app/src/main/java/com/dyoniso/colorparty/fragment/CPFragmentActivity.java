package com.dyoniso.colorparty.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
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
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
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

        private Color BLOCK_COLOR_0 = null;
        private Color BLOCK_COLOR_1 = null;
        private Color BLOCK_COLOR_2 = null;
        private Color BLOCK_COLOR_3 = null;

        @BindView(R.id.color_block_0)
        CardView zBlockColor0;
        @BindView(R.id.color_block_1)
        CardView zBlockColor1;
        @BindView(R.id.color_block_2)
        CardView zBlockColor2;
        @BindView(R.id.color_block_3)
        CardView zBlockColor3;

        private Timer zTimer;
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

        private void rotationBlocks(Timer timer) {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        BLOCK_COLOR_0 = selectDifferentRandomColor();
                        BLOCK_COLOR_1 = selectDifferentRandomColor();
                        BLOCK_COLOR_2 = selectDifferentRandomColor();
                        BLOCK_COLOR_3 = selectDifferentRandomColor();

                        zBlockColor0.setCardBackgroundColor(BLOCK_COLOR_0.getHex());
                        zBlockColor1.setCardBackgroundColor(BLOCK_COLOR_1.getHex());
                        zBlockColor2.setCardBackgroundColor(BLOCK_COLOR_2.getHex());
                        zBlockColor3.setCardBackgroundColor(BLOCK_COLOR_3.getHex());

                        zDifferentColor.clear();
                    });
                }
            }, 10, 100);
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
            if (zChosenColor.getID() == BLOCK_COLOR_0.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_0.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_1) void block2() {
            if (zChosenColor.getID() == BLOCK_COLOR_1.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_1.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_2) void block3() {
            if (zChosenColor.getID() == BLOCK_COLOR_2.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_2.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_3) void block4() {
            if (zChosenColor.getID() == BLOCK_COLOR_3.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_3.getID());

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

        @Override
        public void onPause() {
            super.onPause();
            if (zTimer != null) {
                zTimer.cancel();
            }
        }

        public void choseColor() {
            zChosenColor =  selectRandomColor();

            zTimer = new Timer();
            rotationBlocks(zTimer);

            new CountDownTimer(3000, 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    zTimer.cancel();
                }
            }.start();

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

        private Color BLOCK_BLOCK_0 = null;
        private Color BLOCK_BLOCK_1 = null;
        private Color BLOCK_BLOCK_2 = null;
        private Color BLOCK_BLOCK_3 = null;
        private Color BLOCK_BLOCK_4 = null;
        private Color BLOCK_BLOCK_5 = null;

        private Timer zTimer;
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
            if (zChosenColor.getID() == BLOCK_BLOCK_0.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_BLOCK_0.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_1) void block1() {
            if (zChosenColor.getID() == BLOCK_BLOCK_1.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_BLOCK_1.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_2) void block2() {
            if (zChosenColor.getID() == BLOCK_BLOCK_2.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_BLOCK_2.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_3) void block3() {
            if (zChosenColor.getID() == BLOCK_BLOCK_3.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_BLOCK_3.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_4) void block4() {
            if (zChosenColor.getID() == BLOCK_BLOCK_4.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_BLOCK_4.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_5) void block5() {
            if (zChosenColor.getID() == BLOCK_BLOCK_5.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_BLOCK_5.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @Override
        public void onPause() {
            super.onPause();
            if (zTimer != null) {
                zTimer.cancel();
            }
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

        private void rotationBlocks(Timer timer) {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        BLOCK_BLOCK_0 = selectDifferentRandomColor();
                        BLOCK_BLOCK_1 = selectDifferentRandomColor();
                        BLOCK_BLOCK_2 = selectDifferentRandomColor();
                        BLOCK_BLOCK_3 = selectDifferentRandomColor();
                        BLOCK_BLOCK_4 = selectDifferentRandomColor();
                        BLOCK_BLOCK_5 = selectDifferentRandomColor();

                        zBlockColor0.setCardBackgroundColor(BLOCK_BLOCK_0.getHex());
                        zBlockColor1.setCardBackgroundColor(BLOCK_BLOCK_1.getHex());
                        zBlockColor2.setCardBackgroundColor(BLOCK_BLOCK_2.getHex());
                        zBlockColor3.setCardBackgroundColor(BLOCK_BLOCK_3.getHex());
                        zBlockColor4.setCardBackgroundColor(BLOCK_BLOCK_4.getHex());
                        zBlockColor5.setCardBackgroundColor(BLOCK_BLOCK_5.getHex());

                        zDifferentColor.clear();
                    });
                }
            }, 10, 100);
        }

        public void choseColor() {
            zChosenColor =  selectRandomColor();

            zTimer = new Timer();
            rotationBlocks(zTimer);

            new CountDownTimer(3000, 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    zTimer.cancel();
                }
            }.start();

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

        private Color BLOCK_BLOCK_0 = null;
        private Color BLOCK_BLOCK_1 = null;
        private Color BLOCK_BLOCK_2 = null;
        private Color BLOCK_BLOCK_3 = null;
        private Color BLOCK_BLOCK_4 = null;
        private Color BLOCK_BLOCK_5 = null;
        private Color BLOCK_BLOCK_6 = null;
        private Color BLOCK_BLOCK_7 = null;

        private Timer zTimer;
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
            if (zChosenColor.getID() == BLOCK_BLOCK_0.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_BLOCK_0.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_1) void block1() {
            if (zChosenColor.getID() == BLOCK_BLOCK_1.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_BLOCK_1.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_2) void block2() {
            if (zChosenColor.getID() == BLOCK_BLOCK_2.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_BLOCK_2.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_3) void block3() {
            if (zChosenColor.getID() == BLOCK_BLOCK_3.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_BLOCK_3.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_4) void block4() {
            if (zChosenColor.getID() == BLOCK_BLOCK_4.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_BLOCK_4.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_5) void block5() {
            if (zChosenColor.getID() == BLOCK_BLOCK_5.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_BLOCK_5.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }


        @OnClick(R.id.color_block_6) void block6() {
            if (zChosenColor.getID() == BLOCK_BLOCK_6.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_BLOCK_6.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @OnClick(R.id.color_block_7) void block7() {
            if (zChosenColor.getID() == BLOCK_BLOCK_7.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_BLOCK_7.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            choseColor();
        }

        @Override
        public void onPause() {
            super.onPause();
            if (zTimer != null) {
                zTimer.cancel();
            }
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

        private void rotationBlocks(Timer timer) {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        BLOCK_BLOCK_0 = selectDifferentRandomColor();
                        BLOCK_BLOCK_1 = selectDifferentRandomColor();
                        BLOCK_BLOCK_2 = selectDifferentRandomColor();
                        BLOCK_BLOCK_3 = selectDifferentRandomColor();
                        BLOCK_BLOCK_4 = selectDifferentRandomColor();
                        BLOCK_BLOCK_5 = selectDifferentRandomColor();
                        BLOCK_BLOCK_6 = selectDifferentRandomColor();
                        BLOCK_BLOCK_7 = selectDifferentRandomColor();

                        zBlockColor0.setCardBackgroundColor(BLOCK_BLOCK_0.getHex());
                        zBlockColor1.setCardBackgroundColor(BLOCK_BLOCK_1.getHex());
                        zBlockColor2.setCardBackgroundColor(BLOCK_BLOCK_2.getHex());
                        zBlockColor3.setCardBackgroundColor(BLOCK_BLOCK_3.getHex());
                        zBlockColor4.setCardBackgroundColor(BLOCK_BLOCK_4.getHex());
                        zBlockColor5.setCardBackgroundColor(BLOCK_BLOCK_5.getHex());
                        zBlockColor6.setCardBackgroundColor(BLOCK_BLOCK_6.getHex());
                        zBlockColor7.setCardBackgroundColor(BLOCK_BLOCK_7.getHex());

                        zDifferentColor.clear();
                    });
                }
            }, 10, 100);
        }

        public void choseColor() {
            zChosenColor =  selectRandomColor();

            zTimer = new Timer();
            rotationBlocks(zTimer);

            new CountDownTimer(3000, 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    zTimer.cancel();
                }
            }.start();

            zCPViews.setTextColor(zChosenColor, selectRandomColor().getHex());
        }
    }

}
