package com.example.cne_shop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.cne_shop.R;
import com.example.cne_shop.base.BaseViewHolder;
import com.example.cne_shop.bean.OrderMsg;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.NineGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 博 on 2017/7/28.
 */

public class MyOrderAdapter extends SimpleAdapter<OrderMsg>{

    private TextView orderNum ;
    private TextView isSuccess ;
    private NineGridView waresImg ;
    private TextView waresPriceNum ;
    private Button buyAgain ;

    public MyOrderAdapter(Context context, List<OrderMsg> mDatas ) {
        super(context, mDatas, R.layout.myorder_item_adapter);
    }

    @Override
    public void bindData(BaseViewHolder holder, OrderMsg orderMsg, int position) {

        orderNum = holder.findTextView(R.id.order_num) ;
        isSuccess = holder.findTextView(R.id.isSuccess) ;
        waresImg = holder.findNineGridView(R.id.waresImg) ;
        waresPriceNum = holder.findTextView(R.id.waresPriceNum) ;
        buyAgain = holder.findButton(R.id.buyAgain) ;

        orderNum.setText(orderMsg.getOrderNum()) ;
        Log.d("----" , "---------------" + orderMsg.getAmount()) ;
        waresPriceNum.setText((float)orderMsg.getAmount() + "");

        switch (orderMsg.getStatus()){
            case 1 :
                isSuccess.setText("支付成功") ;
                isSuccess.setTextColor(Color.GREEN);

                break;
            case -2 :
                isSuccess.setText("支付失败") ;
                isSuccess.setTextColor(Color.RED);

                break;
            case 0 :
                isSuccess.setText("等待支付") ;
                isSuccess.setTextColor(Color.BLUE);

                break;
        }

        initNineGridLayout(orderMsg) ;

    }

    private void initNineGridLayout(OrderMsg orderMsg) {

        List<ImageInfo> imageInfos = new ArrayList<>() ;
        for (int i = 0; i < orderMsg.getItems().size() ; i++) {
            ImageInfo im = new ImageInfo() ;
            im.setBigImageUrl(orderMsg.getItems().get(i).getWares().getImgUrl());
            im.setThumbnailUrl(orderMsg.getItems().get(i).getWares().getImgUrl());
            imageInfos.add(im) ;
        }

        nineGridAdapter nGridAdater = new nineGridAdapter(context , imageInfos) ;

        waresImg.setAdapter(nGridAdater);

    }

    class nineGridAdapter extends NineGridViewAdapter {

        public nineGridAdapter(Context context, List<ImageInfo> imageInfo) {
            super(context, imageInfo);
        }
    }
}
