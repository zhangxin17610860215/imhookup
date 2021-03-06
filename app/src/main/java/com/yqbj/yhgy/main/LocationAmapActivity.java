package com.yqbj.yhgy.main;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.netease.nim.uikit.api.model.location.LocationProvider;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.utils.NimGeocoder;
import com.yqbj.yhgy.utils.NimLocationManager;

public class LocationAmapActivity extends BaseActivity implements OnCameraChangeListener, OnClickListener
        , NimLocationManager.NimLocationListener {

//	private static final String TAG = "LocationAmapActivity";

//    private TextView sendButton;
    private ImageView pinView;
    private View pinInfoPanel;
    private TextView pinInfoTextView;

    private NimLocationManager locationManager = null;

    private double latitude; // 经度
    private double longitude; // 维度
    private String addressInfo; // 对应的地址信息

    private static LocationProvider.Callback callback;

    private double cacheLatitude = -1;
    private double cacheLongitude = -1;
    private String cacheAddressInfo;

    private boolean locating = true; // 正在定位的时候不用去查位置
    private NimGeocoder geocoder;

    AMap amap;
    private MapView mapView;
    private Button btnMyLocation;

    public static void start(Context context, LocationProvider.Callback callback) {
        LocationAmapActivity.callback = callback;
        context.startActivity(new Intent(context, LocationAmapActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view_amap_layout);
        mapView = findViewById(R.id.autonavi_mapView);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        setToolbar(this,0,"地图定位");

        initView();
        initAmap();
        initLocation();
        updateSendStatus();
    }

    private void initView() {
        pinView = findViewById(R.id.location_pin);
        pinInfoPanel = findViewById(R.id.location_info);
        pinInfoTextView = pinInfoPanel.findViewById(R.id.marker_address);

        pinView.setOnClickListener(this);
        pinInfoPanel.setOnClickListener(this);


        btnMyLocation = findViewById(R.id.my_location);
        btnMyLocation.setOnClickListener(this);
        btnMyLocation.setVisibility(View.GONE);
    }

    private void initAmap() {
        try {
            amap = mapView.getMap();
            amap.setOnCameraChangeListener(this);

            UiSettings uiSettings = amap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);
            // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            uiSettings.setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initLocation() {
        locationManager = new NimLocationManager(this, this);
        Location location = locationManager.getLastKnownLocation();

        Intent intent = getIntent();
        float zoomLevel = intent.getIntExtra(LocationExtras.ZOOM_LEVEL, LocationExtras.DEFAULT_ZOOM_LEVEL);

        LatLng latlng;
        if (location == null) {
            latlng = new LatLng(39.90923, 116.397428);
        } else {
            latlng = new LatLng(location.getLatitude(), location.getLongitude());
        }
        CameraUpdate camera = CameraUpdateFactory.newCameraPosition(new CameraPosition(latlng, zoomLevel, 0, 0));
        amap.moveCamera(camera);
        geocoder = new NimGeocoder(this, geocoderListener);
    }

    private void updateSendStatus() {
        if (isFinishing()) {
            return;
        }
        String titleResID = "地图定位";
        if (TextUtils.isEmpty(addressInfo)) {
            titleResID = "正在定位";
            onInitRightSure(this,"",null);
        } else {
            onInitRightSure(this, "发送", new onToolBarRightTextListener() {
                @Override
                public void onRight(View view) {
                    sendLocation();
                    finish();
                }
            });
        }
        if (btnMyLocation.getVisibility() == View.VISIBLE || Math.abs(-1 - cacheLatitude) < 0.1f) {
//            setTitle(titleResID);
            setToolbar(this,0,titleResID);
        } else {
//            setTitle(R.string.my_location);
            setToolbar(this,0,"我的位置");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        locationManager.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        locationManager.request();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationManager != null) {
            locationManager.stop();
        }

        callback = null;
    }

    private String getStaticMapUrl() {
        StringBuilder urlBuilder = new StringBuilder(LocationExtras.STATIC_MAP_URL_1);
        urlBuilder.append(latitude);
        urlBuilder.append(",");
        urlBuilder.append(longitude);
        urlBuilder.append(LocationExtras.STATIC_MAP_URL_2);
        return urlBuilder.toString();
    }

    private void sendLocation() {
        Intent intent = new Intent();
        intent.putExtra(LocationExtras.LATITUDE, latitude);
        intent.putExtra(LocationExtras.LONGITUDE, longitude);
        addressInfo = TextUtils.isEmpty(addressInfo) ? "该位置信息暂无" : addressInfo;
        intent.putExtra(LocationExtras.ADDRESS, addressInfo);
        intent.putExtra(LocationExtras.ZOOM_LEVEL, amap.getCameraPosition().zoom);
        intent.putExtra(LocationExtras.IMG_URL, getStaticMapUrl());

        if (callback != null) {
            callback.onSuccess(longitude, latitude, addressInfo);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location_pin:
                setPinInfoPanel(!isPinInfoPanelShow());
                break;
            case R.id.location_info:
                pinInfoPanel.setVisibility(View.GONE);
                break;
            case R.id.my_location:
                locationAddressInfo(cacheLatitude, cacheLongitude, cacheAddressInfo);
                break;
        }
    }

    private void locationAddressInfo(double lat, double lng, String address) {
        if (amap == null) {
            return;
        }

        if (amap.getCameraPosition() == null){
            return;
        }

        LatLng latlng = new LatLng(lat, lng);
        CameraUpdate camera = CameraUpdateFactory.newCameraPosition(new CameraPosition(latlng, amap.getCameraPosition().zoom, 0, 0));
        amap.moveCamera(camera);
        addressInfo = address;
        latitude = lat;
        longitude = lng;

        setPinInfoPanel(true);
    }

    private boolean isPinInfoPanelShow() {
        return pinInfoPanel.getVisibility() == View.VISIBLE;
    }

    private void setPinInfoPanel(boolean show) {
        if (show && !TextUtils.isEmpty(addressInfo)) {
            pinInfoPanel.setVisibility(View.VISIBLE);
            pinInfoTextView.setText(addressInfo);
        } else {
            pinInfoPanel.setVisibility(View.GONE);
        }
        updateSendStatus();
    }

    @Override
    public void onLocationChanged(NimLocation location) {
        if (location != null && location.hasCoordinates()) {
            cacheLatitude = location.getLatitude();
            cacheLongitude = location.getLongitude();
            cacheAddressInfo = location.getAddrStr();

            if (locating) {
                locating = false;
                locationAddressInfo(cacheLatitude, cacheLongitude, cacheAddressInfo);
            }
        }
    }

    @Override
    public void onCameraChange(CameraPosition arg0) {
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (!locating) {
            queryLatLngAddress(cameraPosition.target);
        } else {
            latitude = cameraPosition.target.latitude;
            longitude = cameraPosition.target.longitude;
        }
        updateMyLocationStatus(cameraPosition);
    }

    private void updateMyLocationStatus(CameraPosition cameraPosition) {
        if (Math.abs(-1 - cacheLatitude) < 0.1f) {
            // 定位失败
            return;
        }
        LatLng source = new LatLng(cacheLatitude, cacheLongitude);
        LatLng target = cameraPosition.target;
        float distance = AMapUtils.calculateLineDistance(source, target);
        boolean showMyLocation = distance > 50;
        btnMyLocation.setVisibility(showMyLocation ? View.VISIBLE : View.GONE);
        updateSendStatus();
    }

    private void queryLatLngAddress(LatLng latlng) {
        if (!TextUtils.isEmpty(addressInfo) && latlng.latitude == latitude && latlng.longitude == longitude) {
            return;
        }

        Handler handler = getHandler();
        handler.removeCallbacks(runable);
        handler.postDelayed(runable, 20 * 1000);// 20s超时
        geocoder.queryAddressNow(latlng.latitude, latlng.longitude);

        latitude = latlng.latitude;
        longitude = latlng.longitude;

        this.addressInfo = null;
        setPinInfoPanel(false);
    }

    private void clearTimeoutHandler() {
        Handler handler = getHandler();
        handler.removeCallbacks(runable);
    }

    private NimGeocoder.NimGeocoderListener geocoderListener = new NimGeocoder.NimGeocoderListener() {
        @Override
        public void onGeoCoderResult(NimLocation location) {
            if (latitude == location.getLatitude() && longitude == location.getLongitude()) { // 响应的是当前查询经纬度
                if (location.hasAddress()) {
                    LocationAmapActivity.this.addressInfo = location.getFullAddr();
                } else {
                    addressInfo = "该位置信息暂无";
                }
                setPinInfoPanel(true);
                clearTimeoutHandler();
            }
        }
    };


    private Runnable runable = new Runnable() {
        @Override
        public void run() {
            LocationAmapActivity.this.addressInfo = "该位置信息暂无";
            setPinInfoPanel(true);
        }
    };

}
