package com.example.cne_shop.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.example.cne_shop.R;
import com.example.cne_shop.activity.WareDetialActivity;
import com.example.cne_shop.adapter.HotAdapter;
import com.example.cne_shop.base.BaseAdapter;
import com.example.cne_shop.base.BaseFragment;
import com.example.cne_shop.bean.Page;
import com.example.cne_shop.bean.Ware;
import com.example.cne_shop.contents.Contents;
import com.example.cne_shop.utils.CartProvider;
import com.example.cne_shop.utils.Pager;
import com.example.cne_shop.widget.MyDivider;

import java.util.List;

/**
 * Created by 博 on 2017/8/21.
 */

public class HotFragment extends BaseFragment {

    private RecyclerView recyclerView ;
    private MaterialRefreshLayout materialRefreshLayout ;
    private HotAdapter mHotAdapter ;
    private Pager pager ;

    @Override
    protected int getResRootViewId() {
        return R.layout.hot_main_fragment;
    }

    @Override
    protected void init() {

        recyclerView = (RecyclerView) mView.findViewById(R.id.resyclerView);
        materialRefreshLayout = (MaterialRefreshLayout) mView.findViewById(R.id.materialRefreshLayout) ;

        try {
            initMaterialRefreshLayout();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initMaterialRefreshLayout() throws Exception {

        String uri = Contents.API.HOT ;

        Pager.Builder builder = Pager.getBuilder()
                .setMaterialRefreshLayout(materialRefreshLayout)
                .putParams("curPage" , 1)
                .putParams("pageSize" , 10)
                .setUri(uri)
                .setOnPageListener(new Pager.OnPageListener<Ware>() {
                    @Override
                    public void loadNormal(List<Ware> mData, int totalPage, int pageSize) {
                        mHotAdapter =new HotAdapter(mContext , mData ) ;
                        setItemlistenler() ;
                        recyclerView.setAdapter(mHotAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        recyclerView.addItemDecoration(new MyDivider());
                    }

                    @Override
                    public void loadMoreData(List<Ware> mData, int totalPage, int pageSize) {
                        mHotAdapter.addData(mData);
                    }

                    @Override
                    public void refData(List<Ware> mData, int totalPage, int pageSize) {
                        mHotAdapter.cleanData();
                        mHotAdapter.addData(mData);
                        recyclerView.scrollToPosition(0);
                    }
                }) ;

        pager = builder.build(mContext , Page.class) ;

    }

    public void setItemlistenler (){

        mHotAdapter.setOnItemClickListener(new BaseAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                if(view.getId() == R.id.buyButton){
                    addToCart(position) ;
                }else {

                    Ware ware = mHotAdapter.getData(position);
                    Intent intent = new Intent(getActivity() , WareDetialActivity.class);
                    intent.putExtra(Contents.WARE , ware);

                    startActivity(intent);
                }

            }
        });

    }

    public void addToCart( int position ){

        Ware hotGoodsMsgPart = mHotAdapter.getData(position) ;

        CartProvider.getCartProvider(getContext()) .put( hotGoodsMsgPart );
        Toast.makeText( getContext() , "已添加到购物车" , Toast.LENGTH_SHORT ).show();
    }


}
