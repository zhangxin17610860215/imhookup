package com.yqbj.yhgy.viewholder;

import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nim.uikit.common.ui.widget.BlurTransformation;
import com.netease.nim.uikit.impl.cache.ConfigConstants;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.file.AttachmentStore;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.action.SnapChatAttachment;
import com.yqbj.yhgy.main.WatchSnapChatPictureActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhoujianghua on 2015/8/7.
 */
public class MsgViewHolderSnapChat extends MsgViewHolderBase {

    private ImageView thumbnailImageView;

    protected View progressCover;
    private TextView downSee;
    private TextView tips;
    private RoundedImageView roundedImageView;
    private TextView progressLabel;
    private boolean isLongClick = false;
    private Map<String, Object> localExtension;
    private boolean ownWhetherSee;

    public MsgViewHolderSnapChat(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_snapchat;
    }

    @Override
    protected void inflateContentView() {
        thumbnailImageView = (ImageView) view.findViewById(R.id.message_item_snap_chat_image);
        progressBar = findViewById(R.id.message_item_thumb_progress_bar); // 覆盖掉
        progressCover = findViewById(R.id.message_item_thumb_progress_cover);
        progressLabel = (TextView) view.findViewById(R.id.message_item_thumb_progress_text);
    }

    @Override
    protected void bindContentView() {

        getLocalExtension();

        contentContainer.setOnTouchListener(onTouchListener);

        layoutByDirection();

        refreshStatus();
    }

    private void refreshStatus() {

        thumbnailImageView.setBackgroundResource(isReceivedMessage() ? !ownWhetherSee ? R.drawable.message_view_holder_left_snapchat : R.drawable.message_view_holder_left_snapchat_no : !ownWhetherSee ? R.drawable.message_view_holder_right_snapchat : R.drawable.message_view_holder_right_snapchat_no);

        if (message.getStatus() == MsgStatusEnum.sending || message.getAttachStatus() == AttachStatusEnum.transferring) {
            progressCover.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressCover.setVisibility(View.GONE);
        }

        progressLabel.setText(StringUtil.getPercentString(getMsgAdapter().getProgress(message)));
    }

    @Override
    protected boolean shouldDisplayReceipt() {
        return false;
    }

    protected View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.getParent().requestDisallowInterceptTouchEvent(false);

                    WatchSnapChatPictureActivity.destroy();
                    thumbnailImageView.setBackgroundResource(isReceivedMessage() ? !ownWhetherSee ? R.drawable.message_view_holder_left_snapchat : R.drawable.message_view_holder_left_snapchat_no : !ownWhetherSee ? R.drawable.message_view_holder_right_snapchat : R.drawable.message_view_holder_right_snapchat_no);

                    // 删除这条消息，当然你也可以将其标记为已读，同时删除附件内容，然后不让再查看
                    if (isLongClick && message.getAttachStatus() == AttachStatusEnum.transferred) {
                        // 物理删除
//                        NIMClient.getService(MsgService.class).deleteChattingHistory(message);
//                        AttachmentStore.delete(((SnapChatAttachment) message.getAttachment()).getPath());
//                        AttachmentStore.delete(((SnapChatAttachment) message.getAttachment()).getThumbPath());
                        downSee.setText("已焚毁");
                        tips.setText("");
//                        getMsgAdapter().deleteItem(message, true);
                        isLongClick = false;
                    }
                    break;
            }

            return false;
        }
    };

    @Override
    protected boolean onItemLongClick() {
        if (message.getStatus() == MsgStatusEnum.success && !ownWhetherSee) {
            WatchSnapChatPictureActivity.start(context, message);
            isLongClick = true;
            ownWhetherSee = true;
            setLocalExtension();
            return true;
        }
        return false;
    }

    /**
     * 设置本地字段
     * */
    private void setLocalExtension() {
        localExtension.put(ConfigConstants.MESSAGE_ATTRIBUTE.ownWhetherSee,ownWhetherSee);
        message.setLocalExtension(localExtension);
    }

    /**
     * 获取本地字段
     * */
    private void getLocalExtension() {
        localExtension = message.getLocalExtension();
        if (null != localExtension && !localExtension.isEmpty() && localExtension.size() > 0){
            ownWhetherSee = (boolean) localExtension.get(ConfigConstants.MESSAGE_ATTRIBUTE.ownWhetherSee);
        }else {
            localExtension = new HashMap<>();
        }
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }

    private void layoutByDirection() {
        View body = findViewById(R.id.message_item_snap_chat_body);
        View tipsLayout = findViewById(R.id.message_item_tips_layout);
        downSee = findViewById(R.id.message_item_snap_chat_tips_label);
        tips = findViewById(R.id.message_item_snap_chat_tips);
        roundedImageView = findViewById(R.id.message_item_snap_chat_imagePhoto);
        View readed = findViewById(R.id.message_item_snap_chat_readed);
        ViewGroup container = (ViewGroup) body.getParent();
        container.removeView(tipsLayout);
        Glide.with(context).load(((SnapChatAttachment) message.getAttachment()).getPath()).optionalTransform(new BlurTransformation(context, 50)).placeholder(R.mipmap.zhanwei_logo).error(R.mipmap.zhanwei_logo).into(roundedImageView);
        if (isReceivedMessage()) {
            container.addView(tipsLayout, 1);
        } else {
            container.addView(tipsLayout, 0);
        }
        if (message.getStatus() == MsgStatusEnum.success) {
            downSee.setVisibility(View.VISIBLE);
            tips.setVisibility(View.VISIBLE);
            downSee.setText("按住查看");
            tips.setText("阅后即焚的照片");
        } else {
            downSee.setVisibility(View.GONE);
            tips.setText("");
            tips.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(getMsgAdapter().getUuid()) && message.getUuid().equals(getMsgAdapter().getUuid())) {
            readed.setVisibility(View.VISIBLE);
        } else {
            readed.setVisibility(View.GONE);
        }
    }
}
