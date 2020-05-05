package com.dyoniso.colorparty.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.dyoniso.colorparty.R;
import com.dyoniso.colorparty.adapter.color.ColorAdapter;
import com.dyoniso.colorparty.model.Color;
import com.dyoniso.colorparty.tool.ComboCount;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

interface CPViews {
    void setColorName(Color color, int hex);
    void setColor(Color color);
    void setScore(int score);
    void toleranceTimer(int time);
    void startGameFT();
    void beginTextTransition(String text, Timer timer);
    void failMessage();
    void successMessage();
    void viewChangeType(int type);
}

public class CPFragmentActivity extends FragmentActivity implements CPViews {
    private static int SCORE_VALUE = 0;

    @BindAnim(R.anim.alpha)
    Animation zAlpha;
    @BindView(R.id.color_status)
    CardView zColorStatus;
    @BindView(R.id.tolerance_timer_view)
    TextView zToleranceTimerView;
    @BindView(R.id.score_view)
    TextView zScoreView;
    @BindView(R.id.color_view)
    TextView zColorView;

    private int zMinToNextLevel = 0;

    private ComboCount zComboCount;
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

        zComboCount = new ComboCount();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, zCPFragment4B)
                .commit();
            }

        zAlpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                zToleranceTimerView.clearAnimation();
                zColorView.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

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

        zToleranceTimerView.startAnimation(zAlpha);
        zToleranceTimerView.setVisibility(View.VISIBLE);

        zToleranceTimer = new CountDownTimer(time * 1000, 100) {
            @Override
            public void onTick(long t) {
                zToleranceTimerView.setText(String.valueOf(t/1000));
            }

            @Override
            public void onFinish() {
                startGameFT();
                if (SCORE_VALUE <= 0) {
                    setScore(0);
                } else {
                    setScore(SCORE_VALUE - 1);
                }

                zColorView.setText("Time is over");
                zColorView.setTextColor(getResources().getColor(R.color.color_block_0));
                viewChangeType(0);

                zComboCount.removeCombo();
                zToleranceTimerView.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    @Override
    public void startGameFT() {
        final int minToNextLevel_1 = 20;
        final int minToNextLevel_2 = 30;
        final int minToNextLevel_3 = 40;
        final int minToNextLevel_4 = 50;

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
            zMinToNextLevel = minToNextLevel_1;
        }
        if (SCORE_VALUE == minToNextLevel_1) {
            flipFragment(2);
            zMinToNextLevel = minToNextLevel_2;
        }
        if (SCORE_VALUE == minToNextLevel_1) {
            flipFragment(3);
            zMinToNextLevel = minToNextLevel_3;
        }
        if (SCORE_VALUE == minToNextLevel_1) {
            flipFragment(3);
            zMinToNextLevel = minToNextLevel_4;
        }
    }

    @Override
    public void beginTextTransition(String text, Timer timer) {
        timer.scheduleAtFixedRate(new TimerTask() {
            int count = 1;

            @Override
            public void run() {
                runOnUiThread(() -> {
                    switch (count) {
                        case 1:
                            zColorView.startAnimation(zAlpha);
                            zColorView.setText(text);
                            count = 2;
                            break;

                        case 2:
                            zColorView.startAnimation(zAlpha);
                            zColorView.setText("Click to play");
                            count = 1;
                            break;
                    }
                });
            }
        }, 0, 3000);
    }

    @Override
    public void failMessage() {
        zColorView.setText("Fail! -5");
        zColorView.setTextColor(getResources().getColor(R.color.color_block_0));
        viewChangeType(0);
        zComboCount.removeCombo();
    }

    @Override
    public void successMessage() {
        zColorView.setText(zComboCount.getComboName());
        zColorView.setTextColor(getResources().getColor(R.color.color_block_3));
        viewChangeType(0);
        zComboCount.addCombo();
    }

    @Override
    public void viewChangeType(int type) {
        switch (type) {
            case 0:
                zColorStatus.setCardBackgroundColor(getResources().getColor(R.color.color_dark_gray));
                break;

            case 1:
                zColorStatus.setCardBackgroundColor(getResources().getColor(R.color.color_block_0));
                break;
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
    public void setColorName(Color color, int hex) {
        zColorView.startAnimation(zAlpha);
        zColorView.setText(color.getName());
        zColorView.setTextColor(hex);

        zColorStatus.setCardBackgroundColor(color.getHex());
    }

    @Override
    public void setColor(Color color) {
        zColorView.setTextColor(color.getHex());
    }

    @Override
    public void setScore(int score) {
        SCORE_VALUE = score;
        zScoreView.setText("Sc: "+score+"/"+zMinToNextLevel);
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

        private Timer zBeginMessageTimer;
        private Animation zFadeIn;
        private Animation zFadeOut;
        private AnimationSet zFadeAnimation;
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

            zBeginMessageTimer = new Timer();

            if (zItsFirst) {
                startFadeAnim();
            }
        }

        private void startFadeAnim() {
            CardView[] cardViews = {
                    zBlockColor0,
                    zBlockColor1,
                    zBlockColor2,
                    zBlockColor3
            };

            zFadeIn = new AlphaAnimation(0, 4);
            zFadeIn.setInterpolator(new DecelerateInterpolator());
            zFadeIn.setStartOffset(100);
            zFadeIn.setDuration(1000);
            zFadeIn.setRepeatCount(Animation.INFINITE);

            zFadeOut = new AlphaAnimation(1, 0);
            zFadeOut.setInterpolator(new AccelerateInterpolator());
            zFadeOut.setStartOffset(100);
            zFadeOut.setDuration(1000);
            zFadeOut.setRepeatCount(Animation.INFINITE);

            zFadeAnimation = new AnimationSet(false);
            zFadeAnimation.addAnimation(zFadeIn);
            zFadeAnimation.addAnimation(zFadeOut);

            zCPViews.beginTextTransition("Level 1", zBeginMessageTimer);

            for (CardView cardView : cardViews) {
                cardView.setAnimation(zFadeAnimation);
            }
        }

        private void cancelFadeAnim() {
            if (zBeginMessageTimer != null) {
                zBeginMessageTimer.cancel();
            }

            if (zFadeAnimation != null) {
                zFadeAnimation.cancel();
            }
            if (zFadeIn != null) {
                zFadeIn.cancel();
            }
            if (zFadeOut != null) {
                zFadeOut.cancel();
            }
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            zColorAdapter = new ColorAdapter(getContext(),3);

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
                int count = 0;

                @Override
                public void run() {
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        Color sequential = selectDifferentRandomColor();

                        zBlockColor0.setCardBackgroundColor(sequential.getHex());
                        zBlockColor1.setCardBackgroundColor(sequential.getHex());
                        zBlockColor2.setCardBackgroundColor(sequential.getHex());
                        zBlockColor3.setCardBackgroundColor(sequential.getHex());

                        zCPViews.setColor(sequential);

                        if (count >= 3) {
                            zDifferentColor.clear();
                        }

                        count++;
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
            if (zChosenColor != null || BLOCK_COLOR_0 != null) {
                if (zChosenColor.getID() == BLOCK_COLOR_0.getID()) {
                    addScore(1);
                    zCPViews.successMessage();

                } else {
                    zCPViews.failMessage();
                    removeScore(5);
                }
            }

            zCPViews.startGameFT();
        }

        @OnClick(R.id.color_block_1) void block2() {
            if (zChosenColor != null || BLOCK_COLOR_1 != null) {
                if (zChosenColor.getID() == BLOCK_COLOR_1.getID()) {
                    addScore(1);
                    zCPViews.successMessage();

                } else {
                    zCPViews.failMessage();
                    removeScore(5);
                }
            }

            zCPViews.startGameFT();
        }

        @OnClick(R.id.color_block_2) void block3() {
            if (zChosenColor != null || BLOCK_COLOR_2 != null) {
                if (zChosenColor.getID() == BLOCK_COLOR_2.getID()) {
                    addScore(1);
                    zCPViews.successMessage();

                } else {
                    zCPViews.failMessage();
                    removeScore(5);
                }
            }

            zCPViews.startGameFT();
        }

        @OnClick(R.id.color_block_3) void block4() {
            if (zChosenColor != null || BLOCK_COLOR_3 != null) {
                if (zChosenColor.getID() == BLOCK_COLOR_3.getID()) {
                    addScore(1);
                    zCPViews.successMessage();

                } else {
                    zCPViews.failMessage();
                    removeScore(5);
                }
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
            cancelFadeAnim();

            if (zTickBlock != null || zTimer != null) {
                zTickBlock.cancel();
                zTimer.cancel();
            }

            zChosenColor =  selectRandomColor();
            zTickBlock = new Timer();
            zTimer = new Timer();

            if (zItsFirst) {
                sequentialBlocks(zTimer);

                zCPViews.viewChangeType(1);
                zCPViews.setColorName(zChosenColor, selectRandomColor().getHex());

                new CountDownTimer(2200, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        zItsFirst = false;
                        rotationBlocks(zTimer);

                        new CountDownTimer(1000, 100) {
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

                new CountDownTimer(800, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        zTimer.cancel();
                        blockTick(zTickBlock, 1000, 2000);
                        zCPViews.toleranceTimer(4);

                        zCPViews.viewChangeType(1);
                        zCPViews.setColorName(zChosenColor, selectRandomColor().getHex());
                    }
                }.start();
            }
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

            zCPViews.setColorName(zChosenColor, selectRandomColor().getHex());
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

            zCPViews.setColorName(zChosenColor, selectRandomColor().getHex());
        }
    }

}
