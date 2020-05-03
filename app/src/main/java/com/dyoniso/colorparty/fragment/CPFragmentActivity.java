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
    void setTextColorName(Color color, int hex);
    void setTextColor(Color color);
    void setScore(int score);
    void toleranceTimer(int time);
    void startGameFT();
}

public class CPFragmentActivity extends FragmentActivity implements CPViews {
    private static int SCORE_VALUE = 0;

    @BindView(R.id.tolerance_timer_view)
    TextView zToleranceTimerView;
    @BindView(R.id.score_view)
    TextView zScoreView;
    @BindView(R.id.color_view)
    TextView zColorView;

    private int zLevelType;
    private CountDownTimer zToleranceTimer;
    private CPFragment4B zCPFragment4B;
    private CPFragment6B zCPFragment6B;
    private CPFragment8B zCPFragment8B;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_trasition);
        ButterKnife.bind(this);

        zCPFragment4B = new CPFragment4B(this);
        zCPFragment6B = new CPFragment6B(this);
        zCPFragment8B = new CPFragment8B(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, zCPFragment4B)
                .commit();
            }

        zLevelType = 1;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (zToleranceTimer != null) {
            zToleranceTimer.cancel();
        }
    }

    @Override
    public void toleranceTimer(int time) {
        if (zToleranceTimer != null) {
            zToleranceTimer.cancel();
        }

        zToleranceTimerView.setVisibility(View.VISIBLE);

        zToleranceTimer = new CountDownTimer(time * 1000, 100) {
            @Override
            public void onTick(long t) {
                zToleranceTimerView.setText("Resta: "+t/1000);
            }

            @Override
            public void onFinish() {
                startGame();
                setScore(SCORE_VALUE - 1);
                zToleranceTimerView.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    @OnClick(R.id.btn_trasition) void trasition() {
        flipFragment(2);
    }

    @Override
    public void startGameFT() {
        startGame();
    }

    @OnClick(R.id.btn_start_game) void startGame() {
        switch (zLevelType) {
            case 1:
                zCPFragment4B.choseColor();
                break;

            case 2:
                zCPFragment6B.choseColor();
                break;

            case 3:
                zCPFragment8B.choseColor();
                break;
        }

        if (SCORE_VALUE <= 0) {
            flipFragment(1);
        }
        if (SCORE_VALUE == 1) {
            flipFragment(2);
        }
        if (SCORE_VALUE == 2) {
            flipFragment(3);
        }
        if (SCORE_VALUE == 3) {
            flipFragment(3);
        }
    }

    public void flipFragment(int fType) {
        FragmentTransaction fT = getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out);

        switch (fType) {
            case 1:
                fT.replace(R.id.container, zCPFragment4B);
                fT.addToBackStack(null);
                zLevelType = 1;
                break;

            case 2:
                fT.replace(R.id.container, zCPFragment6B);
                zLevelType = 2;
                break;

            case 3:
                fT.replace(R.id.container, zCPFragment8B);
                zLevelType = 3;
                break;

            default:
                getSupportFragmentManager().popBackStack();
        }
        fT.commit();
    }

    @Override
    public void setTextColorName(Color color, int hex) {
        zColorView.setText(color.getName());
        zColorView.setTextColor(hex);
    }

    @Override
    public void setTextColor(Color color) {
        zColorView.setTextColor(color.getHex());
    }

    @Override
    public void setScore(int score) {
        SCORE_VALUE = score;
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

        private Timer zTickBlock;
        private boolean zItsFirst = true;
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

            zColorAdapter = new ColorAdapter(getContext(),3);
            cScore();
        }

        private void blockTick(Timer timer, int delay, int period) {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (getActivity() != null) {
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
                }
            }, delay, period);

        }

        private void sequentialBlocks(Timer timer) {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        Color sequential = selectDifferentRandomColor();

                        zBlockColor0.setCardBackgroundColor(sequential.getHex());
                        zBlockColor1.setCardBackgroundColor(sequential.getHex());
                        zBlockColor2.setCardBackgroundColor(sequential.getHex());
                        zBlockColor3.setCardBackgroundColor(sequential.getHex());

                        zCPViews.setTextColor(sequential);

                        zDifferentColor.clear();
                    });
                }
            }, 40, 250);
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

            zCPViews.startGameFT();
        }

        @OnClick(R.id.color_block_1) void block2() {
            if (zChosenColor.getID() == BLOCK_COLOR_1.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_1.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            zCPViews.startGameFT();
        }

        @OnClick(R.id.color_block_2) void block3() {
            if (zChosenColor.getID() == BLOCK_COLOR_2.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_2.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            zCPViews.startGameFT();
        }

        @OnClick(R.id.color_block_3) void block4() {
            if (zChosenColor.getID() == BLOCK_COLOR_3.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_3.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            zCPViews.startGameFT();
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
            if (zTickBlock != null || zTimer != null) {
                zTickBlock.cancel();
                zTimer.cancel();
            }

            zChosenColor =  selectRandomColor();
            zTickBlock = new Timer();
            zTimer = new Timer();

            if (zItsFirst) {
                sequentialBlocks(zTimer);

                new CountDownTimer(3500, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        zItsFirst = false;
                        rotationBlocks(zTimer);

                        new CountDownTimer(1500, 100) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                zTimer.cancel();
                                blockTick(zTickBlock, 1000, 2000);
                                zCPViews.toleranceTimer(4);
                            }
                        }.start();
                    }
                }.start();

            } else {
                rotationBlocks(zTimer);

                new CountDownTimer(1500, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        zTimer.cancel();
                        blockTick(zTickBlock, 1000, 2000);
                        zCPViews.toleranceTimer(4);
                    }
                }.start();
            }

            zCPViews.setTextColorName(zChosenColor, selectRandomColor().getHex());
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

        private Color BLOCK_COLOR_0 = null;
        private Color BLOCK_COLOR_1 = null;
        private Color BLOCK_COLOR_2 = null;
        private Color BLOCK_COLOR_3 = null;
        private Color BLOCK_COLOR_4 = null;
        private Color BLOCK_COLOR_5 = null;

        private Timer zTickBlock;
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

            zColorAdapter = new ColorAdapter(getContext(), 5);
        }

        @Override
        public void onResume() {
            super.onResume();
            choseColor();
        }

        @OnClick(R.id.color_block_0) void block0() {
            if (zChosenColor.getID() == BLOCK_COLOR_0.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_0.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            zCPViews.startGameFT();
        }

        @OnClick(R.id.color_block_1) void block1() {
            if (zChosenColor.getID() == BLOCK_COLOR_1.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_1.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            zCPViews.startGameFT();
        }

        @OnClick(R.id.color_block_2) void block2() {
            if (zChosenColor.getID() == BLOCK_COLOR_2.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_2.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            zCPViews.startGameFT();
        }

        @OnClick(R.id.color_block_3) void block3() {
            if (zChosenColor.getID() == BLOCK_COLOR_3.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_3.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            zCPViews.startGameFT();
        }

        @OnClick(R.id.color_block_4) void block4() {
            if (zChosenColor.getID() == BLOCK_COLOR_4.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_4.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            zCPViews.startGameFT();
        }

        @OnClick(R.id.color_block_5) void block5() {
            if (zChosenColor.getID() == BLOCK_COLOR_5.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_5.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            zCPViews.startGameFT();
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

        private void blockTick(Timer timer, int delay, int period) {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (getActivity() != null) {
                        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                            BLOCK_COLOR_0 = selectDifferentRandomColor();
                            BLOCK_COLOR_1 = selectDifferentRandomColor();
                            BLOCK_COLOR_2 = selectDifferentRandomColor();
                            BLOCK_COLOR_3 = selectDifferentRandomColor();
                            BLOCK_COLOR_4 = selectDifferentRandomColor();
                            BLOCK_COLOR_5 = selectDifferentRandomColor();

                            zBlockColor0.setCardBackgroundColor(BLOCK_COLOR_0.getHex());
                            zBlockColor1.setCardBackgroundColor(BLOCK_COLOR_1.getHex());
                            zBlockColor2.setCardBackgroundColor(BLOCK_COLOR_2.getHex());
                            zBlockColor3.setCardBackgroundColor(BLOCK_COLOR_3.getHex());
                            zBlockColor4.setCardBackgroundColor(BLOCK_COLOR_4.getHex());
                            zBlockColor5.setCardBackgroundColor(BLOCK_COLOR_5.getHex());

                            zDifferentColor.clear();
                        });
                    }
                }
            }, delay, period);

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
                    if (getActivity() != null) {
                        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                            BLOCK_COLOR_0 = selectDifferentRandomColor();
                            BLOCK_COLOR_1 = selectDifferentRandomColor();
                            BLOCK_COLOR_2 = selectDifferentRandomColor();
                            BLOCK_COLOR_3 = selectDifferentRandomColor();
                            BLOCK_COLOR_4 = selectDifferentRandomColor();
                            BLOCK_COLOR_5 = selectDifferentRandomColor();

                            zBlockColor0.setCardBackgroundColor(BLOCK_COLOR_0.getHex());
                            zBlockColor1.setCardBackgroundColor(BLOCK_COLOR_1.getHex());
                            zBlockColor2.setCardBackgroundColor(BLOCK_COLOR_2.getHex());
                            zBlockColor3.setCardBackgroundColor(BLOCK_COLOR_3.getHex());
                            zBlockColor4.setCardBackgroundColor(BLOCK_COLOR_4.getHex());
                            zBlockColor5.setCardBackgroundColor(BLOCK_COLOR_5.getHex());

                            zDifferentColor.clear();
                        });
                    }
                }
            }, 10, 100);
        }

        public void choseColor() {
            if (zTimer != null || zTickBlock != null) {
                zTimer.cancel();
                zTickBlock.cancel();
            }

            zChosenColor =  selectRandomColor();

            zTimer = new Timer();
            zTickBlock = new Timer();
            rotationBlocks(zTimer);

            new CountDownTimer(3000, 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    zTimer.cancel();
                    zCPViews.toleranceTimer(4);
                    blockTick(zTickBlock, 1000, 2000);
                }
            }.start();

            zCPViews.setTextColorName(zChosenColor, selectRandomColor().getHex());
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

        private Color BLOCK_COLOR_0 = null;
        private Color BLOCK_COLOR_1 = null;
        private Color BLOCK_COLOR_2 = null;
        private Color BLOCK_COLOR_3 = null;
        private Color BLOCK_COLOR_4 = null;
        private Color BLOCK_COLOR_5 = null;
        private Color BLOCK_COLOR_6 = null;
        private Color BLOCK_COLOR_7 = null;

        private Timer zTickBlock;
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

            zColorAdapter = new ColorAdapter(getContext(), 7);
        }

        @Override
        public void onResume() {
            super.onResume();
            choseColor();
        }

        @OnClick(R.id.color_block_0) void block0() {
            if (zChosenColor.getID() == BLOCK_COLOR_0.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_0.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            zCPViews.startGameFT();
        }

        @OnClick(R.id.color_block_1) void block1() {
            if (zChosenColor.getID() == BLOCK_COLOR_1.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_1.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            zCPViews.startGameFT();
        }

        @OnClick(R.id.color_block_2) void block2() {
            if (zChosenColor.getID() == BLOCK_COLOR_2.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_2.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            zCPViews.startGameFT();
        }

        @OnClick(R.id.color_block_3) void block3() {
            if (zChosenColor.getID() == BLOCK_COLOR_3.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_3.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            zCPViews.startGameFT();
        }

        @OnClick(R.id.color_block_4) void block4() {
            if (zChosenColor.getID() == BLOCK_COLOR_4.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_4.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            zCPViews.startGameFT();
        }

        @OnClick(R.id.color_block_5) void block5() {
            if (zChosenColor.getID() == BLOCK_COLOR_5.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_5.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            zCPViews.startGameFT();
        }


        @OnClick(R.id.color_block_6) void block6() {
            if (zChosenColor.getID() == BLOCK_COLOR_6.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_6.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            zCPViews.startGameFT();
        }

        @OnClick(R.id.color_block_7) void block7() {
            if (zChosenColor.getID() == BLOCK_COLOR_7.getID()) {
                addScore(1);
                Log.e(TAG, "Correct "+BLOCK_COLOR_7.getID());

            } else {
                Log.e(TAG, "Error");
                removeScore(5);
            }

            zCPViews.startGameFT();
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
                        BLOCK_COLOR_0 = selectDifferentRandomColor();
                        BLOCK_COLOR_1 = selectDifferentRandomColor();
                        BLOCK_COLOR_2 = selectDifferentRandomColor();
                        BLOCK_COLOR_3 = selectDifferentRandomColor();
                        BLOCK_COLOR_4 = selectDifferentRandomColor();
                        BLOCK_COLOR_5 = selectDifferentRandomColor();
                        BLOCK_COLOR_6 = selectDifferentRandomColor();
                        BLOCK_COLOR_7 = selectDifferentRandomColor();

                        zBlockColor0.setCardBackgroundColor(BLOCK_COLOR_0.getHex());
                        zBlockColor1.setCardBackgroundColor(BLOCK_COLOR_1.getHex());
                        zBlockColor2.setCardBackgroundColor(BLOCK_COLOR_2.getHex());
                        zBlockColor3.setCardBackgroundColor(BLOCK_COLOR_3.getHex());
                        zBlockColor4.setCardBackgroundColor(BLOCK_COLOR_4.getHex());
                        zBlockColor5.setCardBackgroundColor(BLOCK_COLOR_5.getHex());
                        zBlockColor6.setCardBackgroundColor(BLOCK_COLOR_6.getHex());
                        zBlockColor7.setCardBackgroundColor(BLOCK_COLOR_7.getHex());

                        zDifferentColor.clear();
                    });
                }
            }, 10, 100);
        }

        public void choseColor() {
            if (zTimer != null || zTickBlock != null) {
                zTimer.cancel();
                zTickBlock.cancel();
            }

            zChosenColor =  selectRandomColor();

            zTickBlock = new Timer();
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

            zCPViews.setTextColorName(zChosenColor, selectRandomColor().getHex());
        }
    }

}
