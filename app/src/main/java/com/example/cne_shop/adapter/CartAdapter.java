package com.example.cne_shop.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.cne_shop.R;
import com.example.cne_shop.base.BaseViewHolder;
import com.example.cne_shop.bean.ShoppingCart;
import com.example.cne_shop.widget.NumControlerView;
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
 * Created by 博 on 2017/7/14.
 */

public class CartAdapter extends SimpleAdapter<ShoppingCart> {

    private CheckBox checkBox ;
    private SimpleDraweeView simpleDraweeView ;
    private TextView wareDescription ;
    private TextView warePrice ;
    private NumControlerView numControlerView ;
    private NumControlerListener numControlerListener ;

    public CartAdapter(Context context, List<ShoppingCart> mDatas) {
        super(context, mDatas, R.layout.cart_recylerclview_adapter);
    }

    public interface NumControlerListener{
        void onAdd(int position, int value);
        void onSub(int position, int value);

    }

    @Override
    public void bindData(BaseViewHolder holder, ShoppingCart cart , final int position) {

        checkBox = holder.findCheckBox(R.id.checkbox) ;
        simpleDraweeView = holder.findSimpleDraweeView(R.id.wareImg) ;
        wareDescription = holder.findTextView(R.id.wareDescription) ;
        warePrice = holder.findTextView(R.id.warePrice) ;
        numControlerView = holder.findNumControlerView(R.id.num) ;

        checkBox.setChecked(cart.getChecked());
        wareDescription.setText(cart.getName());
        warePrice.setText(cart.getPrice());
        numControlerView.setValue(cart.getCount());

        numControlerView.setValueChangeListener(new NumControlerView.onNumChangedListener() {
            @Override
            public void addValueListener(View v , int value) {
                numControlerListener.onAdd(position , value);
            }

            @Override
            public void subValueListener(View v , int value) {
                numControlerListener.onSub(position , value);
            }
        });

        inithotGoodsImg() ;
        loadHotGoodsImg(cart) ;
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

        simpleDraweeView.setHierarchy(hierarchy);
    }

    //利用SimpleDraweeView加载图片
    public void loadHotGoodsImg( ShoppingCart shoppingCart ){
        simpleDraweeView.setController(Fresco.newDraweeControllerBuilder()
                .setControllerListener(new BaseControllerListener<ImageInfo>(){
                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                    }
                }).setImageRequest(ImageRequestBuilder.newBuilderWithSource(
                        Uri.parse(shoppingCart.getImgUrl()))
                        .setProgressiveRenderingEnabled(true).build())
                .setOldController(simpleDraweeView.getController()).build());
    }

    public void setNumControlerListener (NumControlerListener numControlerListener){
        this.numControlerListener = numControlerListener ;
    }

}
