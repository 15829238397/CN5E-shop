package com.example.cne_shop.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.cne_shop.R;
import com.example.cne_shop.base.BaseViewHolder;
import com.example.cne_shop.bean.AssortFirstData;

import java.util.List;

/**
 * Created by 博 on 2017/8/23.
 */

public class AssortFirstAdapter extends SimpleAdapter<AssortFirstData> {

    private TextView name ;

    public AssortFirstAdapter(Context context, List<AssortFirstData> mDatas) {
        super(context, mDatas, R.layout.assort_first_adapter);
    }

    @Override
    public void bindData(BaseViewHolder holder, AssortFirstData firstClassifyData , int position) {

        name = holder.findTextView(R.id.text) ;

        name.setText(firstClassifyData.getName());

    }

}
