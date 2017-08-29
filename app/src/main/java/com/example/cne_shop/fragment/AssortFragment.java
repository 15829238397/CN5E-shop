package com.example.cne_shop.fragment;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.example.cne_shop.R;
import com.example.cne_shop.activity.WareDetialActivity;
import com.example.cne_shop.adapter.AssortFirstAdapter;
import com.example.cne_shop.adapter.AssortShowAdapter;
import com.example.cne_shop.base.BaseAdapter;
import com.example.cne_shop.base.BaseFragment;
import com.example.cne_shop.bean.AssortFirstData;
import com.example.cne_shop.bean.Page;
import com.example.cne_shop.bean.SliderIndicator;
import com.example.cne_shop.bean.Ware;
import com.example.cne_shop.contents.Contents;
import com.example.cne_shop.okhttp.OkhttpHelper;
import com.example.cne_shop.okhttp.loadingSpotsDialog;
import com.example.cne_shop.utils.Pager;
import com.example.cne_shop.widget.MyDivider;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 博 on 2017/8/21.
 */

public class AssortFragment extends BaseFragment {
    private RecyclerView assortFirst ;
    private RecyclerView assortShow ;
    private OkhttpHelper okhttpHelper = OkhttpHelper.getOkhttpHelper() ;
    private AssortFirstAdapter assortFirstAdapter ;
    private SliderLayout sliderLayout ;
    private MaterialRefreshLayout materialRefreshLayout ;
    private List<Ware> mData ;
    private Pager pager ;
    private int totalCount = 1 ;
    private int curPage = 1 ;
    private int pageSize = 10 ;
    private int categoryId = 1 ;
    private int lastCategoryId = 1 ;
    private AssortShowAdapter assortShowAdapter ;

    private static final int STATUS_NORMAL = 0 ;
    private static final int STATUS_FRESH = 1 ;
    private static final int STATUS_LOADMARE = 2 ;

    private int status = STATUS_NORMAL ;

    @Override
    protected int getResRootViewId() {
        return R.layout.assort_main_fragment;
    }

    @Override
    protected void init() {
        assortFirst = (RecyclerView) mView.findViewById(R.id.ass_first_recyclerview);
        sliderLayout = (SliderLayout) mView.findViewById(R.id.slidLayout) ;
        materialRefreshLayout = (MaterialRefreshLayout) mView.findViewById(R.id.materialRefreshLayout);
        assortShow = (RecyclerView) mView.findViewById(R.id.ass_show_recyclerview) ;

        curPage = 1 ;
        categoryId = 1 ;

        initFirstClassify() ;
        initSlider() ;
        try {
            initMaterialRefrshLayoutListener() ;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void initMaterialRefrshLayoutListener () throws Exception {

        String uri = Contents.API.WARE_LIST;

        final Pager.Builder builder = Pager.getBuilder()
                .setMaterialRefreshLayout(materialRefreshLayout)
                .putParams("categoryId" , 1)
                .putParams("curPage" , 1)
                .putParams("pageSize" , 10)
                .setUri(uri)
                .setOnPageListener(new Pager.OnPageListener<Ware>() {
                    @Override
                    public void loadNormal(List<Ware> mData, int totalPage, int pageSize) {

                        if( mData!=null && mData.size() > 0 ){
                            if(assortShowAdapter == null){

                                assortShowAdapter = new AssortShowAdapter(mContext, mData);
                                assortShow.setAdapter(assortShowAdapter);
                                assortShow.setLayoutManager(new GridLayoutManager(mContext , 2));
                                assortShow.addItemDecoration(new MyDivider());
                                addAssortShowAdapterListener() ;

                            }else{

                                assortShowAdapter.cleanData();
                                assortShowAdapter.addData(mData);

                            }
                        }else {
                            Toast.makeText(mContext , "该类别暂无商品" , Toast.LENGTH_SHORT) .show();
                        }

                    }

                    @Override
                    public void loadMoreData(List<Ware> mData, int totalPage, int pageSize) {
                        assortShowAdapter.addData(mData);
                    }

                    @Override
                    public void refData(List<Ware> mData, int totalPage, int pageSize) {
                        assortShowAdapter.cleanData();
                        assortShowAdapter.addData(mData);
                        assortShow.scrollToPosition(0);
                    }
                }) ;

        pager = builder.build(mContext , Page.class) ;
    }


    private void addAssortShowAdapterListener(){
        assortShowAdapter.setOnItemClickListener(new BaseAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int position) throws Exception {

                Ware ware = assortShowAdapter.getData(position);
                Intent intent = new Intent(getActivity() , WareDetialActivity.class);
                intent.putExtra(Contents.WARE , ware);

                startActivity(intent);

            }
        });
    }

    public void initFirstClassify(){
        requestAssortFirstData() ;
    }

    public void requestAssortFirstData(){

        String uri = Contents.API.CATEGORY_LIST ;

        okhttpHelper.doGet(uri, new loadingSpotsDialog<List<AssortFirstData>>(mContext) {

            @Override
            public void onErroe(Response response, int responseCode, Exception e) throws IOException {
                this.closeSpotsDialog();
            }

            @Override
            public void callBackSucces(Response response, List<AssortFirstData> assortFirstDatas) throws IOException {
                this.closeSpotsDialog();
                showAssortFirstData(assortFirstDatas);
            }

        });
    }

    public void showAssortFirstData(List<AssortFirstData> assortFirstDatas) {

        assortFirstAdapter = new AssortFirstAdapter(mContext , assortFirstDatas ) ;

        assortFirst.setAdapter(assortFirstAdapter);
        assortFirst.setLayoutManager(new LinearLayoutManager(mContext));
        assortFirst.addItemDecoration(new DividerItemDecoration(mContext , DividerItemDecoration.VERTICAL));

        addFirstClassifyItemListener(assortFirstDatas) ;
    }

    public void addFirstClassifyItemListener(final List<AssortFirstData> assortFirstDatas){
        assortFirstAdapter.setOnItemClickListener(new BaseAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int position) throws Exception {

                lastCategoryId = categoryId ;
                categoryId = position + 1 ;
                pager.changeParamsInUri("categoryId" , categoryId );
                pager.changeParamsInUri("curPage" , 1);
                pager.getData();

            }
        });
    }

    public void initSlider () {
        OkhttpHelper okhttpHelper = OkhttpHelper.getOkhttpHelper();

        Map<String , String> params = new HashMap<String , String>() ;
        params.put("type" , 1+"") ;

        okhttpHelper.doGet(Contents.API.QUERY_SLIDER, new loadingSpotsDialog<List<SliderIndicator>>(mContext) {

            @Override
            public void onFailure(Request request, IOException e) {
                this.closeSpotsDialog();
                Log.e("发送请求数据异常" , "---------->"+e) ;
                initSlider(getWoringWebConectList());
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

    public List<SliderIndicator> getWoringWebConectList(){
        List<SliderIndicator> sliderWebConectlist = new ArrayList<SliderIndicator>() ;
        sliderWebConectlist.add(new SliderIndicator("网络连接错误" , "File://R/web_wrong/web_wrong")) ;
        return sliderWebConectlist ;
    }

    public void initSlider(List<SliderIndicator> sliderIndicators ){
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
        DefaultSliderView SliderView=new DefaultSliderView(mContext);
        SliderView.image(sliderIndicator.getImgUrl()).error(R.drawable.web_wrong);//设置图片的网络地址
        SliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);//设置图片的缩放效果;
        //添加到布局中显示
        sliderLayout.addSlider(SliderView);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("onStop" , "------------------------------------") ;
        assortShowAdapter = null ;
    }

}
