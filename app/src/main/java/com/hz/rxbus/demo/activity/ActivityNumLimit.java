package com.hz.rxbus.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.hz.rxbus.demo.R;
import com.hz.rxbus.demo.utils.NumLimitTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：
 *
 * @author fzJiang
 * @date 2019-12-18 15:34.
 */
public class ActivityNumLimit extends AppCompatActivity {

    @BindView(R.id.et_1)
    EditText mEt1;
    @BindView(R.id.et_2)
    EditText mEt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numlimit);
        ButterKnife.bind(this);

        // (0-1000)
        mEt1.addTextChangedListener(new NumLimitTextWatcher(0, 1000));

        // (0.00-1000.00)
        mEt2.addTextChangedListener(new NumLimitTextWatcher(0.01, 1000.07, 2));

    }
}