package com.bawei.chaoyang.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bawei.chaoyang.R;

public class MyAdderSubtractor extends LinearLayout {

    private TextView txt_jian;
    private EditText et_num;
    private TextView txt_jia;
    private int i;
    MyAdderSubtractorLisener myAdderSubtractorLisener;
    public void setMyAdderSubtractorLisener(MyAdderSubtractorLisener myAdderSubtractorLisener){
        this.myAdderSubtractorLisener=myAdderSubtractorLisener;
    }

    public MyAdderSubtractor(Context context) {
        super(context);
    }

    public MyAdderSubtractor(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.addersubtractor_layout,this);
        txt_jian = findViewById(R.id.txt_jian);
        et_num = findViewById(R.id.et_num);
        txt_jia = findViewById(R.id.txt_jia);
        i = new Integer(et_num.getText().toString());
        txt_jian.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i <2){
                    Toast.makeText(context, "这是最后一个了哦", Toast.LENGTH_SHORT).show();
                }else{
                    i--;
                    String num = String.valueOf(i);
                    et_num.setText(num);
                    myAdderSubtractorLisener.jian();
                }
            }
        });
        txt_jia.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                String num = String.valueOf(i);
                et_num.setText(num);
                myAdderSubtractorLisener.jia();
            }
        });

    }

    public MyAdderSubtractor(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public interface MyAdderSubtractorLisener{
        void jian();
        void jia();
    }
}
