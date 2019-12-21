package com.hz.rxbus.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.hz.rxbus.demo.R;
import com.hz.rxbus.demo.utils.RxBusUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * 描述：
 *
 * @author fzJiang
 * @date 2019-12-16 11:33.
 */
public class ActivityTwo extends AppCompatActivity {

    @BindView(R.id.btn_1)
    Button mBtn1;
    @BindView(R.id.tv_1)
    TextView mTv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        ButterKnife.bind(this);

        RxBusUtil.get().subscribe(Message.class, new Consumer() {

            @Override
            public void accept(Object o) throws Exception {
                Message message = (Message) o;
                if (ActivityThree.FINISH == message.what) {
                    Log.i("ActivityTwo", "接收到事件:" + ((Message) o).what);

                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        RxBusUtil.get().unSubscribe();
        super.onDestroy();
    }

    @OnClick(R.id.btn_1)
    public void onViewClicked() {
        startActivity(new Intent(this, ActivityThree.class));
    }
}