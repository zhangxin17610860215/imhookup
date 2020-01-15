package com.yqbj.yhgy.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.main.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.common.ModuleUIComFn;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.reminder.ReminderItem;
import com.netease.nim.uikit.common.reminder.ReminderManager;
import com.netease.nim.uikit.common.reminder.ReminderSettings;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.drop.DropCover;
import com.netease.nim.uikit.common.ui.drop.DropFake;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.SplashActivity;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.bean.UserBean;
import com.yqbj.yhgy.home.AnonymousReportActivity;
import com.yqbj.yhgy.home.HomeFragment;
import com.yqbj.yhgy.me.MeFragment;
import com.yqbj.yhgy.message.NewMessageFragment;
import com.yqbj.yhgy.radio.RadioFragment;
import com.yqbj.yhgy.utils.AppManager;
import com.yqbj.yhgy.utils.DemoCache;
import com.yqbj.yhgy.utils.LogUtil;
import com.yqbj.yhgy.utils.Preferences;
import com.yqbj.yhgy.utils.StatusBarsUtil;
import com.yqbj.yhgy.view.MyNoScrollViewPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements ReminderManager.UnreadNumChangedCallback {

    @BindView(R.id.view_pager_main)
    MyNoScrollViewPager viewPagerMain;
    @BindView(R.id.tv_main_tab_home)
    TextView tvMainTabHome;
    @BindView(R.id.ll_main_tab_home)
    RelativeLayout llMainTabHome;
    @BindView(R.id.tv_main_tab_radio)
    TextView tvMainTabRadio;
    @BindView(R.id.ll_main_tab_radio)
    RelativeLayout llMainTabRadio;
    @BindView(R.id.tv_main_tab_message)
    TextView tvMainTabMessage;
    @BindView(R.id.ll_main_tab_message)
    RelativeLayout llMainTabMessage;
    @BindView(R.id.tv_main_tab_me)
    TextView tvMainTabMe;
    @BindView(R.id.img_my_visible)
    ImageView imgMyVisible;
    @BindView(R.id.ll_main_tab_me)
    RelativeLayout llMainTabMe;
    @BindView(R.id.ll_main_bottom)
    LinearLayout llMainBottom;
    @BindView(R.id.unread_number_home)
    DropFake unread_number_home;
    @BindView(R.id.unread_number_radio)
    DropFake unread_number_radio;
    @BindView(R.id.unread_number_message)
    DropFake unread_number_message;
    @BindView(R.id.unread_number_me)
    DropFake unread_number_me;

    private Activity activity;

    private static final String EXTRA_APP_QUIT = "APP_QUIT";
    private int mTabTextColorSelecdted;
    private int mTabTextColorNormal;
    private int currentPos = 0;
    private BottomBarListener mBottomBarListener;
    private HomeFragment mHomeFragment;
    private RadioFragment mRadioFragment;
    private NewMessageFragment mMessageFragment;
    private MeFragment mMeFragment;
    private MainPagerAdapter mMainPagerAdapter;
    private MainOnPageChangeListener mMainOnPageChangeListener;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        activity = this;
        initStatusBars();

        registerObservers(true);
        observerSyncDataComplete();
        initView();
        initModuleComFn();
        initUnreadCover();
        registerMsgUnreadInfoObserver(true);
        registerSystemMessageObservers(true);

        requestSystemMessageUnreadCount();
        initData();

    }

    @OnClick({R.id.ll_main_tab_home, R.id.ll_main_tab_radio, R.id.ll_main_tab_message, R.id.ll_main_tab_me})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_main_tab_home:
                break;
            case R.id.ll_main_tab_radio:
                break;
            case R.id.ll_main_tab_message:
                break;
            case R.id.ll_main_tab_me:
                break;
        }
    }

    private void initStatusBars() {
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarsUtil.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        StatusBarsUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarsUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarsUtil.setStatusBarColor(this, 0x55000000);
        }
    }

    private void initView() {
        mTabTextColorSelecdted = getResources().getColor(R.color.text_theme_color);
        mTabTextColorNormal = getResources().getColor(R.color.color_gray_999999);

        mBottomBarListener = new BottomBarListener();
        llMainTabHome.setOnClickListener(mBottomBarListener);
        llMainTabRadio.setOnClickListener(mBottomBarListener);
        llMainTabMessage.setOnClickListener(mBottomBarListener);
        llMainTabMe.setOnClickListener(mBottomBarListener);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getFragments() != null && fragmentManager.getFragments().size() > 0) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            List<Fragment> fragments = fragmentManager.getFragments();
            for (Fragment f : fragments) {
                if (f != null) {
                    transaction.remove(f);
                }
            }
            transaction.commitAllowingStateLoss();
        }

        mMainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mMainOnPageChangeListener = new MainOnPageChangeListener();
        mHomeFragment = HomeFragment.newInstance("Home", "");
        mRadioFragment = RadioFragment.newInstance("Radio", "");
        mMessageFragment = NewMessageFragment.newInstance("Message", "");
        mMeFragment = MeFragment.newInstance("Me", "");
        viewPagerMain.setAdapter(mMainPagerAdapter);
        viewPagerMain.setOnPageChangeListener(mMainOnPageChangeListener);
        viewPagerMain.setOffscreenPageLimit(3);
        mBottomBarListener = new BottomBarListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerMsgUnreadInfoObserver(false);
        registerSystemMessageObservers(false);
        registerObservers(false);
        DropManager.getInstance().destroy();
    }

    /**
     * 注册用户是否在线观察者
     * */
    private void registerObservers(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }

    /**
     * 注册未读消息数量观察者
     */
    private void registerMsgUnreadInfoObserver(boolean register) {
        if (register) {
            ReminderManager.getInstance().registerUnreadNumChangedCallback(this);
        } else {
            ReminderManager.getInstance().unregisterUnreadNumChangedCallback(this);
        }
    }

    /**
     * 注册/注销系统消息未读数变化
     */
    private void registerSystemMessageObservers(boolean register) {
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver, register);
    }

    /**
     * 查询系统消息未读数
     */
    private void requestSystemMessageUnreadCount() {
        int unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
//        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unread);
        ReminderManager.getInstance().updateContactUnreadNum(unread);
    }

    private void initModuleComFn() {
        ModuleUIComFn.getInstance().toReportActivityInstance = new ModuleUIComFn.ToReportActivity() {
            @Override
            public void toReportActivityInstance(Context context) {
                //进入举报页面
                AnonymousReportActivity.start(context);
            }
        };
    }

    private void initData() {

    }

    private void kickOut(StatusCode code) {
        Preferences.saveUserData(new UserBean());

        if (code == StatusCode.PWD_ERROR) {
            LogUtil.e("Auth", "user password error");
            toast("帐号或密码错误");
        } else {
            LogUtil.i("Auth", "Kicked!");
        }
        onLogout();
    }

    // 注销
    private void onLogout() {
        // 清理缓存&注销监听&清除状态
        NimUIKit.logout();
        SplashActivity.start(activity,null);
        toast("你的帐号被踢出下线，请注意帐号信息安全");
        AppManager.getAppManager().finishAllActivity();
    }

    /**
     * 用户状态变化
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                kickOut(code);
            }
        }
    };

    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer unreadCount) {

            NIMClient.getService(SystemMessageService.class).querySystemMessageUnread()
                    .setCallback(new RequestCallback<List<SystemMessage>>() {
                        @Override
                        public void onSuccess(List<SystemMessage> systemMessages) {
                            for (int i = 0; i < systemMessages.size(); i++){
                                SystemMessage msg = systemMessages.get(i);
                                for (int j = systemMessages.size() - 1 ; j > i; j--){
                                    SystemMessage msg2 = systemMessages.get(j);
                                    if (msg.getFromAccount().equals(msg2.getFromAccount()) && msg.getType().getValue() == msg2.getType().getValue()){
                                        systemMessages.remove(msg2);
                                    }

                                }
                            }
//                            SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(systemMessages.size());
                            ReminderManager.getInstance().updateContactUnreadNum(systemMessages.size());
                        }

                        @Override
                        public void onFailed(int i) {

                        }

                        @Override
                        public void onException(Throwable throwable) {

                        }
                    });
        }
    };

    @Override
    public void onUnreadNumChanged(ReminderItem item) {
        setRedPoint(unread_number_message, item);
    }

    private void setRedPoint(final DropFake redPointView, final ReminderItem item) {
        if (redPointView != null) {
            redPointView.setTouchListener(new DropFake.ITouchListener() {
                @Override
                public void onDown() {
                    DropManager.getInstance().setCurrentId(String.valueOf(item.getId()));
                    DropManager.getInstance().down(redPointView, redPointView.getText());
                }

                @Override
                public void onMove(float curX, float curY) {
                    DropManager.getInstance().move(curX, curY);
                }

                @Override
                public void onUp() {
                    DropManager.getInstance().up();
                }
            });
        }
        int unread = item.unread();
        redPointView.setVisibility(unread > 0 ? View.VISIBLE : View.GONE);
        if (unread > 0) {
            redPointView.setText(String.valueOf(ReminderSettings.unreadMessageShowRule(unread)));
        }
    }

    private void observerSyncDataComplete() {
        boolean syncCompleted = LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {
            @Override
            public void onEvent(Void v) {
                DialogMaker.dismissProgressDialog();
            }
        });
        //如果数据没有同步完成，弹个进度Dialog
        if (!syncCompleted) {
            DialogMaker.showProgressDialog(activity, "正在准备数据...").setCanceledOnTouchOutside(false);
        }
    }

    //初始化未读红点动画
    private void initUnreadCover() {
        DropManager.getInstance().init(this, (DropCover) findView(R.id.unread_cover),
                new DropCover.IDropCompletedListener() {
                    @Override
                    public void onCompleted(Object id, boolean explosive) {
                        if (id == null || !explosive) {
                            return;
                        }

                        if (id instanceof RecentContact) {
                            RecentContact r = (RecentContact) id;
                            NIMClient.getService(MsgService.class).clearUnreadCount(r.getContactId(), r.getSessionType());
                            return;
                        }

                        if (id instanceof String) {
                            if (((String) id).contentEquals("0")) {
                                NIMClient.getService(MsgService.class).clearAllUnreadCount();
                            } else if (((String) id).contentEquals("1")) {
                                NIMClient.getService(SystemMessageService.class).resetSystemMessageUnreadCount();
                            }
                        }
                    }
                });
    }

    private class BottomBarListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_main_tab_home:
//                    StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.white));
                    currentPos = 0;
                    break;
                case R.id.ll_main_tab_radio:
//                    StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.white));
                    currentPos = 1;
                    break;
                case R.id.ll_main_tab_message:
//                    StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.white));
                    currentPos = 2;
                    break;
                case R.id.ll_main_tab_me:
//                    StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.white));
                    currentPos = 3;
                    break;
                default:
                    break;
            }
            updateTabsSelected(currentPos);
            viewPagerMain.setCurrentItem(currentPos, false);
        }
    }

    private class MainPagerAdapter extends FragmentPagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(final int position) {
            switch (position) {
                case 0:
                    return MainActivity.this.mHomeFragment;
                case 1:
                    return MainActivity.this.mRadioFragment;
                case 2:
                    return MainActivity.this.mMessageFragment;
                case 3:
                    return MainActivity.this.mMeFragment;
                default:
                    return MainActivity.this.mHomeFragment;
            }
        }
    }

    private class MainOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int pos) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int pos) {
            updateTabsSelected(pos);
        }
    }

    /**
     * 系统的"返回键"按下的时间戳。用来实现连点2次退出应用
     */
    private static long BACK_PRESS_TIME = 0;

    @Override
    public void onBackPressed() {
        if (BACK_PRESS_TIME + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            toast("再按一次退出约会公园");
        }
        BACK_PRESS_TIME = System.currentTimeMillis();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        try {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                BACK_PRESS_TIME = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.dispatchTouchEvent(event);
    }


    private void updateTabsNormal() {
        tvMainTabHome.setTextColor(mTabTextColorNormal);
        tvMainTabHome.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.mipmap.home_logo_no), null, null);

        tvMainTabRadio.setTextColor(mTabTextColorNormal);
        tvMainTabRadio.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.mipmap.radio_logo_no), null, null);

        tvMainTabMessage.setTextColor(mTabTextColorNormal);
        tvMainTabMessage.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.mipmap.message_logo_no), null, null);

        tvMainTabMe.setTextColor(mTabTextColorNormal);
        tvMainTabMe.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.mipmap.me_logo_no), null, null);

    }

    public void updateTabsSelected(int currentPox) {
        updateTabsNormal();
        switch (currentPox) {
            case 0:
                tvMainTabHome.setTextColor(mTabTextColorSelecdted);
                tvMainTabHome.setCompoundDrawablesWithIntrinsicBounds(null,
                        getResources().getDrawable(R.mipmap.home_logo_yes), null, null);
                break;
            case 1:
                tvMainTabRadio.setTextColor(mTabTextColorSelecdted);
                tvMainTabRadio.setCompoundDrawablesWithIntrinsicBounds(null,
                        getResources().getDrawable(R.mipmap.radio_logo_yes), null, null);
                break;
            case 2:
                tvMainTabMessage.setTextColor(mTabTextColorSelecdted);
                tvMainTabMessage.setCompoundDrawablesWithIntrinsicBounds(null,
                        getResources().getDrawable(R.mipmap.message_logo_yes), null, null);
                break;
            case 3:
                tvMainTabMe.setTextColor(mTabTextColorSelecdted);
                tvMainTabMe.setCompoundDrawablesWithIntrinsicBounds(null,
                        getResources().getDrawable(R.mipmap.me_logo_yes), null, null);
                break;

            default:
                break;
        }
    }

    // 注销
    public static void logout(Context context, boolean quit) {
        Intent extra = new Intent();
        extra.putExtra(EXTRA_APP_QUIT, quit);
        start(context, extra);
    }

}
