package com.example.cne_shop.activity;


import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidlibrary.ButterKnife.BindView;
import com.example.cne_shop.R;
import com.example.cne_shop.base.BaseActivity;
import com.example.cne_shop.bean.TabIndicator;
import com.example.cne_shop.fragment.AssortFragment;
import com.example.cne_shop.fragment.CartFragment;
import com.example.cne_shop.fragment.HomeFragment;
import com.example.cne_shop.fragment.HotFragment;
import com.example.cne_shop.fragment.MineFragment;

import java.util.ArrayList;

/**
 * Created by 博 on 2017/8/21.
 */

public class ShopMainActivity extends BaseActivity {

    @BindView(R.id.tabHost)
    private FragmentTabHost fragmentTabHost ;
    private ImageView tabPhoto ;
    private TextView tabTitle ;
    private static String TAG = ShopMainActivity.class.getName() ;

    private ArrayList<TabIndicator> tabIndicators = new ArrayList<>() ;

    @Override
    protected int getContentViewId() {
        return R.layout.shop_main_activity;
    }

    @Override
    protected void initView() {
        initTab() ;
    }

    @Override
    protected void addListener() {
    }

    @Override
    protected void beforLayout() {
    }

    /**
     * 初始化tab信息
     */
    public void initTab (){
        //指定fragmentTabHost中实际的FragmentLayout。
        fragmentTabHost.setup(this , getSupportFragmentManager() , R.id.realTabContent);

        TabIndicator HomeTab = new TabIndicator(R.string.tab_home , R.drawable.tab_home_image, HomeFragment.class) ;
        TabIndicator SearchTab = new TabIndicator(R.string.tab_search , R.drawable.tab_search_image, HotFragment.class) ;
        TabIndicator ClassifyTab = new TabIndicator(R.string.tab_assort , R.drawable.tab_classify_image, AssortFragment.class) ;
        TabIndicator ShoppingBikeTab = new TabIndicator(R.string.tab_cart , R.drawable.tab_shoppingbike_image, CartFragment.class) ;
        TabIndicator MineTab = new TabIndicator(R.string.tab_mine , R.drawable.tab_user_image, MineFragment.class) ;

        tabIndicators.add(HomeTab) ;
        tabIndicators.add(SearchTab) ;
        tabIndicators.add(ClassifyTab) ;
        tabIndicators.add(ShoppingBikeTab) ;
        tabIndicators.add(MineTab) ;

        //将每一个Tab装入fragmentTabHost
        for(TabIndicator tabIndicator : tabIndicators){
            View view = LayoutInflater.from(mContext).inflate(R.layout.tab_indicator_view , null ) ;
            tabPhoto = (ImageView) view.findViewById(R.id.tab_photo);
            tabTitle = (TextView) view.findViewById(R.id.tab_title) ;

            tabPhoto.setBackgroundResource(tabIndicator.getTabPhoto());
            tabTitle.setText(tabIndicator.getTabTitle());

            //向fragmentTableHost中添加一个TabSpec
            fragmentTabHost.addTab(fragmentTabHost.newTabSpec(getString(tabIndicator.getTabTitle())).setIndicator(view) , tabIndicator.getFragment() , null );
            //执行结束
        }

    }


}
