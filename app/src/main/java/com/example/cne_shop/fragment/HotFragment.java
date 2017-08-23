package com.example.cne_shop.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cjj.MaterialRefreshLayout;
import com.example.cne_shop.R;
import com.example.cne_shop.adapter.HotAdapter;
import com.example.cne_shop.base.BaseFragment;
import com.example.cne_shop.bean.Page;
import com.example.cne_shop.bean.Ware;
import com.example.cne_shop.contents.Contents;
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
//                        setItemlistenler() ;
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


}
