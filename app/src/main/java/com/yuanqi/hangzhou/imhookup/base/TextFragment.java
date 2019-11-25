package com.yuanqi.hangzhou.imhookup.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuanqi.hangzhou.imhookup.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
/**
 * 样板Fragment
 * */
public class TextFragment extends BaseFragment {

    Unbinder unbinder;

    public TextFragment() {

    }

    public static TextFragment newInstance(String param1, String param2) {
        TextFragment fragment = new TextFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, container, false);
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
