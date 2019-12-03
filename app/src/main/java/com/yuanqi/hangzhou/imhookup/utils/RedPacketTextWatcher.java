package com.yuanqi.hangzhou.imhookup.utils;

/**
 * zhangxin
 */

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 描述 ：金额输入字体监听类，限制小数点后输入位数
 * 默认限制小数点2位
 * 默认第一位输入小数点时，转换为0.
 * 如果起始位置为0,且第二位跟的不是".",则无法后续输入
 */
public class RedPacketTextWatcher implements TextWatcher {
    private String TAG = "RedPacketTextWatcher";
    private EditText editText;
    private int digits = 2;
    private Object object;  // 需要对那个View操作    传过来
    private int type;

    public RedPacketTextWatcher(EditText et) {
        editText = et;
    }

    public RedPacketTextWatcher setDigits(int d) {
        digits = d;
        return this;
    }

    public RedPacketTextWatcher addView(int type, Object o) {
        object = o;
        this.type = type;
        return this;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        redPacketMoney(s);
    }

    @Override
    public void afterTextChanged(Editable s) { }

    private void redPacketMoney(CharSequence s) {
        //        删除“.”后面超过2位后的数据
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > digits) {
                s = s.toString().subSequence(0, s.toString().indexOf(".") + digits + 1);
                editText.setText(s);
                editText.setSelection(s.length());
//                光标移到最后
            }
        }
//        如果 "." 在起始位置, 则起始位置自动补0
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            editText.setText(s);
            editText.setSelection(2);
        }
//        如果起始位置为0, 且第二位跟的不是 ".", 则无法后续输入
        if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                editText.setText(s.subSequence(0, 1));
                editText.setSelection(1);
                return;
            }
        }

        if (null != object){
            TextView textView = (TextView) object;
            if (StringUtil.isNotEmpty(editText.getText().toString())){
                if (type==1){
                    if (NumberUtil.compareEqual(editText.getText().toString(),"0") || NumberUtil.compareLess(editText.getText().toString(),"0")){
                        textView.setText("0.00");
                    }else {
                        textView.setText(s.toString());
                    }
                }else if (type == 2){
                    try {
                        double mul = NumberUtil.mul(editText.getText().toString(), "0.0075");
                        String str = NumberUtil.interceptUp(String.valueOf(mul), 2);
                        if (NumberUtil.compareLess(str,"0.10")){
                            textView.setText("0.10");
                        }else {
                            textView.setText(str);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}

