package com.yqbj.yhgy.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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

import com.netease.nim.uikit.api.model.main.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.common.ModuleUIComFn;
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
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.home.AnonymousReportActivity;
import com.yqbj.yhgy.home.HomeFragment;
import com.yqbj.yhgy.me.MeFragment;
import com.yqbj.yhgy.message.NewMessageFragment;
import com.yqbj.yhgy.radio.RadioFragment;
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
    // 获取Android系统权限用到的id
    public static final int REQUEST_PERMISSION = 123;
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
        //检查权限
        checkPermission();
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

    private void checkPermission() {
        PackageManager pkgManager = getPackageManager();
        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission =
                pkgManager.checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission =
                pkgManager.checkPermission(android.Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // 获取摄像机用于获取摄像机 设备信息  CAMERA
        boolean cameraSatePermission =
                pkgManager.checkPermission(android.Manifest.permission.CAMERA, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission || !cameraSatePermission) {
            requestPermission();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                },
                REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        DropManager.getInstance().destroy();
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

    private void setRedPoint(DropFake redPointView, ReminderItem item) {
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
            toast("再按一次退出快速交友");
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
