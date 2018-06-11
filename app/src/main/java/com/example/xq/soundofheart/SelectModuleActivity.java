package com.example.xq.soundofheart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.xq.soundofheart.utils.StatusBarUtil;
import com.example.zhouwei.library.CustomPopWindow;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rocky on 2018/6/10 0010.
 */

public class SelectModuleActivity extends AppCompatActivity {
    @BindView(R.id.tv_content1)
    TextView tvContent1;
    @BindView(R.id.tv_content2)
    TextView tvContent2;
    @BindView(R.id.tv_content3)
    TextView tvContent3;
    @BindView(R.id.tv_item2)
    TextView tvItem2;
    @BindView(R.id.tb_find)
    Toolbar tbFind;
    private CustomPopWindow popWindow;
    private String des1 = "声调对比辨别训练对提高使用者辨别中文言语中的具有不同声调的字词的能力是非是有帮助的。训练主要显示的是字词和对应的拼音，配有每个显示字词的对应图像，便于幼龄儿童的理解并提高儿童对训练的兴趣，增加训练的趣味性。";
    private String des2 = "本训练总共有25组字，有50张图片。如果选错了会再次播放正确的语音信号以及对应的图像。";
    private String des3 = "本训练总共有25组字，有50张图片。如果选错了会再次播放正确的语音信号以及对应的图像。";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_module);
        ButterKnife.bind(this);
        StatusBarUtil.setTransparent(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        tvItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPopWindow();
            }
        });
    }

    private void initView() {
        tvContent1.setText(des1);
        tvContent2.setText(des2);
        tvContent3.setText(des3);
        tbFind.setNavigationIcon(R.mipmap.ic_arrow_left);
        setSupportActionBar(tbFind);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tbFind.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setPopWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_popwindow_select_module,null);
         popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)//显示的布局，还可以通过设置一个View
                .setFocusable(true)//是否获取焦点，默认为ture
                .size(450,200)
                .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                .create()//创建PopupWindow
                .showAsDropDown(tvItem2, 0, 20);//显示PopupWindow
        TextView tvQuiet = (TextView)contentView.findViewById(R.id.tv_quiet);
        TextView tvNoise = (TextView)contentView.findViewById(R.id.tv_noise);
        tvQuiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SelectModuleActivity.this,TrainActivity.class);
                startActivity(intent);
                popWindow.dissmiss();
            }
        });
    }
}
