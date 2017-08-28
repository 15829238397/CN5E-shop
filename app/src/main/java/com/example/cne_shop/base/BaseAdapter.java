package com.example.cne_shop.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 博 on 2017/7/12.
 */

public abstract class BaseAdapter<T , H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> mDatas ;
    protected Context context ;
    protected LayoutInflater inflater ;
    protected int resId ;
    protected View mView ;
    protected onItemClickListener listener ;
    private List<Boolean> isSelecterd ;

    //新建一个接口，用于响应点击事件
    public interface onItemClickListener{
        void onClick(View view, int position) throws Exception;
    } ;

    //设置点击事件
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener ;
    }

    public BaseAdapter(Context context , List<T> mDatas , int resId) {

        this.context = context ;
        this.mDatas = mDatas ;
        this.inflater = LayoutInflater.from(context) ;
        this.resId = resId ;
        this.isSelecterd = new ArrayList<Boolean>() ;

    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mView = inflater.inflate(resId , null , false) ;

        return new BaseViewHolder(mView , listener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T t = getData( position) ;
        bindData(holder , t , position);
    }

    @Override
    public int getItemCount() {

        if (mDatas == null){
            return 0 ;
        }

            for (int i = 0 ; i < mDatas.size() ; i++){
                isSelecterd.add(false) ;
            }

        return mDatas.size();
    }

    public abstract void bindData(BaseViewHolder holder , T t , int position);

    public T getData ( int positon){
        return mDatas.get(positon) ;
    }

    public void cleanData(){

        this.notifyItemRangeRemoved(0 , mDatas.size());
        mDatas.clear();

    }

    public void addData(List<T> data){

        mDatas.addAll(mDatas.size() , data ) ;
        this.notifyItemRangeChanged(0 , mDatas.size());

    }

    public int getMdataSize(){
        return mDatas.size() ;
    }



}
