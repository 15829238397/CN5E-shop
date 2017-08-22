package com.example.cne_shop.fragment;


import android.view.View;

import com.example.cne_shop.R;
import com.example.cne_shop.base.BaseFragment;
import com.example.cne_shop.widget.CnToolbar;

import butterknife.BindView;

/**
 * Created by 博 on 2017/8/21.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.toolBar)
    CnToolbar cnToolbar ;

    @Override
    protected int getResRootViewId() {
        return R.layout.home_main_fragment;
    }

    @Override
    protected void init(View view) {

    }
}
