package com.hz.rxbus.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hz.rxbus.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：
 *
 * @author fzJiang
 * @date 2019-12-16 11:33.
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_1)
    Button mBtn1;
    @BindView(R.id.btn_2)
    Button mBtn2;
    @BindView(R.id.btn_3)
    Button mBtn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_1, R.id.btn_2, R.id.btn_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                startActivity(new Intent(this, ActivityOne.class));
                break;

            case R.id.btn_2:
                startActivity(new Intent(this, ActivityRxJava2.class));
                break;

            case R.id.btn_3:
                startActivity(new Intent(this, ActivityNumLimit.class));
                break;

            default:
                break;
        }
    }
}