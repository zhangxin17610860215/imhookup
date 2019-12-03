package com.yuanqi.hangzhou.imhookup.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxj.xpopup.core.CenterPopupView;
import com.netease.nim.uikit.common.ToastHelper;
import com.yuanqi.hangzhou.imhookup.R;
import com.yuanqi.hangzhou.imhookup.utils.NumberUtil;


public class PaySelect extends CenterPopupView implements View.OnClickListener {

    private Context context;

    private String payNum;
    private String commodityName;
    private String currWalletNum;
    private ImageView ali_btn_icon;
    private ImageView wchat_btn_icon;

    private ImageView wallet_btn_icon;

    private int type = 0;
    private Boolean isWalletAble = false;

    public static enum SelectPayType {
        ALI, // 支付宝
        WCHAT, // 微信
        WALLET, // 钱包
    }

    private SelectPayType currSeletPayType;

    /**
     * 返回当前选择的支付类型
     *
     * @return
     */
    public SelectPayType getCurrSeletPayType() {
        return currSeletPayType;
    }

    /**
     * 支付方式选择
     *
     * @param context
     * @param payNum        支付的金额
     * @param commodityName 商品名称
     * @param currWalletNum 当前余额
     * @param type          1 （展示 余额 微信 支付宝）2 （展示 微信 支付宝）
     */
    public PaySelect(@NonNull Context context, String payNum, String commodityName, String currWalletNum, int type) {
        super(context);
        this.context = context;
        this.commodityName = commodityName;
        this.payNum = payNum;
        this.currWalletNum = currWalletNum;
        this.type = type;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.attach_pay_select;
    }


    @Override
    public void init() {
        super.init();
        initView();
    }

    private void initView() {
        ImageView close_btn = findViewById(R.id.close_btn);
//        TextView pay_btn = findViewById(R.id.pay_btn);
        LinearLayout ali_btn = findViewById(R.id.ali_btn);
        LinearLayout wchat_btn = findViewById(R.id.wchat_btn);
        LinearLayout wallet_btn = findViewById(R.id.wallet_btn);

        ali_btn_icon = findViewById(R.id.bt3);
        wchat_btn_icon = findViewById(R.id.bt2);
        wallet_btn_icon = findViewById(R.id.bt1);

        close_btn.setOnClickListener(this);
        ali_btn.setOnClickListener(this);
        wchat_btn.setOnClickListener(this);
        TextView pay_btn = findViewById(R.id.pay_btn);
        pay_btn.setOnClickListener(this);
        TextView walletnum = findViewById(R.id.walletnum);
        if (this.type != 1) {
            wallet_btn.setVisibility(View.GONE);
            findViewById(R.id.gap).setVisibility(View.GONE);
        } else {
            if (NumberUtil.compareLess(this.currWalletNum, this.payNum)) {
                findViewById(R.id.little).setVisibility(View.VISIBLE);
                walletnum.setTextColor(getResources().getColor(R.color.black));
                wallet_btn_icon.setVisibility(View.GONE);
            } else {
                wallet_btn.setOnClickListener(this);
                findViewById(R.id.little).setVisibility(View.GONE);
                isWalletAble = true;
            }
        }

        TextView amount = findViewById(R.id.amount);
        amount.setText("¥ " + this.payNum + " 元");
        walletnum.setText("钱包余额:¥ " + currWalletNum + " 元");


        TextView commodityName = findViewById(R.id.commodityName);
        commodityName.setText("商品名称:" + this.commodityName);

        resetBtnIcon();
        //默认选中支付宝支付
        currSeletPayType = SelectPayType.ALI;
        ali_btn_icon.setImageResource(R.mipmap.selected_logo);

    }

    private OnClickListener sureListener;

    public void setOnClickListenerOnSure(OnClickListener sureListener) {
        this.sureListener = sureListener;
    }

    private void resetBtnIcon() {
        ali_btn_icon.setImageResource(R.mipmap.unselected_logo);
        wchat_btn_icon.setImageResource(R.mipmap.unselected_logo);
        if (isWalletAble) {
            wallet_btn_icon.setImageResource(R.mipmap.unselected_logo);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_btn:
                dismiss();
                break;
            case R.id.pay_btn:
                if (null == currSeletPayType){
                    ToastHelper.showToast(context,"请选择支付方式");
                    return;
                }
                if (sureListener != null) {
                    sureListener.onClick(v);
                }
                dismiss();
                break;
            case R.id.ali_btn:
                resetBtnIcon();
                currSeletPayType = SelectPayType.ALI;
                ali_btn_icon.setImageResource(R.mipmap.selected_logo);
                break;
            case R.id.wchat_btn:
//                Toast toast = Toast.makeText(context,
//                        "功能添加中，敬请期待", Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
                resetBtnIcon();
                currSeletPayType = SelectPayType.WCHAT;
                wchat_btn_icon.setImageResource(R.mipmap.selected_logo);
                break;
            case R.id.wallet_btn:
                resetBtnIcon();
                currSeletPayType = SelectPayType.WALLET;
                wallet_btn_icon.setImageResource(R.mipmap.selected_logo);
                break;
        }

    }
}
