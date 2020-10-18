package com.example.autoscorllview;

/**
 * Creat by:supergao
 * Date:2020,2020/10/11
 * Time:8:58 PM
 * Describe:
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.annotation.SuppressLint;

@SuppressLint("AppCompatCustomView")

public class MarqueeTextView extends TextView {

    // 刚显示时不滚动保持的时间
    private static final int FIRST_HOLD_TIME = 100;
    // 滚动之间的间隔
    private static final int MARGIN_BETWEEN = 100;
    // 每次移动的横跨长度，可用于控制速度
    private static final int MOVE_STEP = 5;

    // 文字宽度
    private int textWidth;

    // 控件宽度
    private int viewWidth;

    private Paint paint;

    private Paint.FontMetricsInt metrics;

    // 文字X,Y轴的坐标
    private float posX1, posX2, posY;

    // 中间的间隙
    private int marginBetween;

    // 每次绘制时移动的距离
    private int moveStep;

    // 第一次绘制时停留的时间（单位毫秒）
    private int firstHoldTime;

    private String text="test";


    public MarqueeTextView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public MarqueeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MarqueeTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        text = getText().toString();
        paint = getPaint();
        metrics = new Paint.FontMetricsInt();
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MarqueeTextView);
            marginBetween = array.getDimensionPixelSize(R.styleable.MarqueeTextView_marquee_margin_between, MARGIN_BETWEEN);
            moveStep = array.getInt(R.styleable.MarqueeTextView_marquee_move_step, MOVE_STEP);
            firstHoldTime = array.getInt(R.styleable.MarqueeTextView_marquee_first_hold_time, FIRST_HOLD_TIME);
            array.recycle();
        } else {
            marginBetween = MARGIN_BETWEEN;
            moveStep = MOVE_STEP;
            firstHoldTime = FIRST_HOLD_TIME;
        }

        // 计算得到文字的宽度和控件的宽度
        textWidth = (int) paint.measureText(text);
        viewWidth = getWidth();

        paint.getFontMetricsInt(metrics);
        posY = Math.abs(metrics.top) + getPaddingTop();

    }

    public void startText(String text) {
        posX1 = 0;
        invalidate();
    }


    @Override
    public void onDraw(Canvas canvas) {
        calculatePosition();
        canvas.drawText(text, posX1, posY, paint);
        canvas.drawText(text, posX2, posY, paint);
        invalidate();
    }

    /**
     * 计算位置
     */
    private void calculatePosition() {
        posX1 -= moveStep;
        // 如果第一个文本还在显示，则第二个文本按照第一个文本的位置计算
        if (posX1 < 0 && Math.abs(posX1) <= textWidth) {
            posX2 = posX1 + textWidth + marginBetween;
        } else {
            posX2 -= moveStep;
            posX1 = posX2 + textWidth + marginBetween;
        }
    }
}
