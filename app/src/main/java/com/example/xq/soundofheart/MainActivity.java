package com.example.xq.soundofheart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.xq.soundofheart.utils.StatusBarUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvItem1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setTransparent(this);
        initView();
        initEvent();

    }

    private void initEvent() {
        tvItem1.setOnClickListener(this);
    }

    private void initView() {
        tvItem1=(TextView)findViewById(R.id.tv_item1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_item1:
                Intent intent = new Intent();
                intent.setClass(this,SelectModuleActivity.class);
                startActivity(intent);
                break;
            default:break;
        }
    }
}
