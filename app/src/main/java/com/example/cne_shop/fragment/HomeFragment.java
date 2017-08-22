package com.example.cne_shop.fragment;


import android.util.Log;
import android.view.View;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.cne_shop.R;
import com.example.cne_shop.base.BaseFragment;
import com.example.cne_shop.bean.SliderIndicator;
import com.example.cne_shop.contents.Contents;
import com.example.cne_shop.okhttp.OkhttpHelper;
import com.example.cne_shop.okhttp.loadingSpotsDialog;
import com.example.cne_shop.widget.CnToolbar;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by 博 on 2017/8/21.
 */

public class HomeFragment extends BaseFragment {

    CnToolbar cnToolbar ;
    SliderLayout sliderLayout ;
    View mView ;

    @Override
    protected int getResRootViewId() {
        return R.layout.home_main_fragment;
    }

    @Override
    protected void init(View view) {
        mView = view ;
        cnToolbar = (CnToolbar) mView.findViewById(R.id.toolBar);
        initSlider() ;
    }

    public List<SliderIndicator> getWoringWebConectList(){
        List<SliderIndicator> sliderWebConectlist = new ArrayList<SliderIndicator>() ;
        sliderWebConectlist.add(new SliderIndicator("网络连接错误" , "File://R/web_wrong/web_wrong")) ;
        return sliderWebConectlist ;
    }

    public void initSlider () {
        OkhttpHelper okhttpHelper = OkhttpHelper.getOkhttpHelper();

        Map<String , String> params = new HashMap<String , String>() ;
        params.put("type" , 1+"") ;

        okhttpHelper.doGet(Contents.API.QUERY_SLIDER , new loadingSpotsDialog<List<SliderIndicator>>(mContext) {

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("发送请求数据异常" , "---------->"+e) ;
                initSlider(getWoringWebConectList());
                this.closeSpotsDialog();
            }

            @Override
            public void onErroe(Response response, int responseCode, Exception e) throws IOException {
                this.closeSpotsDialog();
                Log.e("接收数据异常" , "---------->"+e) ;
                initSlider(getWoringWebConectList());
            }

            @Override
            public void callBackSucces(Response response, List<SliderIndicator> sliderIndicator) throws IOException {
                this.closeSpotsDialog();
                initSlider(sliderIndicator);
            }
        } , params);
    }

    public void initSlider(List<SliderIndicator> sliderIndicators ){
        sliderLayout = (SliderLayout) mView.findViewById(R.id.slider);
        //添加图片控件
        sliderLayout.removeAllSliders();
        for(SliderIndicator sliderIndicator : sliderIndicators){
            addTextSliderView(sliderIndicator) ;
        }
        //设置指示器的位置
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        //设置图片的切换效果
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        //添加textView动画特效
        //设置切换时长2000 ,时长越小，切换速度越快
        sliderLayout.setDuration(3000);
    }

    public void addTextSliderView (SliderIndicator sliderIndicator){
        TextSliderView textSliderView1=new TextSliderView(mView.getContext());
        textSliderView1.description(sliderIndicator.getName());//设置标题
        textSliderView1.image(sliderIndicator.getImgUrl()).error(R.drawable.web_wrong);//设置图片的网络地址
        textSliderView1.setScaleType(BaseSliderView.ScaleType.CenterCrop);//设置图片的缩放效果;
        //添加到布局中显示
        sliderLayout.addSlider(textSliderView1);
    }

}
