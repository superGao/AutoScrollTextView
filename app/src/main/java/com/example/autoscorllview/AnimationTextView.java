package com.example.autoscorllview;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.annotation.Nullable;


@SuppressLint("AppCompatCustomView")

/**
 * Date:2020/10/12
 * Time:14:20
 * Author:高永健
 * Description:文字跑马灯效果
 * 任尔风吹浪打，我自闲庭信步。
 */
public class AnimationTextView extends TextView {
    private static final String TAG = "AnimationTextView";
    private String mText = "";
    private int mTextWidth = 0;
    private int windowWith = 0;
    private int textViewWidth = 0;
    private Paint paint = null;
    private Rect rect = null;
    private TextView textView;
    private TranslateAnimation translateAnimation;
    private int delayTime = 20 * 1000;//动画执行时间
    private int startLocation = 0;//动画起始位置=textView宽度
    private int finishLocation = 0;//动画结束位置=-text宽度
    private int distance = 0;//动画移动距离=动画起始位置-动画结束位置
    private final float speed = 89.2f;//平移速度=移动距离/动画执行时间


    public AnimationTextView(Context context) {
        super(context);
    }

    public AnimationTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimationTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        Log.d(TAG, "onTextChanged：" + text);
        if (!TextUtils.isEmpty(text)) {
            mText = text.toString();
            updateLengthInfo();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged：");
        rect = new Rect();
        paint = getPaint();
        paint.setAntiAlias(true);
        paint.getTextBounds(mText, 0, mText.length(), rect);
        updateLengthInfo();
    }

/*    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "drawText：" + mText);
        canvas.drawText(mText,0, -rect.top + (getHeight() - rect.height()) / 2F, paint);
        canvas.restore();
    }*/

    /**
     * 更新长度信息
     */
    private void updateLengthInfo() {
        textView = this;
        mTextWidth = (int) getPaint().measureText(mText);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        windowWith = displayMetrics.widthPixels;
        textViewWidth = textView.getWidth();

        startLocation = textViewWidth;
        finishLocation = -mTextWidth;
        distance = startLocation - finishLocation;
        delayTime = (int) ((float) distance / speed * 1000);
        Log.d(TAG, "onMeasure" + " startLocation" + startLocation + " " +
                "finishLocation" + finishLocation + " distance" + distance + " delayTime:" + delayTime
                + "windowWith：" + windowWith + " mTextWidth" + mTextWidth + " " + "textViewWidth" + textViewWidth + "  text" + mText);

        if(mTextWidth>textViewWidth){
            initAnimation(0, 0, 0,1);
            Log.d(TAG, "获取焦点");
            textView.requestFocus();
        }else{
            initAnimation(startLocation, finishLocation, delayTime,ValueAnimator.INFINITE);
        }

    }

    /**
     * 初始化动画
     *
     * @param startLocation  起始位置
     * @param finishLocation 结束位置
     * @param delayTime      执行时间
     */
    private void initAnimation(int startLocation, int finishLocation, int delayTime,int RepeatCount) {
        translateAnimation = new TranslateAnimation(startLocation, finishLocation, 0, 0);
        translateAnimation.setDuration(delayTime);
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setRepeatCount(RepeatCount);
        textView.startAnimation(translateAnimation);
    }
}
