package com.yuanqi.hangzhou.imhookup.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.yuanqi.hangzhou.imhookup.R;
import com.yuanqi.hangzhou.imhookup.base.BaseActivity;

/**
 * Created by hzxuwen on 2016/12/14.
 */

public class FileDownloadActivity extends BaseActivity {
    private static final String INTENT_EXTRA_DATA = "INTENT_EXTRA_DATA";

    private TextView fileNameText;
    private Button fileDownloadBtn;

    private IMMessage message;

    public static void start(Context context, IMMessage message) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_EXTRA_DATA, message);
        intent.setClass(context, FileDownloadActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_download_activity);

        onParseIntent();
        findViews();

        updateUI();
        registerObservers(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
    }

    private void onParseIntent() {
        this.message = (IMMessage) getIntent().getSerializableExtra(INTENT_EXTRA_DATA);
    }

    private void findViews() {
        fileNameText = findViewById(R.id.file_name);
        fileDownloadBtn = findViewById(R.id.download_btn);

        fileDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOriginDataHasDownloaded(message)) {
                    return;
                }

                downloadFile();
            }
        });
    }

    private void updateUI() {
        FileAttachment attachment = (FileAttachment) message.getAttachment();
        if (attachment != null) {
            fileNameText.setText(attachment.getDisplayName());
        }

        if (isOriginDataHasDownloaded(message)) {
            onDownloadSuccess();
        } else {
            onDownloadFailed();
        }
    }

    private boolean isOriginDataHasDownloaded(final IMMessage message) {
        if (!TextUtils.isEmpty(((FileAttachment) message.getAttachment()).getPath())) {
            return true;
        }

        return false;
    }

    private void downloadFile() {
        DialogMaker.showProgressDialog(this, "loading");
        NIMClient.getService(MsgService.class).downloadAttachment(message, false);
    }

    /**
     * ********************************* 下载 ****************************************
     */

    private void registerObservers(boolean register) {
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(statusObserver, register);
    }

    private Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage msg) {
            if (!msg.isTheSame(message) || isDestroyedCompatible()) {
                return;
            }

            if (msg.getAttachStatus() == AttachStatusEnum.transferred && isOriginDataHasDownloaded(msg)) {
                DialogMaker.dismissProgressDialog();
                onDownloadSuccess();
            } else if (msg.getAttachStatus() == AttachStatusEnum.fail) {
                DialogMaker.dismissProgressDialog();
                ToastHelper.showToast(FileDownloadActivity.this, "download failed");
                onDownloadFailed();
            }
        }
    };

    private void onDownloadSuccess() {
        fileDownloadBtn.setText("已下载");
        fileDownloadBtn.setEnabled(false);
        fileDownloadBtn.setBackgroundResource(R.drawable.g_white_btn_pressed);
    }

    private void onDownloadFailed() {
        fileDownloadBtn.setText("下载");
        fileDownloadBtn.setEnabled(true);
        fileDownloadBtn.setBackgroundResource(R.drawable.nim_team_create_btn_selector);
    }
}
