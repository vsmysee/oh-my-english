package com.codingbaby;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class CustomTextView extends View {

    private static String mp3Url = "http://dict.youdao.com/dictvoice?type=1&audio=";

    private static int DEFAULT_COLOR = Color.BLACK;

    private ExecutorService executorService = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1), new ThreadPoolExecutor.DiscardPolicy());


    // 画笔
    private Paint paint = new Paint();


    //虚线笔
    Paint virtualLine = new Paint();
    Paint virtualLineBlue = new Paint();

    {
        virtualLine.setStyle(Paint.Style.STROKE);
        virtualLine.setAntiAlias(true);
        virtualLine.setStrokeWidth(3);
        virtualLine.setColor(Color.RED);
        virtualLine.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));
    }

    {
        virtualLineBlue.setStyle(Paint.Style.STROKE);
        virtualLineBlue.setAntiAlias(true);
        virtualLineBlue.setStrokeWidth(3);
        virtualLineBlue.setColor(Color.GRAY);
        virtualLineBlue.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));
    }



    private List<LinePoint> drawLinePoint = new ArrayList<>();


    private List<String> shortEnglish;

    private String englishWord;

    public static DataHolder dataHolder;


    private ButtonStatus buttonStatus;


    final MediaPlayer mediaPlayer = new MediaPlayer();


    private float englishFirstY = 0;
    private float englishEndY = 0;


    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);


        buttonStatus = new ButtonStatus(context, this);


        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                buttonStatus.onLongClick();


                invalidate();
                return true;
            }
        });

    }


    private void initPint() {

        paint.setAntiAlias(true);
        paint.setColor(DEFAULT_COLOR);
        paint.setTextSize(sp2px(30));

    }


    @Override
    public void draw(Canvas canvas) {

        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();


        //draw background
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);


        //draw button
        buttonStatus.drawButton(canvas, paint);

        initPint();

        buttonStatus.drawBottomButton(canvas, paint, getHeight());

        canvas.translate(getWidth() / 2, getHeight() / 2);


        if (buttonStatus.selectEnglishWord) {
            drawEnglishWord(canvas);
        }

        if (buttonStatus.selectShortEnglish) {
            drawShortEnglish(canvas);
        }

        super.draw(canvas);

    }


    //触摸屏幕
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            float y = event.getY();
            float x = event.getX();


            if (buttonStatus.selectEnglishWord) {
                float v = y - getHeight() / 2;
                if (v > englishEndY || v < englishFirstY) {
                    englishWord = dataHolder.randEnglish(buttonStatus);
                }
            }

            if (buttonStatus.selectShortEnglish) {
                shortEnglish = dataHolder.randShortEnglish();
            }

            if (buttonStatus.checkFuncTouch(y, x)) {
                buttonStatus.startAnimation();
            }

            if (buttonStatus.checkBottomTouch(y, x, getHeight())) {
                buttonStatus.startAnimation();
            }


            invalidate();

        }

        return super.onTouchEvent(event);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {

            case KeyEvent.KEYCODE_VOLUME_UP:


                if (buttonStatus.selectEnglishWord) {

                    try {
                        String englishWordPop = dataHolder.popEnglish();
                        if (englishWordPop.equals(englishWord)) {
                            englishWord = dataHolder.popEnglish();
                        } else {
                            englishWord = englishWordPop;
                        }
                    } catch (Exception e) {
                        englishWord = dataHolder.randEnglish(buttonStatus);
                    }

                    invalidate();
                }


                return true;


            case KeyEvent.KEYCODE_VOLUME_DOWN:

                drawLinePoint.clear();


                if (buttonStatus.selectEnglishWord) {
                    englishWord = dataHolder.randEnglish(buttonStatus);
                }

                if (buttonStatus.selectShortEnglish) {
                    shortEnglish = dataHolder.randShortEnglish();
                }


                invalidate();
                return true;

        }

        return super.onKeyDown(keyCode, event);
    }


    private void drawEnglishWord(Canvas canvas) {

        if (englishWord == null) {
            englishWord = dataHolder.randEnglish(buttonStatus);
        }

        paint.setTextSize(sp2px(30));

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        // 文本高度
        float textHeight = fontMetrics.bottom - fontMetrics.top;

        List<String> rows = new ArrayList<>();


        String theWord = englishWord;
        int start = englishWord.indexOf("[");

        if (start == -1) {
            String first = theWord.substring(0, 1);
            char c = first.toUpperCase().charAt(0);
            List<String> strings = dataHolder.getByChar(c);
            for (String string : strings) {
                String w = string.substring(0, string.indexOf(" "));
                if (w.equals(theWord)) {
                    englishWord = string;
                    break;
                }
            }
        }

        start = englishWord.indexOf("[");
        if (start != -1) {
            theWord = englishWord.substring(0, start);
            rows.add(theWord);

            int end = englishWord.indexOf("]");
            rows.add(englishWord.substring(start, end + 1));
            rows.add(englishWord.substring(end + 1));
        } else {
            rows.add(theWord);
        }

        int textLines = rows.size();

        // 中间文本的baseline
        float ascent = paint.ascent();
        float descent = paint.descent();

        float abs = Math.abs(ascent + descent);
        float centerBaselineY = abs / 2;


        final String voice = theWord.trim();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    File sdCard = Environment.getExternalStorageDirectory();
                    File dir = new File(sdCard.getAbsolutePath() + "/poem");
                    if (!dir.exists()) {
                        dir.mkdir();
                    }

                    File file = new File(dir, voice + ".mp3");
                    if (!file.exists()) {
                        file.createNewFile();
                        URLConnection conn = new URL(mp3Url + voice).openConnection();
                        InputStream is = conn.getInputStream();
                        OutputStream outstream = new FileOutputStream(file);
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = is.read(buffer)) > 0) {
                            outstream.write(buffer, 0, len);
                        }
                        outstream.close();
                    }

                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(getContext(), Uri.parse("file:" + file.getAbsolutePath()));
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        for (int i = 0; i < textLines; i++) {

            if (i == 0) {
                paint.setColor(Color.BLUE);
            } else {
                paint.setColor(DEFAULT_COLOR);
            }

            float baseY = centerBaselineY + (i - (textLines - 1) / 2.0f) * textHeight;

            if (i == 0) {
                englishFirstY = baseY;
            }

            if (i == textLines - 1) {
                englishEndY = baseY;
            }

            String content = rows.get(i);

            float textWidth = paint.measureText(content);

            canvas.drawText(content, -textWidth / 2, baseY, paint);
            if (i == 0 || i == 1) {
                canvas.drawLine(-textWidth, baseY + descent, textWidth, baseY + descent, paint);
            }
        }

        canvas.drawLine(-getWidth() / 2, englishFirstY + ascent, getWidth() / 2, englishFirstY + ascent, virtualLineBlue);
        canvas.drawLine(-getWidth() / 2, englishEndY + 2 * descent, getWidth() / 2, englishEndY + 2 * descent, virtualLineBlue);


    }

    private void drawShortEnglish(Canvas canvas) {

        if (shortEnglish == null) {
            shortEnglish = dataHolder.randShortEnglish();
        }

        paint.setColor(DEFAULT_COLOR);

        paint.setTextSize(sp2px(18));

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        // 文本高度
        float textHeight = fontMetrics.bottom - fontMetrics.top;

        List<String> rows = new ArrayList<>();

        for (String english : shortEnglish) {
            rows.add(english);
        }


        int textLines = rows.size();

        // 中间文本的baseline
        float ascent = paint.ascent();
        float descent = paint.descent();

        float abs = Math.abs(ascent + descent);
        float centerBaselineY = abs / 2;

        for (int i = 0; i < textLines; i++) {

            float baseY = centerBaselineY + (i - (textLines - 1) / 2.0f) * textHeight;

            String content = rows.get(i);
            float textWidth = paint.measureText(content);

            canvas.drawText(content, -textWidth / 2, baseY, paint);
        }

    }


    /**
     * dp转px
     *
     * @param dp dp值
     * @return px值
     */
    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }


    /**
     * sp转px
     *
     * @param sp sp值
     * @return px值
     */
    public int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getContext().getResources().getDisplayMetrics());
    }

    static class LinePoint {
        private float sx;
        private float sy;
        private float ex;
        private float ey;

        public LinePoint(float sx, float sy, float ex, float ey) {
            this.sx = sx;
            this.sy = sy;
            this.ex = ex;
            this.ey = ey;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LinePoint linePoint = (LinePoint) o;
            return Float.compare(linePoint.sx, sx) == 0 &&
                    Float.compare(linePoint.sy, sy) == 0 &&
                    Float.compare(linePoint.ex, ex) == 0 &&
                    Float.compare(linePoint.ey, ey) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(sx, sy, ex, ey);
        }
    }
}
