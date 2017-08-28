package com.example.cne_shop.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;

import com.example.cne_shop.R;
import com.example.cne_shop.base.BaseViewHolder;
import com.example.cne_shop.bean.ShoppingCart;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

/**
 * Created by 博 on 2017/7/25.
 */

public class OrderWareMsgAdapter extends SimpleAdapter<ShoppingCart> {

    private SimpleDraweeView simpleDraweeView ;

    public OrderWareMsgAdapter(Context context, List<ShoppingCart> mDatas ) {
        super(context, mDatas, R.layout.order_waremsg_adapter);
    }

    @Override
    public void bindData(BaseViewHolder holder, ShoppingCart ware, int position) {

        simpleDraweeView = holder.findSimpleDraweeView(R.id.wareImg) ;

        inithotGoodsImg() ;
        loadHotGoodsImg(ware) ;
    }

    public void inithotGoodsImg(){
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(context.getResources())
                .setFadeDuration(500)
//                    .setPlaceholderImage(R.mipmap.ic_launcher)
//                    .setPlaceholderImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                .build();

        RoundingParams roundingParams = new RoundingParams() ;
        roundingParams.setRoundAsCircle(true) ;
        roundingParams.setCornersRadius(200) ;
        hierarchy.setRoundingParams(roundingParams);

        simpleDraweeView.setHierarchy(hierarchy);
    }

    //利用SimpleDraweeView加载图片
    public void loadHotGoodsImg( ShoppingCart hotGoodsMsgPart ){
        simpleDraweeView.setController(Fresco.newDraweeControllerBuilder()
                .setControllerListener(new BaseControllerListener<ImageInfo>(){
                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                    }
                }).setImageRequest(ImageRequestBuilder.newBuilderWithSource(
                        Uri.parse(hotGoodsMsgPart.getImgUrl()))
                        .setProgressiveRenderingEnabled(true).build())
                .setOldController(simpleDraweeView.getController()).build());
    }
}
