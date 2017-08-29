package com.example.cne_shop.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cne_shop.R;
import com.example.cne_shop.adapter.OrderWareMsgAdapter;
import com.example.cne_shop.application.MyApplication;
import com.example.cne_shop.base.BaseActivity;
import com.example.cne_shop.bean.ShoppingCart;
import com.example.cne_shop.bean.SubmitOrderMsg;
import com.example.cne_shop.contents.Contents;
import com.example.cne_shop.fragment.CartFragment;
import com.example.cne_shop.okhttp.OkhttpHelper;
import com.example.cne_shop.okhttp.loadingSpotsDialog;
import com.example.cne_shop.utils.CartProvider;
import com.example.cne_shop.utils.JsonUtil;
import com.example.cne_shop.widget.CnToolbar;
import com.example.cne_shop.widget.MyDivider;
import com.google.gson.reflect.TypeToken;
import com.pingplusplus.android.PaymentActivity;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by 博 on 2017/7/24.
 */

public class NewOrderActivity extends BaseActivity {


    @BindView(R.id.wareMsg)
    RecyclerView recyclerView ;
    @BindView(R.id.warePrice)
    TextView sumPrices ;
    @BindView(R.id.toolBar)
    CnToolbar cnToolbar ;
    @BindView(R.id.checkbox_alipay)
    RadioButton alipayButton ;
    @BindView(R.id.checkbox_wechat)
    RadioButton wechatButton ;
    @BindView(R.id.check_bd)
    RadioButton dbButton ;
    @BindView(R.id.Alipay)
    RelativeLayout alipay ;
    @BindView(R.id.wechat)
    RelativeLayout wechat ;
    @BindView(R.id.consigneeMsg)
    ImageButton consigneeMsg ;
    @BindView(R.id.bd)
    RelativeLayout db ;
    @BindView(R.id.btn_submit)
    Button submit ;
    @BindView(R.id.userMsg)
    TextView userMsg ;
    @BindView(R.id.address)
    TextView address ;
    @BindView(R.id.toOrderButton)
    ImageButton toOrderButton ;
    private OrderWareMsgAdapter orderWareMsgAdapter ;
    private CartProvider cartProvider ;
    private List<ShoppingCart> shoppingCarts ;
    private float sumPrice ;

    private List<OrderItem>  OrderItems;
    private String orderNum ;
    private static OkhttpHelper okhttpHelper = OkhttpHelper.getOkhttpHelper();

    private static final int CHECK_ALIPAY = 0 ;
    private static final int CHECK_WECHECK = 1 ;
    private static final int CHECK_DB = 2 ;

    public static int ORDER_SUCCESS = 1 ;
    public static int ORDER_FAIL = -1 ;
    public static int ORDER_GONE = -2 ;


    private static int radio_check = CHECK_ALIPAY ;
    private static String pay_channel  ;


    @Override
    protected int getContentViewId() {
        return R.layout.new_order_activity;
    }

    @Override
    protected void initView() {
        cartProvider = CartProvider.getCartProvider(this) ;

        if(MyApplication.getInstance().getUser().getDefauteConsigen() == null){
            userMsg.setText("点击右侧箭头添加收货人信息");
            address.setText("");
        }else {
            userMsg.setText(MyApplication.getInstance().getUser().getDefauteConsigen().getConsignee()
                    + "(" +
                    MyApplication.getInstance().getUser().getDefauteConsigen().getPhone().substring(0 ,3 )
                    +  "*****"+
                    MyApplication.getInstance().getUser().getDefauteConsigen().getPhone().substring(8)
                    + ")");
            address.setText(MyApplication.getInstance().getUser().getDefauteConsigen().getAddr());
        }

        getData() ;
        initRecyclerView();
        initRadioButton() ;
        sumPrices.setText("￥" + sumPrice);

    }

    @Override
    protected void beforLayout() {

    }

    private void submitOrder(){

        submit.setClickable(false);

        OrderItems = new ArrayList<OrderItem>() ;
        for (int i = 0; i <shoppingCarts.size() ; i++) {
            OrderItem o = new OrderItem((int) shoppingCarts.get(i).getId(),shoppingCarts.get(i).getCount() ) ;
            OrderItems.add(o) ;
        }

        Map<String , String> params = new HashMap<String , String>() ;
        params.put("user_id" , MyApplication.getInstance().getUser().getId()+"") ;
        params.put("item_json" , JsonUtil.toJSON(OrderItems)) ;
        params.put("amount" , ((int)sumPrice)+"") ;
        params.put("addr_id" , "1") ;
        params.put("pay_channel" , pay_channel ) ;

        okhttpHelper.doPost(Contents.API.SUBMIT_ORDER, new loadingSpotsDialog<SubmitOrderMsg>(this) {
            @Override
            public void onErroe(Response response, int responseCode, Exception e) throws IOException {
                this.closeSpotsDialog();
                submit.setClickable(true);
            }

            @Override
            public void callBackSucces(Response response, SubmitOrderMsg submitOrderMsg) throws IOException {
                this.closeSpotsDialog();

                submit.setClickable(true);
                if(submitOrderMsg.getStatus() == 1) {
                    orderNum = submitOrderMsg.getData().getOrderNum() ;
                    Object charge = submitOrderMsg.getData().getCharge() ;

                    openPaymentAcitivity(JsonUtil.toJSON(charge)) ;
                    Log.d("----" , "----------------提交的订单--------------"+orderNum + "J"+JsonUtil.toJSON(charge)) ;
                }
            }

            @Override
            public void onTokenError(Response response, int responseCode) {
                super.onTokenError(response, responseCode);
                this.closeSpotsDialog();
                submit.setClickable(true);
            }
        } , params);
    }

    private void openPaymentAcitivity(String charge){
        Intent intent = new Intent();
        String packageName = getPackageName() ;
        ComponentName componentName = new ComponentName(packageName , packageName + ".wxapi.WXPayEntryActivity") ;
        intent.setComponent(componentName) ;
        intent.putExtra(PaymentActivity.EXTRA_CHARGE , charge) ;
        startActivityForResult(intent , Contents.REQUEST_ORDER_CODE);
        Log.d("----" , "-------------------------------------打开了支付页面") ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        Log.d("----" , "-------------------------------------支付返回requestCode" + requestCode) ;
//        Log.d("----" , "-------------------------------------支付返回resultCode" + resultCode) ;
//        Log.d("----" , "-------------------------------------支付返回REQUEST_ORDER_CODE" + Contents.REQUEST_ORDER_CODE) ;
//        Log.d("----" , "-------------------------------------支付返回RESULT_OK" + Activity.RESULT_OK) ;
//        Log.d("----" , "-------------------------------------支付返回data.getExtras()." +  data.getExtras().getString("pay_result")) ;

        if (requestCode == Contents.REQUEST_ORDER_CODE){
            if (resultCode == Activity.RESULT_OK){

                String result = data.getExtras().getString("pay_result") ;
                Log.d("----" , "-------------------------------------支付返回" + result) ;
                if (result.equals("success"))
                    changeOrderStatus(ORDER_SUCCESS);
                else if (result.equals("fail"))
                    changeOrderStatus(ORDER_FAIL);
                else if (result.equals("cancel"))
                    changeOrderStatus(ORDER_GONE);
                else
                    changeOrderStatus(0);

            }
        }else if (requestCode == Contents.REQUEST_ORDER_CONSIGNEE){

            if(MyApplication.getInstance().getUser().getDefauteConsigen() == null){
                userMsg.setText("点击右侧箭头添加收货人信息");
                address.setText("");
            }else {
                userMsg.setText(MyApplication.getInstance().getUser().getDefauteConsigen().getConsignee()
                        + "(" +
                        MyApplication.getInstance().getUser().getDefauteConsigen().getPhone().substring(0 ,2 )
                        +  "*****"+
                        MyApplication.getInstance().getUser().getDefauteConsigen().getPhone().substring(8)
                        + ")");
                address.setText(MyApplication.getInstance().getUser().getDefauteConsigen().getAddr());
            }

        }
    }

    private void changeOrderStatus(int status){

        if(status == ORDER_GONE || status == ORDER_FAIL){
            toPayResule(status) ;
            return;
        }

        Log.d("----" , "---------支付无误-修改订单状态" + status ) ;
        Map<String , String> params = new HashMap<String ,String>() ;
        params.put("order_num" , orderNum) ;
        params.put("status" , status+"" );

        okhttpHelper.doPost(Contents.API.CHANGE_ORDER_STATUS, new loadingSpotsDialog<SubmitOrderMsg>(this) {
            @Override
            public void onErroe(Response response, int responseCode, Exception e) throws IOException {
                this.closeSpotsDialog();

                toPayResule(ORDER_FAIL) ;
            }

            @Override
            public void callBackSucces(Response response, SubmitOrderMsg submitOrderMsg) throws IOException {
                this.closeSpotsDialog();

                int status = submitOrderMsg.getStatus() ;
                Log.d("", "callBackSucces: ------------------------------" + status);
                toPayResule(status) ;
            }

        }, params);

    }

    private void toPayResule(int status){
        Log.d("", "toPayResule: 条状-------------------------------------------------------------------------");
        Intent intent = new Intent(this , PayResultActivity.class) ;

        if(status == 1){
            for (int i = 0; i < shoppingCarts.size() ; i++) {
                cartProvider.delete(shoppingCarts.get(i));
            }
        }
        Log.d("", "callBackSucces: ------------------------------" + status);
        intent.putExtra("status" ,status ) ;

        startActivity(intent);
        finish();

    }



    protected void addListener(){

        toOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewOrderActivity.this , MyOrderActivity.class));
            }
        });

        consigneeMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(NewOrderActivity.this , ShowConsigneeAdrActivity.class ) , Contents.REQUEST_ORDER_CONSIGNEE );

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitOrder() ;
            }
        });

        cnToolbar.setLeftButtonIcOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        alipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( radio_check != CHECK_ALIPAY ){
                    radio_check = CHECK_ALIPAY ;
                    initRadioButton() ;
                }
            }
        });

        wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( radio_check != CHECK_WECHECK){
                    radio_check = CHECK_WECHECK ;
                    initRadioButton() ;
                }

            }
        });

        db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( radio_check != CHECK_DB){
                    radio_check = CHECK_DB ;
                    initRadioButton() ;
                }
            }
        });

    }

    private void getData(){

        Intent intent = getIntent() ;
        Type type = new TypeToken<List<ShoppingCart>>(){}.getType() ;
        shoppingCarts = JsonUtil.fromJson(intent.getStringExtra(CartFragment.ORDER_WARES) , type ) ;
        sumPrice = intent.getFloatExtra(CartFragment.SUM_PRICE , 0) ;

    }

    public void initRecyclerView (){

        getData () ;

        if(cartProvider!=null){

            orderWareMsgAdapter = new OrderWareMsgAdapter(this , shoppingCarts) ;
            recyclerView.setAdapter(orderWareMsgAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false));
            recyclerView.addItemDecoration(new MyDivider());

        }else {
            Log.d("----" , "获得的值为null--------------------------------------");
        }
    }

    private void initRadioButton (){

        switch (radio_check){
            case CHECK_ALIPAY:

                alipayButton.setChecked(true);
                dbButton.setChecked(false);
                wechatButton.setChecked(false);
                pay_channel = Contents.ALIPAY ;

                break;
            case CHECK_DB:

                alipayButton.setChecked(false);
                dbButton.setChecked(true);
                wechatButton.setChecked(false);
                pay_channel = Contents.BD ;

                break;
            case CHECK_WECHECK:

                alipayButton.setChecked(false);
                dbButton.setChecked(false);
                wechatButton.setChecked(true);

                pay_channel = Contents.WX ;

                break;
        }

    }

    class OrderItem {

        private int ware_id ;
        private int amount ;

        public OrderItem(int ware_id , int amout) {
            super();
            this.amount = amout ;
            this.ware_id = ware_id ;
        }
    }


}
