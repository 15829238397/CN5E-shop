package com.example.cne_shop.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 博 on 2017/8/21.
 */

public abstract class BaseFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resRootViewId =  getResRootViewId () ;
        View view = inflater.inflate(resRootViewId , null , false) ;

        init(view);

        return view ;
    }

    protected abstract int getResRootViewId() ;
    protected abstract void init(View view) ;

}
