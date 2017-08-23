package com.example.cne_shop.utils;

import android.content.Context;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.cne_shop.bean.Page;
import com.example.cne_shop.bean.Ware;
import com.example.cne_shop.okhttp.OkhttpHelper;
import com.example.cne_shop.okhttp.loadingSpotsDialog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 博 on 2017/7/15.
 */

public class Pager {

    private List<Ware> mData ;
    private static final int STATUS_NORMAL = 0 ;
    private static final int STATUS_FRESH = 1 ;
    private static final int STATUS_LOADMARE = 2 ;

    private int status = STATUS_NORMAL ;

    //定义一个静态实例，接收数据，生成一个Pager实例对象
    private static Pager.Builder builder ;

    private OkhttpHelper okhttpHelper;

    private Pager(){

        okhttpHelper = OkhttpHelper.getOkhttpHelper() ;
        getData() ;
        setMaterialRefreshLayoutListener(builder.materialRefreshLayout) ;

    }

    public void changeParamsInUri(String key , Integer value){

        builder.params.put(key , value+"") ;

    }

    public int getTotalCount (){
        return this.builder.totalCount ;
    }

    //对MaterialRefreshLayout添加事件。实现上滑和下拉。
    public void setMaterialRefreshLayoutListener (MaterialRefreshLayout materialRefreshLayout ){
        materialRefreshLayout.setLoadMore(true);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

                Toast.makeText(materialRefreshLayout.getContext() , "刷新" , Toast.LENGTH_SHORT).show();
                freshData() ;
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                if( builder.curPage*builder.pageSize < builder.totalCount ){
                    Toast.makeText(materialRefreshLayout.getContext() , "加载更多" , Toast.LENGTH_SHORT).show();
                    loadMore() ;
                }else {
                    materialRefreshLayout.finishRefreshLoadMore();
                    Toast.makeText(materialRefreshLayout.getContext() , "已经到底啦" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //实现刷新数据功能
    public void freshData(){
        status = STATUS_FRESH ;
        builder.curPage = 1 ;

        builder.putParams("curPage" , builder.curPage) ;
        getData();

    }

    //实现加载更多功能
    public void loadMore(){
        status = STATUS_LOADMARE ;
        builder.curPage = builder.curPage + 1 ;

        builder.putParams("curPage" , builder.curPage) ;
        getData();
    }

    //从服务器获得数据
    public void getData(){
        okhttpHelper.doGet(builder.uri , new PageCallBack(builder.context) , builder.params);
    }

    //展示得到的数据
    public <T> void showData(List<T> mData , int totalPage , int pageSize){

        switch (status){
            case STATUS_NORMAL :

                if (builder.onPageListener !=null ){
                    builder.onPageListener.loadNormal(mData , totalPage ,pageSize);
                }

                break;
            case STATUS_LOADMARE :

                if (builder.onPageListener !=null ){
                    builder.onPageListener.loadMoreData(mData , totalPage ,pageSize);
                }
                builder.materialRefreshLayout.finishRefreshLoadMore();
                status = STATUS_NORMAL ;
                break;
            case STATUS_FRESH :

                if (builder.onPageListener !=null ){
                    builder.onPageListener.refData(mData , totalPage ,pageSize);
                }
                builder.materialRefreshLayout.finishRefresh();
                status = STATUS_NORMAL ;
                break;
        }
    }

    public static Pager.Builder getBuilder(){

        return  builder = new Builder() ;
    }

    public interface OnPageListener<T>{
        void loadNormal(List<T> mData , int totalPage , int pageSize) ;
        void loadMoreData(List<T> mData , int totalPage , int pageSize);
        void refData(List<T> mData , int totalPage , int pageSize) ;
    }

    /**
     * 用来盛放所需要的所有数据。只涉及到数据的存取
     */
    public static class Builder {

        public int curPage = 1 ;
        private int pageSize = 10 ;
        public int totalCount = 1 ;
        private String uri ;
        private MaterialRefreshLayout materialRefreshLayout ;
        private Type type ;
        private Context context ;
        private OnPageListener onPageListener ;
        private List<Ware> mData ;
        public Map<String, String> params ;

        public Map<String, String> getParams() {
            return params;
        }

        public Builder (){
            params = new HashMap<>();
        }

        public Builder setOnPageListener(OnPageListener onPageListener){
            builder.onPageListener = onPageListener ;
            return builder;
        }

        public Builder setMaterialRefreshLayout(MaterialRefreshLayout materialRefreshLayout){

            builder.materialRefreshLayout = materialRefreshLayout ;
            return builder ;

        }

        public Builder setUri(String uri){

            builder.uri = uri ;
            return builder ;

        }

        public Pager build(Context context , Type type) throws Exception {

            builder.type = type ;
            builder.context = context ;

            isVisible() ;

            return new Pager() ;
        }

        public void isVisible() throws Exception {
            if(builder.uri == null || builder.curPage==0 && builder.pageSize==0){
                throw new Exception("uri 为空") ;
            }
        }

        public Builder putParams(String Key , Object value){
            builder.params.put(Key , value+"") ;
            return builder ;
        }


    }

    /**
     * 继承loadingSpotsDialog<Page<T>>实现对网络加载拦截处理。
     * @param <T>
     */
    class PageCallBack<T> extends loadingSpotsDialog<Page<T>> {

        public PageCallBack(Context context){
            super(context);
            this.type = builder.type ;
        }

        @Override
        public void onErroe(Response response, int responseCode, Exception e) throws IOException {
            this.closeSpotsDialog();
        }

        @Override
        public void callBackSucces(Response response, Page<T> page) throws IOException {

            builder.curPage = page.getCurrentPage() ;
            builder.totalCount = page.getTotalCount() ;

            showData(page.getList() , page.getTotalPage() , page.getPageSize());
            this.closeSpotsDialog();
        }
    }

}
