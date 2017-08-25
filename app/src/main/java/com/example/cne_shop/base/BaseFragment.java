package com.example.cne_shop.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cne_shop.activity.LoginActivity;
import com.example.cne_shop.application.MyApplication;
import com.example.cne_shop.contents.Contents;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 博 on 2017/8/21.
 */

public abstract class BaseFragment extends Fragment {

    protected Context mContext ;
    protected View mView ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resRootViewId =  getResRootViewId () ;
        mView = inflater.inflate(resRootViewId , container , false) ;

        mContext = this.getContext() ;
        init();
        return mView ;
    }

    protected abstract int getResRootViewId() ;
    protected abstract void init() ;

    protected void startActivityWithLogin(Intent intent , boolean isNeedLogin , int startIntentStype){

        if (isNeedLogin){

            if(MyApplication.getInstance().getUser() == null){

                Intent intent1 = new Intent(getActivity() , LoginActivity.class) ;

                if (MyApplication.START_FOR_RESULT == startIntentStype){
                    startActivityForResult(intent1 , Contents.REQUEST_CODE);
                }else if(MyApplication.START_NO_RESULT == startIntentStype){
                    MyApplication.getInstance().setIntent(intent);
                    startActivity(intent1);
                }
            }else {
                getActivity().startActivity(intent);
            }
        }else {
            getActivity().startActivity(intent);
        }

    }

}
