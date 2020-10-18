package com.example.autoscorllview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.autoscorllview.danmu.LoadManmuActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_main_scollText;
    private EditText edt_text;
    private Button btn_change_text;
    private AnimationTextView tv_free_play;
    private RelativeLayout rlScrollText;
    private Button btn_danmu;

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
        btn_change_text.setOnClickListener(this);
        btn_danmu.setOnClickListener(this);
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

    public static void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}