package com.example.cne_shop.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cne_shop.R;
import com.example.cne_shop.activity.LoginActivity;
import com.example.cne_shop.activity.MyOrderActivity;
import com.example.cne_shop.activity.ShowConsigneeAdrActivity;
import com.example.cne_shop.application.MyApplication;
import com.example.cne_shop.base.BaseFragment;
import com.example.cne_shop.bean.User;
import com.example.cne_shop.widget.CnToolbar;

import butterknife.BindView;

/**
 * Created by 博 on 2017/8/21.
 */

public class MineFragment extends BaseFragment {

    private CnToolbar cnToolbar ;
    private Button loginOut ;
    private TextView my_consignee ;
    private TextView my_favorite ;
    private TextView my_orderList ;

    @Override
    protected int getResRootViewId() {
        return R.layout.mine_main_fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void init( ) {

        cnToolbar = (CnToolbar) mView.findViewById(R.id.toolBar) ;
        loginOut = (Button) mView.findViewById(R.id.loginOut) ;
        my_consignee = (TextView)mView.findViewById(R.id.my_consignee) ;
        my_favorite = (TextView) mView.findViewById(R.id.my_favorite);
        my_orderList = (TextView) mView.findViewById(R.id.my_list) ;

        initPbtoolbar() ;
        addListener() ;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initPbtoolbar(){

        cnToolbar.setUserPhotoClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , LoginActivity.class) ;
                startActivityWithLogin(intent , true , MyApplication.START_FOR_RESULT);
            }
        });

        User user = MyApplication.getInstance().getUser() ;
        showUser(user) ;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        User user = MyApplication.getInstance().getUser() ;
        showUser(user) ;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showUser(User user){
        if (user != null){
            cnToolbar.setUserPhotoIcon(this.getActivity() , user.getLogo_url() , R.drawable.default_head );
            cnToolbar.setUserNameText(user.getUsername());
            loginOut.setVisibility(View.VISIBLE);
            cnToolbar.setUserClickable(false);

        }else {

            cnToolbar.setUserNameText("点击登录");
            cnToolbar.setUserPhotoIcon(getContext() , R.drawable.default_head);
            loginOut.setVisibility(View.GONE);
            cnToolbar.setUserClickable(true);
        }
    }

    private void addListener(){

        my_orderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityWithLogin(new Intent(getContext() , MyOrderActivity.class) , true , MyApplication.START_NO_RESULT);
            }
        });

        loginOut.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                loginOut() ;
            }
        });

        my_consignee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityWithLogin(new Intent(getContext() , ShowConsigneeAdrActivity.class ) , true , MyApplication.START_FOR_RESULT );
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void loginOut(){

        MyApplication.getInstance().clearUser();
        User user = MyApplication.getInstance().getUser() ;
        showUser(user) ;
    }
}
