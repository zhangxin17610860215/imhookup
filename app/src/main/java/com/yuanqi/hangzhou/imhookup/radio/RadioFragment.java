package com.yuanqi.hangzhou.imhookup.radio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuanqi.hangzhou.imhookup.R;
import com.yuanqi.hangzhou.imhookup.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 电台
 * */
public class RadioFragment extends BaseFragment {

    Unbinder unbinder;

    public RadioFragment() {

    }

    public static RadioFragment newInstance(String param1, String param2) {
        RadioFragment fragment = new RadioFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radio, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: get params
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != unbinder){
            unbinder.unbind();
        }
    }
}
