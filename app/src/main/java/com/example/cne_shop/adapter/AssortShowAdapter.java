package com.example.cne_shop.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.widget.TextView;

import com.example.cne_shop.R;
import com.example.cne_shop.base.BaseViewHolder;
import com.example.cne_shop.bean.Ware;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

/**
 * Created by 博 on 2017/8/23.
 */

public class AssortShowAdapter extends SimpleAdapter<Ware> {

    private SimpleDraweeView hotGoodsImg ;
    private TextView hotGoodsDescription ;
    private TextView hotGoodsPrice ;

    public AssortShowAdapter(Context context, List<Ware> mDatas ) {
        super(context, mDatas, R.layout.assort_show_adapter);
    }

    @Override
    public void bindData(BaseViewHolder holder, Ware classifyWaresData , int position) {

        hotGoodsImg = holder.findSimpleDraweeView(R.id.hotGoodsImg) ;
        hotGoodsDescription = holder.findTextView(R.id.hotGoodsDescription);
        hotGoodsPrice = holder.findTextView(R.id.hotGoodsPrice);

        hotGoodsDescription.setText(classifyWaresData.getName());
        hotGoodsPrice.setText(classifyWaresData.getPrice());
        inithotGoodsImg() ;
        loadHotGoodsImg( classifyWaresData ) ;
    }

    public void inithotGoodsImg(){
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(context.getResources())
                .setFadeDuration(500)
//                    .setPlaceholderImage(R.mipmap.ic_launcher)
//                    .setPlaceholderImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                .build();

        RoundingParams roundingParams = new RoundingParams() ;
        roundingParams.setRoundAsCircle(false) ;
        roundingParams.setCornersRadius(200) ;
        hierarchy.setRoundingParams(roundingParams);

        hotGoodsImg.setHierarchy(hierarchy);
    }

    //利用SimpleDraweeView加载图片
    public void loadHotGoodsImg( Ware hotGoodsMsgPart ){
        hotGoodsImg.setController(Fresco.newDraweeControllerBuilder()
                .setControllerListener(new BaseControllerListener<ImageInfo>(){
                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                    }
                }).setImageRequest(ImageRequestBuilder.newBuilderWithSource(
                        Uri.parse(hotGoodsMsgPart.getImgUrl()))
                        .setProgressiveRenderingEnabled(true).build())
                .setOldController(hotGoodsImg.getController()).build());
    }

}
