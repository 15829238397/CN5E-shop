package com.example.cne_shop.fragment;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.example.cne_shop.R;
import com.example.cne_shop.base.BaseFragment;
import com.example.cne_shop.widget.CnToolbar;

import butterknife.BindView;

/**
 * Created by 博 on 2017/8/21.
 */

public class MineFragment extends BaseFragment {

    @Override
    protected int getResRootViewId() {
        return R.layout.mine_main_fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void init( ) {

    }


}
