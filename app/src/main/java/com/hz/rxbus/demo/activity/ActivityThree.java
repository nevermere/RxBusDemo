package com.hz.rxbus.demo.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.hz.rxbus.demo.R;
import com.hz.rxbus.demo.utils.RxBusUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：
 *
 * @author fzJiang
 * @date 2019-12-16 11:33.
 */
public class ActivityThree extends AppCompatActivity {

    public static final int FINISH = 12;

    @BindView(R.id.btn_1)
    Button mBtn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_1)
    public void onViewClicked() {
        Message message = Message.obtain();
        message.what = FINISH;
        RxBusUtil.get().post(message);

        finish();
    }
}