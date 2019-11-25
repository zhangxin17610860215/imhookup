package com.yuanqi.hangzhou.imhookup.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.yuanqi.hangzhou.imhookup.R;

public class EasyAlertDialogHelper {


    public static void showOneButtonDiolag(Context mContext, int titleResId, int msgResId, int btnResId,
                                           boolean cancelable, final OnClickListener positiveListener) {
        showOneButtonDiolag(mContext, getString(mContext, titleResId), getString(mContext, msgResId),
                getString(mContext, btnResId), cancelable, positiveListener).show();
    }

    public static Dialog showOneButtonDiolag(Context mContext, CharSequence titleString, CharSequence msgString,
                                           CharSequence btnString, boolean cancelable, final OnClickListener positiveListener) {
        final Dialog dialog = new Dialog(mContext, R.style.custom_dialog);

        dialog.setContentView(R.layout.common_dialog);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) dialog.findViewById(R.id.tv_content);
        TextView btn_left = (TextView) dialog.findViewById(R.id.btn_cancel);
        TextView btn_right = (TextView) dialog.findViewById(R.id.btn_sure);
        View divider_view = dialog.findViewById(R.id.diver_view);
        View titleLayout = dialog.findViewById(R.id.titleLayout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        if(TextUtils.isEmpty(titleString)){
            titleLayout.setVisibility(View.GONE);
        }else{
            tv_title.setText(titleString);
        }

        tv_content.setText(msgString);
        dialog.setCanceledOnTouchOutside(false);

        btn_left.setVisibility(View.GONE);
        divider_view.setVisibility(View.GONE);

        btn_right.setText(btnString);

        btn_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(positiveListener!=null){
                    positiveListener.onClick(v);
                }
            }
        });

        dialog.setCancelable(cancelable);
        return dialog;
    }

    public static EasyAlertDialog createOkCancelDiolag(Context context, CharSequence title, CharSequence message,
                                                       boolean cancelable, final OnDialogActionListener listener) {
        return createOkCancelDiolag(context, title, message, null, null, cancelable, listener);
    }


    public static Dialog showCommonDialog(Context context, CharSequence title, CharSequence message,
                                          boolean cancelable, final OnDialogActionListener listener){
        return showCommonDialog(context, title, message, null, null, cancelable, listener);

    }

    /**
     * 两个按钮的dialog
     *
     * @param context
     * @param title
     * @param message
     * @param okStr
     * @param cancelStr
     * @param cancelable
     * @param listener
     * @return
     */
    public static EasyAlertDialog createOkCancelDiolag(Context context, CharSequence title, CharSequence message,
                                                       CharSequence okStr, CharSequence cancelStr, boolean cancelable, final OnDialogActionListener listener) {
        final EasyAlertDialog dialog = new EasyAlertDialog(context);
        Log.e("EasyAlertDialog","EasyAlet");
        OnClickListener okListener = new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                listener.doOkAction();
            }
        };
        OnClickListener cancelListener = new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                listener.doCancelAction();
            }
        };
        if (TextUtils.isEmpty(title)) {
            dialog.setTitleVisible(false);
        } else {
            dialog.setTitle(title);
        }
        if (TextUtils.isEmpty(message)) {
            dialog.setMessageVisible(false);
        } else {
            dialog.setMessage(message);
        }
        dialog.addPositiveButton(okStr, okListener);
        dialog.addNegativeButton(cancelStr, cancelListener);
        dialog.setCancelable(cancelable);
        return dialog;
    }


    public static Dialog showCommonDialog(Context activity, CharSequence title, CharSequence content,  CharSequence rightBtnText,CharSequence leftBtnText,boolean cancelable, final OnDialogActionListener mListener){

        final Dialog dialog = new Dialog(activity, R.style.custom_dialog);


            dialog.setContentView(R.layout.common_dialog);
            TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
            TextView tv_content = (TextView) dialog.findViewById(R.id.tv_content);
            TextView btn_left = (TextView) dialog.findViewById(R.id.btn_cancel);
            TextView btn_right = (TextView) dialog.findViewById(R.id.btn_sure);
            View divider_view = dialog.findViewById(R.id.diver_view);
            View titleLayout = dialog.findViewById(R.id.titleLayout);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            if(TextUtils.isEmpty(title)){
                titleLayout.setVisibility(View.GONE);
            }else{
                tv_title.setText(title);
            }

            tv_content.setText(content);
            dialog.setCanceledOnTouchOutside(false);
            if (TextUtils.isEmpty(leftBtnText)) {
                btn_left.setVisibility(View.GONE);
                divider_view.setVisibility(View.GONE);
            } else {
                btn_left.setText(leftBtnText);

            }
            if (!TextUtils.isEmpty(rightBtnText)) {
                btn_right.setText(rightBtnText);
            }
            btn_left.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (mListener != null) {
                        mListener.doCancelAction();
                    }
                }
            });

            btn_right.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (mListener != null) {
                        mListener.doOkAction();
                    }
                }
            });

            dialog.setCancelable(cancelable);


        return dialog;
    }


    /*

   自定义content内容
     */

    public static Dialog showCommonDialogSelfContent(Context activity, int contentLayoutId,  CharSequence rightBtnText,CharSequence leftBtnText,boolean cancelable, final OnDialogActionListener mListener){

        final Dialog dialog = new Dialog(activity, R.style.custom_dialog);


        dialog.setContentView(R.layout.common_dialog);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) dialog.findViewById(R.id.tv_content);
        TextView btn_left = (TextView) dialog.findViewById(R.id.btn_cancel);
        TextView btn_right = (TextView) dialog.findViewById(R.id.btn_sure);
        View divider_view = dialog.findViewById(R.id.diver_view);
        View titleLayout = dialog.findViewById(R.id.titleLayout);
        LinearLayout contentLayout = (LinearLayout) dialog.findViewById(R.id.content_layout);
        titleLayout.setVisibility(View.GONE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        LayoutInflater.from(activity).inflate(contentLayoutId,contentLayout,true);

        tv_content.setVisibility(View.GONE);
        dialog.setCanceledOnTouchOutside(false);
        if (TextUtils.isEmpty(leftBtnText)) {
            btn_left.setVisibility(View.GONE);
            divider_view.setVisibility(View.GONE);
        } else {
            btn_left.setText(leftBtnText);

        }
        if (!TextUtils.isEmpty(rightBtnText)) {
            btn_right.setText(rightBtnText);
        }
        btn_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mListener != null) {
                    mListener.doCancelAction();
                }
            }
        });

        btn_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mListener != null) {
                    mListener.doOkAction();
                }
            }
        });

        dialog.setCancelable(cancelable);


        return dialog;
    }


     /*

   自定义content内容
     */

    public static Dialog showCommonDialogWithEdit(Context activity,int titleResId,CharSequence rightBtnText,CharSequence leftBtnText,boolean cancelable, final OnDialogWithEditActionListener mListener){

        final Dialog dialog = new Dialog(activity, R.style.custom_dialog);


        dialog.setContentView(R.layout.common_dialog);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) dialog.findViewById(R.id.tv_content);
        TextView btn_left = (TextView) dialog.findViewById(R.id.btn_cancel);
        TextView btn_right = (TextView) dialog.findViewById(R.id.btn_sure);
        final EditText editText =  new EditText(activity);
        View divider_view = dialog.findViewById(R.id.diver_view);
        View titleLayout = dialog.findViewById(R.id.titleLayout);
        LinearLayout contentLayout = (LinearLayout) dialog.findViewById(R.id.content_layout);
//        titleLayout.setVisibility(View.GONE);
        if(titleResId!=0){
            tv_title.setText(titleResId);
        }else{
            titleLayout.setVisibility(View.GONE);
        }

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        tv_content.setVisibility(View.GONE);
        dialog.setCanceledOnTouchOutside(false);
        if (TextUtils.isEmpty(leftBtnText)) {
            btn_left.setVisibility(View.GONE);
            divider_view.setVisibility(View.GONE);
        } else {
            btn_left.setText(leftBtnText);

        }
        if (!TextUtils.isEmpty(rightBtnText)) {
            btn_right.setText(rightBtnText);
        }
        btn_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mListener != null) {
                    mListener.doCancelAction();
                }
            }
        });

        btn_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mListener != null) {

                    mListener.doOkAction(editText.getText().toString());

                }
            }
        });




        LinearLayout.LayoutParams layoutParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(ScreenUtil.dip2px(10),ScreenUtil.dip2px(30),ScreenUtil.dip2px(10),ScreenUtil.dip2px(30));


        editText.setLayoutParams(layoutParams);

        contentLayout.addView(editText);


        dialog.setCancelable(cancelable);


        return dialog;
    }


      /*

   自定义content内容
     */

    public static Dialog showCommonDialogWithEdit(Context activity,int titleResId,CharSequence rightBtnText,CharSequence leftBtnText,boolean cancelable, final OnDialogWithEditActionListener mListener,int inputType){

        final Dialog dialog = new Dialog(activity, R.style.custom_dialog);


        dialog.setContentView(R.layout.common_dialog);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) dialog.findViewById(R.id.tv_content);
        TextView btn_left = (TextView) dialog.findViewById(R.id.btn_cancel);
        TextView btn_right = (TextView) dialog.findViewById(R.id.btn_sure);
        final EditText editText =  new EditText(activity);
        View divider_view = dialog.findViewById(R.id.diver_view);
        View titleLayout = dialog.findViewById(R.id.titleLayout);
        LinearLayout contentLayout = (LinearLayout) dialog.findViewById(R.id.content_layout);
//        titleLayout.setVisibility(View.GONE);
        if(titleResId!=0){
            tv_title.setText(titleResId);
        }else{
            titleLayout.setVisibility(View.GONE);
        }

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        tv_content.setVisibility(View.GONE);
        dialog.setCanceledOnTouchOutside(false);
        if (TextUtils.isEmpty(leftBtnText)) {
            btn_left.setVisibility(View.GONE);
            divider_view.setVisibility(View.GONE);
        } else {
            btn_left.setText(leftBtnText);

        }
        if (!TextUtils.isEmpty(rightBtnText)) {
            btn_right.setText(rightBtnText);
        }
        btn_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mListener != null) {
                    mListener.doCancelAction();
                }
            }
        });

        btn_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mListener != null) {

                    mListener.doOkAction(editText.getText().toString());

                }
            }
        });




        LinearLayout.LayoutParams layoutParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(ScreenUtil.dip2px(10),ScreenUtil.dip2px(30),ScreenUtil.dip2px(10),ScreenUtil.dip2px(30));


        editText.setLayoutParams(layoutParams);
        editText.setInputType(inputType);

        contentLayout.addView(editText);


        dialog.setCancelable(cancelable);


        return dialog;
    }






    public interface OnDialogActionListener {
        void doCancelAction();

        void doOkAction();

    }

    public interface OnDialogWithEditActionListener {
        void doCancelAction();

        void doOkAction(String text);

    }

    private static String getString(Context context, int id) {
        if (id == 0) {
            return null;
        }
        return context.getString(id);
    }
}
