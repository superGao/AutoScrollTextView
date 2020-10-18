package com.example.autoscorllview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.autoscorllview.danmu.BiliDanmukuParser;
import com.example.autoscorllview.danmu.LoadManmuActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.util.IOUtils;
import master.flame.danmaku.ui.widget.DanmakuView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_main_scollText;
    private EditText edt_text;
    private Button btn_change_text;
    private AnimationTextView tv_free_play;
    private RelativeLayout rlScrollText;
    private Button btn_danmu;
    private DanmakuView mDanmakuView;
    private DanmakuContext mContext;
    private BaseDanmakuParser mParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_main_scollText=(TextView)findViewById(R.id.tv_main_scollText);
        edt_text=(EditText)findViewById(R.id.edt_text);
        btn_change_text=(Button)findViewById(R.id.btn_change_text);
        tv_free_play=(AnimationTextView) findViewById(R.id.tv_free_play);
        rlScrollText=(RelativeLayout)findViewById(R.id.rl_scroll_text);
        btn_danmu=(Button)findViewById(R.id.btn_danmu);
        mDanmakuView=(DanmakuView)findViewById(R.id.sv_danmaku);
        btn_change_text.setOnClickListener(this);
        btn_danmu.setOnClickListener(this);

        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mContext = DanmakuContext.create();
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
                //.setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair).setDanmakuMargin(40);
        mParser = createParser(this.getResources().openRawResource(R.raw.comments));
        if (mDanmakuView != null) {

            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
//                    Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
                }

                @Override
                public void prepared() {
                    mDanmakuView.start();
                }
            });
            mDanmakuView.prepare(mParser, mContext);
            mDanmakuView.enableDanmakuDrawingCache(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_change_text:
                hideKeyboard(edt_text);
                String text=edt_text.getText().toString();
                tv_main_scollText.setText(text);
                //tv_main_scollText.requestFocus();
                tv_free_play.setText(text);
                addDanmaku(false,edt_text.getText().toString());
                //tv_marquee.startText(edt_text.getText().toString());
                //tv_marquee.requestFocus();
                break;
            case R.id.btn_danmu:
                startActivity(new Intent(this, LoadManmuActivity.class));
                break;
            default:
                break;
        }
    }

    private void addDanmaku(boolean islive,String text) {
        Log.d("启动弹幕:",text);
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        // for(int i=0;i<100;i++){
        // }
        Log.d("加载弹幕:",text);
        danmaku.text = text;
        danmaku.padding = 5;
        danmaku.priority = 100;  // 低优先级可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = islive;
        danmaku.setTime(mDanmakuView.getCurrentTime() + 10);//滚动时间
        danmaku.textSize = 25f;
        danmaku.textColor = Color.RED;
        //danmaku.textShadowColor = Color.WHITE;//文字描边颜色
        // danmaku.underlineColor = Color.GREEN;
        //danmaku.borderColor = Color.GREEN;//文字边框颜色
        mDanmakuView.addDanmaku(danmaku);

    }

    /**
     * 绘制背景(自定义弹幕样式)
     */
    private static class BackgroundCacheStuffer extends SpannedCacheStuffer {
        // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
        final Paint paint = new Paint();

        @Override
        public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
            danmaku.padding = 10;  // 在背景绘制模式下增加padding
            super.measure(danmaku, paint, fromWorkerThread);
        }

        @Override
        public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
            paint.setColor(0x8125309b);
            canvas.drawRect(left + 2, top + 2, left + danmaku.paintWidth - 2, top + danmaku.paintHeight - 2, paint);
        }

        @Override
        public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
            // 禁用描边绘制
        }
    }

    public static void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    private BaseDanmakuParser createParser(InputStream stream) {

        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }
}