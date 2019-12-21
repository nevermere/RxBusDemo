package com.hz.rxbus.demo.utils;

import android.text.Editable;

import java.math.BigDecimal;

/**
 * 带数字限制的文本监视器
 *
 * @author fzJiang
 */
public class NumLimitTextWatcher implements android.text.TextWatcher {

    private static final String POINT_DOT = ".";
    private static final int CLEAR_TEXT = 0;//清空
    private static final int HOLD_TEXT = 1;//保持
    private static final int CHANGE_TEXT = 2;//修改为最大值或最小值

    private double maxValue;
    private double minValue;
    private int pointLen;

    private String defaultMaxValue;
    private String defaultMinValue;

    private CharSequence inputText = "";
    private boolean isInputOk = true;

    private int processType = HOLD_TEXT;

    private int maxLength;

    /**
     * 带数字限制的文本监视器
     *
     * @param minvalue 最小值
     * @param maxvalue 最大值
     */
    public NumLimitTextWatcher(long minvalue, long maxvalue) {
        if (maxvalue < minvalue) {
            throw new IllegalArgumentException("maxvalue 不能小于 minvalue");
        }
        maxValue = maxvalue;
        minValue = minvalue;
        pointLen = 0;
        defaultMaxValue = String.valueOf(maxvalue);
        defaultMinValue = String.valueOf(minvalue);

        maxLength = defaultMaxValue.length();
    }

    /**
     * 带数字限制的文本监视器
     *
     * @param minvalue 最小值
     * @param maxvalue 最大值
     * @param len      小数点后最大位数
     */
    public NumLimitTextWatcher(double minvalue, double maxvalue, int len) {
        if (maxvalue < minvalue) {
            throw new IllegalArgumentException("maxvalue 不能小于 minvalue");
        }
        minValue = minvalue;
        maxValue = maxvalue;

        defaultMaxValue = formatString(maxvalue, len);
        defaultMinValue = formatString(minvalue, len);

        pointLen = len;
        maxLength = defaultMaxValue.length();

        LogUtil.i("maxValue:" + maxValue + ",minValue:" + minValue + ",defaultMaxValue:" + defaultMaxValue + ",defaultMinValue:" + defaultMinValue + ",maxLength:" + maxLength);
    }

    @Override
    public void afterTextChanged(Editable s) {
        String str = s.toString();
        int len = str.length();
        if ("".equals(str)) {
            inputText = "";
            isInputOk = true;
            return;//不处理空字符
        }
        if (!isInputOk) {
            isInputOk = true;
            s.replace(0, s.length(), inputText);
            return;
        }
        //double value = Double.valueOf(str);
        //LogUtil.i("str:" + str + ",value:" + value);

        // 输入超过最大长度限制
        if (len > maxLength) {
            LogUtil.i("输入超过最大长度限制,len:" + len + ",maxLength:" + maxLength);
            s.replace(0, s.length(), inputText);
        }

        // 不在最小值-最大值范围内
        if (str.compareTo(defaultMaxValue) > 0 || str.compareTo(defaultMinValue) < 0) {
            LogUtil.i("不在最小值-最大值范围内,inputText:" + inputText + ",s:" + s.toString());

            switch (processType) {
                case CLEAR_TEXT:
                    s.clear();
                    break;
                case HOLD_TEXT:
                    s.replace(0, s.length(), inputText);
                    break;
                case CHANGE_TEXT:
                    if (str.compareTo(defaultMaxValue) > 0) {
                        s.replace(0, s.length(), defaultMaxValue);
                    } else if (str.compareTo(defaultMinValue) < 0) {
                        s.replace(0, s.length(), defaultMinValue);
                    }
                    break;
                default:
                    break;
            }
        }


//        if (compare(value, maxValue) > 0 || compare(value, minValue) < 0) {
//            LogUtil.i("不在最小值-最大值范围内,inputText:" + inputText + ",s:" + s.toString());
//
//            switch (processType) {
//                case CLEAR_TEXT:
//                    s.clear();
//                    break;
//                case HOLD_TEXT:
//                    s.replace(0, s.length(), inputText);
//                    break;
//                case CHANGE_TEXT:
//                    if (value > maxValue) {
//                        s.replace(0, s.length(), defaultMaxValue);
//                    } else if (value < minValue) {
//                        s.replace(0, s.length(), defaultMinValue);
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }

        // 有小数位数限制时,不包含小数点
        if (pointLen != 0 && str.contains(POINT_DOT)) {
            len -= 1;
            LogUtil.i("有小数位数限制时,不包含小数点:" + str + ",len:" + len);

            if (len - str.indexOf(POINT_DOT) > pointLen) {
                s.replace(0, s.length(), inputText);
                return;
            }
        }
        inputText = s.toString();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (pointLen == 0) {
            isInputOk = s.toString().matches("^[0-9]*$");
        } else {
            isInputOk = s.toString().matches("^(\\d+\\.?)|(\\.?\\d+)|(\\d+\\.?\\d+)$");
        }
    }

    /**
     * 格式化精度
     *
     * @param v
     * @param point 小数位数
     * @return double
     */
    private double format(double v, int point) {
        BigDecimal b = BigDecimal.valueOf(v);
        return b.setScale(point, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 格式化精度
     *
     * @param v
     * @param point 小数位数
     * @return double
     */
    private String formatString(double v, int point) {
        BigDecimal b = BigDecimal.valueOf(v);
        return b.setScale(point, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    /**
     * 比较大小 小于0：v1 < v2 大于0：v1 > v2 等于0：v1 = v2
     *
     * @param v1
     * @param v2
     * @return
     */
    private int compare(double v1, double v2) {
        BigDecimal n1 = BigDecimal.valueOf(v1);
        BigDecimal n2 = BigDecimal.valueOf(v2);
        return n1.compareTo(n2);
    }
}