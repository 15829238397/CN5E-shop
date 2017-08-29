package com.example.cne_shop.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.cne_shop.widget.NumControlerView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.ninegrid.NineGridView;


/**
 * Created by 博 on 2017/7/12.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    //定义一个SparseArray<View>来存储所有的view
    private SparseArray<View> views ;
    private View itemView ;
    private BaseAdapter.onItemClickListener listener ;


    public BaseViewHolder(View itemView , BaseAdapter.onItemClickListener listener ) {
        super(itemView);

        this.itemView = itemView ;
        this.views = new SparseArray<View>() ;
        this.listener = listener ;

    }


    /**
     * 暴露给外界的方法用来获得view实例
     */

    public SimpleDraweeView findSimpleDraweeView(int resId){
        return findView(resId) ;
    }

    public ImageView findImageView(int resId){
        return findView(resId) ;
    }

    public Button findButton (int resId){
        return findView(resId) ;
    }

    public TextView findTextView(int resId){
        return findView(resId) ;
    }

    public NineGridView findNineGridView(int resId){
        return findView(resId) ;
    }

    public RadioButton findRadioButton(int resId){
        return findView(resId) ;
    }

    public CheckBox findCheckBox(int resId) {return findView(resId) ; }

    public NumControlerView findNumControlerView(int resId) {return findView(resId) ;}

    //获得view实例对象。在views里查找，如果存在，直接返回，如果不存在实例化一个，并将实例化结果返回。
    private < T extends View> T findView (int resId){

        if ( views.get(resId) == null ){
            View view = itemView.findViewById(resId) ;
            views.put(resId , view);
            //给新得到的view增加点击事件
            view.setOnClickListener(this);
        }

       return (T) views.get(resId) ;
    }

    @Override
    public void onClick(View v) {
        if(listener != null)
            try {
                listener.onClick(v , getLayoutPosition() );
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

}
