package com.example.cne_shop.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;


/**
 * Created by 博 on 2017/8/21.
 */

public abstract class BaseActivity extends AppCompatActivity{

    protected Activity mContext ;
    protected Handler mHandler ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //在加载布局之前执行一些方法
        beforLayout();

        this.mContext = this ;

        //加载布局
        setContentView(getContentViewId() );

        //定义一个handle
        mHandler = new Handler() ;

        //绑定ButterKnife
        ButterKnife.bind(mContext);

        initView();
        addListener();
    }

    protected abstract int getContentViewId() ;
    protected abstract void initView() ;
    protected abstract void addListener() ;
    protected abstract void beforLayout() ;

}
