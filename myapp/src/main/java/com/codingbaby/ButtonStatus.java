package com.codingbaby;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.View;

public class ButtonStatus {

    private Context context;

    private View view;


    public boolean selectEnglishWord = true;
    public boolean selectShortEnglish = false;
    public boolean selectJunior = false;


    public boolean english1 = false;
    public boolean english2 = false;
    public boolean english3 = false;
    public boolean english4 = false;
    public boolean english5 = false;
    public boolean english6 = false;

    public boolean selectEnglishForAll = true;


    private boolean longPress = false;


    public ButtonStatus(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    public void onLongClick() {
        longPress = !longPress;
    }


    //功能按钮动画
    private ValueAnimator functionAnimator;

    {
        functionAnimator = ValueAnimator.ofInt(0, 500);
        functionAnimator.setDuration(2000);
        functionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.invalidate();
            }
        });
        functionAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                longPress = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    public void startAnimation() {
        if (!functionAnimator.isStarted()) {
            functionAnimator.start();
        }
    }


    public void drawButton(Canvas canvas, Paint paint) {

        if (!longPress) {
            return;
        }


        int gap = sp2px(40);
        int textSize = sp2px(15);

        paint.setTextSize(textSize);


        int wordY = sp2px(35);


        int radius = sp2px(15);
        int fromX = sp2px(30);
        int fromY = sp2px(30);


        int wordFrom = sp2px(22);

        int n = 0;

        paint.setColor(selectEnglishWord ? Color.BLUE : Color.GRAY);
        canvas.drawCircle(fromX + n * gap, fromY, radius, paint);
        paint.setColor(Color.WHITE);
        canvas.drawText("英", wordFrom + n * gap, wordY, paint);


        n = 1;
        paint.setColor(selectShortEnglish ? Color.BLUE : Color.GRAY);
        canvas.drawCircle(fromX + n * gap, fromY, radius, paint);
        paint.setColor(Color.WHITE);
        canvas.drawText("短", wordFrom + n * gap, wordY, paint);

        n = 2;
        paint.setColor(selectJunior ? Color.BLUE : Color.GRAY);
        canvas.drawCircle(fromX + n * gap, fromY, radius, paint);
        paint.setColor(Color.WHITE);
        canvas.drawText("初", wordFrom + n * gap, wordY, paint);
    }


    public void drawBottomButton(Canvas canvas, Paint paint, int height) {

        if (!longPress) {
            return;
        }

        int gap = sp2px(40);
        int textSize = sp2px(15);
        paint.setTextSize(textSize);


        int wordY = height - sp2px(25);


        int radius = sp2px(15);
        int fromX = sp2px(30);
        int fromY = height - sp2px(30);


        int wordFrom = sp2px(22);


        if (selectEnglishWord) {

            int n = 0;

            paint.setColor(english1 ? Color.BLUE : Color.GRAY);
            canvas.drawCircle(fromX + n * gap, fromY, radius, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText("一", wordFrom, wordY, paint);

            n = 1;

            paint.setColor(english2 ? Color.BLUE : Color.GRAY);
            canvas.drawCircle(fromX + n * gap, fromY, radius, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText("二", wordFrom + n * gap, wordY, paint);


            n = 2;

            paint.setColor(english3 ? Color.BLUE : Color.GRAY);
            canvas.drawCircle(fromX + n * gap, fromY, radius, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText("三", wordFrom + n * gap, wordY, paint);

            n = 3;

            paint.setColor(english4 ? Color.BLUE : Color.GRAY);
            canvas.drawCircle(fromX + n * gap, fromY, radius, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText("四", wordFrom + n * gap, wordY, paint);

            n = 4;

            paint.setColor(english5 ? Color.BLUE : Color.GRAY);
            canvas.drawCircle(fromX + n * gap, fromY, radius, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText("五", wordFrom + n * gap, wordY, paint);

            n = 5;

            paint.setColor(english6 ? Color.BLUE : Color.GRAY);
            canvas.drawCircle(fromX + n * gap, fromY, radius, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText("六", wordFrom + n * gap, wordY, paint);

            n = 6;

            paint.setColor(selectEnglishForAll ? Color.BLUE : Color.GRAY);
            canvas.drawCircle(fromX + n * gap, fromY, radius, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText("全", wordFrom + n * gap, wordY, paint);
        }

    }

    /**
     * 检查是否点击了按钮
     *
     * @param y
     * @param x
     * @return
     */
    public boolean checkFuncTouch(float y, float x) {

        int gap = sp2px(40);
        int textSize = sp2px(15);

        if (!longPress) {
            return false;
        }

        int topButtonX = textSize;
        if (y >= topButtonX && y <= 3 * topButtonX && x >= topButtonX && x <= 3 * topButtonX) {

            selectEnglishWord = true;
            selectShortEnglish = false;
            selectJunior = false;

            return true;
        }

        int n = 1;

        if (y >= topButtonX && y <= 3 * topButtonX && x >= topButtonX + n * gap && x <= 3 * topButtonX + n * gap) {

            selectEnglishWord = false;
            selectShortEnglish = true;
            selectJunior = false;

            return true;

        }

        n = 2;

        if (y >= topButtonX && y <= 3 * topButtonX && x >= topButtonX + n * gap && x <= 3 * topButtonX + n * gap) {

            selectEnglishWord = false;
            selectShortEnglish = false;
            selectJunior = true;

            return true;

        }

        return false;
    }


    public boolean checkBottomTouch(float y, float x, int height) {

        if (!longPress) {
            return false;
        }

        int gap = sp2px(40);
        int textSize = sp2px(15);


        int topButtonX = height - 3 * textSize;

        int n = 0;
        if (y >= topButtonX && y <= height - textSize && x >= textSize + n * gap && x <= 3 * textSize + n * gap) {


            if (selectEnglishWord) {
                english1 = true;
                english2 = false;
                english3 = false;
                english4 = false;
                english5 = false;
                english6 = false;
                selectEnglishForAll = false;
            }

            return true;

        }


        n = 1;
        if (y >= topButtonX && y <= height - textSize && x >= textSize + n * gap && x <= 3 * textSize + n * gap) {


            if (selectEnglishWord) {
                english1 = false;
                english2 = true;
                english3 = false;
                english4 = false;
                english5 = false;
                english6 = false;
                selectEnglishForAll = false;
            }
            return true;


        }

        n = 2;
        if (y >= topButtonX && y <= height - textSize && x >= textSize + n * gap && x <= 3 * textSize + n * gap) {


            if (selectEnglishWord) {
                english1 = false;
                english2 = false;
                english3 = true;
                english4 = false;
                english5 = false;
                english6 = false;
                selectEnglishForAll = false;
            }
            return true;

        }

        n = 3;
        if (y >= topButtonX && y <= height - textSize && x >= textSize + n * gap && x <= 3 * textSize + n * gap) {


            if (selectEnglishWord) {
                english1 = false;
                english2 = false;
                english3 = false;
                english4 = true;
                english5 = false;
                english6 = false;
                selectEnglishForAll = false;
            }

            return true;

        }

        n = 4;
        if (y >= topButtonX && y <= height - textSize && x >= textSize + n * gap && x <= 3 * textSize + n * gap) {


            if (selectEnglishWord) {
                english1 = false;
                english2 = false;
                english3 = false;
                english4 = false;
                english5 = true;
                english6 = false;
                selectEnglishForAll = false;
            }

            return true;

        }


        n = 5;
        if (y >= topButtonX && y <= height - textSize && x >= textSize + n * gap && x <= 3 * textSize + n * gap) {


            if (selectEnglishWord) {
                english1 = false;
                english2 = false;
                english3 = false;
                english4 = false;
                english5 = false;
                english6 = true;

                selectEnglishForAll = false;
            }

            return true;

        }


        n = 6;
        if (y >= topButtonX && y <= height - textSize && x >= textSize + n * gap && x <= 3 * textSize + n * gap) {


            if (selectEnglishWord) {
                english1 = false;
                english2 = false;
                english3 = false;
                english4 = false;
                english5 = false;
                english6 = false;

                selectEnglishForAll = true;
            }

            return true;

        }

        return false;

    }


    /**
     * sp转px
     *
     * @param sp sp值
     * @return px值
     */
    public int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }


}
