package com.example.cne_shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.cne_shop.R;
import com.example.cne_shop.adapter.ConsigneeAddressAdapter;
import com.example.cne_shop.application.MyApplication;
import com.example.cne_shop.base.BaseActivity;
import com.example.cne_shop.base.BaseAdapter;
import com.example.cne_shop.bean.ConsigneeMsg;
import com.example.cne_shop.bean.SubmitOrderMsg;
import com.example.cne_shop.contents.Contents;
import com.example.cne_shop.okhttp.OkhttpHelper;
import com.example.cne_shop.okhttp.loadingSpotsDialog;
import com.example.cne_shop.widget.CnToolbar;
import com.example.cne_shop.widget.MyDivider;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by 博 on 2017/7/27.
 */

public class ShowConsigneeAdrActivity extends BaseActivity {

    private ConsigneeAddressAdapter consigneeAddressAdapter ;
    private List<ConsigneeMsg> consigneeMsgs ;
    private static OkhttpHelper okhttpHelper = OkhttpHelper.getOkhttpHelper() ;
    @BindView(R.id.toolBar)
    CnToolbar cnToolbar ;
    @BindView(R.id.recycleView)
    RecyclerView recyclerView ;

    @Override
    protected int getContentViewId() {
        return R.layout.show_addres_activity;
    }

    protected void initView() {

        initPbTool() ;
        initRecyclerView() ;

    }

    @Override
    protected void addListener() {

    }

    @Override
    protected void beforLayout() {

    }

    /**
     * 初始化recyclerView
     */
    private void initRecyclerView() {

        getData();

    }

    private void initPbTool() {
        addPbToolListener() ;
    }

    private void addPbToolListener() {

        cnToolbar.setLeftButtonIcOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cnToolbar.setRightImgeButtonIcOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowConsigneeAdrActivity.this , EditeConsigneeAdrActivity.class) ;

                intent.putExtra("isEdite" , false) ;

                startActivityForResult(intent , Contents.REQUEST_CONSIGNEE_ADR);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Contents.REQUEST_CONSIGNEE_ADR){
            if(resultCode == RESULT_OK){

                //添加成功
                getData();
            }
        }

    }

    public void getData() {

        Map<String , String> params = new HashMap<>() ;
        params.put("user_id" , MyApplication.getInstance().getUser().getId()+"") ;

       okhttpHelper.doGet(Contents.API.GET_CONSIGNEE, new loadingSpotsDialog<List<ConsigneeMsg>>(this) {
           @Override
           public void onErroe(Response response, int responseCode, Exception e) throws IOException {
               this.closeSpotsDialog();
           }

           @Override
           public void callBackSucces(Response response, List<ConsigneeMsg> Msgs) throws IOException {
               this.closeSpotsDialog();
               consigneeMsgs = Msgs ;
               showData( consigneeMsgs) ;
           }
       }, params);
    }

    private void showData(List<ConsigneeMsg> consigneeMsgs) {

        if(consigneeMsgs != null && consigneeMsgs.size() > 0 ){

            orderItem();

            if(consigneeAddressAdapter == null){

                consigneeAddressAdapter = new ConsigneeAddressAdapter(this , consigneeMsgs) ;
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.addItemDecoration(new MyDivider());
                recyclerView.setAdapter(consigneeAddressAdapter);

                addConsigneeAddressAdapterListener() ;

            }else {
                consigneeAddressAdapter.cleanData();
                consigneeAddressAdapter.addData(consigneeMsgs);
                Log.d("----" , "更改默认") ;
            }
    }else {
            if (consigneeAddressAdapter !=null ){

                Log.d("----" , "删除最后一条") ;
                consigneeAddressAdapter.cleanData();
                MyApplication.getInstance().getUser().setDefauteConsigen(null);
            }
        }
    }

    private void orderItem() {

        for (int i = 0; i < consigneeMsgs.size() ; i++) {
            if (consigneeMsgs.get(i).isDefault()){
                MyApplication.getInstance().getUser().setDefauteConsigen(consigneeMsgs.get(i));
                ConsigneeMsg tmp = consigneeMsgs.get(i) ;
                consigneeMsgs.remove(i) ;
                consigneeMsgs.add(0 , tmp);
                return;
            }
        }

        setDefauteAdr(0 , true);
        getData();

    }

    private void addConsigneeAddressAdapterListener() {

        consigneeAddressAdapter.setOnItemClickListener(new BaseAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int position) throws Exception {

                if (view.getId() == R.id.radio_selected && !consigneeMsgs.get(position).isDefault()){

                    for (int i = 0; i < consigneeMsgs.size(); i++) {
                        if (consigneeMsgs.get(i).isDefault()){
                            setDefauteAdr(i , position) ;
                            getData();
                            return;
                        }
                    }

                    setDefauteAdr(0 , true);
                    getData();
                    return;

                }else if (view.getId() == R.id.delete_address) {

                    deleteAdr(consigneeMsgs.get(position).getId());

                }else if(view.getId() == R.id.edite_address){

                    startEditeConsigneeAdrActivity(position) ;

                }

        }
        });
    }

    private void startEditeConsigneeAdrActivity(int position) {

        Intent intent = new Intent(this , EditeConsigneeAdrActivity.class) ;
        intent.putExtra( "consigneeName" , consigneeMsgs.get(position).getConsignee() ) ;
        intent.putExtra("consigneePhone" , consigneeMsgs.get(position).getPhone()) ;
        String[] adrs = consigneeMsgs.get(position).getAddr().split(" ") ;
        intent.putExtra("selectAdr" ,adrs[0] ) ;
        intent.putExtra("addAdr" , adrs[1]) ;
        intent.putExtra("isEdite" , true) ;
        intent.putExtra("isDefault" , consigneeMsgs.get(position).isDefault()) ;
        intent.putExtra("id" , consigneeMsgs.get(position).getId()) ;
        intent.putExtra("edte_zip_code" , consigneeMsgs.get(position).getZipCode()) ;

        startActivityForResult(intent , Contents.REQUEST_CONSIGNEE_ADR);

    }

    private void setDefauteAdr(int prePosition , int curPosition ){
        setDefauteAdr(prePosition , false);
        setDefauteAdr(curPosition , true);
    }

    private void setDefauteAdr(final int curPosition , final Boolean isDefaut){
        Map<String , String> params = new HashMap<String , String>() ;
        params.put("id" , consigneeMsgs.get(curPosition).getId() + "") ;
        params.put("consignee" , consigneeMsgs.get(curPosition).getConsignee() ) ;
        params.put("phone" , consigneeMsgs.get(curPosition).getPhone()) ;
        params.put("addr" , consigneeMsgs.get(curPosition).getAddr()) ;
        params.put("zip_code" , consigneeMsgs.get(curPosition).getZipCode() ) ;
        params.put("is_default" , isDefaut + "") ;

        okhttpHelper.doPost( Contents.API.UPDATE_CONSIGNEE , new loadingSpotsDialog<SubmitOrderMsg>(this) {
            @Override
            public void onErroe(Response response, int responseCode, Exception e) throws IOException {
                this.closeSpotsDialog();
            }

            @Override
            public void callBackSucces(Response response, SubmitOrderMsg submitOrderMsg) throws IOException {
                this.closeSpotsDialog();


                if(submitOrderMsg.getStatus() == 1){
                    if (isDefaut)
                    MyApplication.getInstance().getUser().setDefauteConsigen(consigneeMsgs.get(curPosition));
                }else {
                    Toast.makeText(ShowConsigneeAdrActivity.this , "提交失败" + submitOrderMsg.getMessage() , Toast.LENGTH_SHORT).show();
                }
            }

        }, params);


    }

    private void deleteAdr(int id) {
        Map<String , String> params = new HashMap<>() ;
        params.put("id" , id+"") ;

        okhttpHelper.doPost(Contents.API.DELETE_CONSIGNEE, new loadingSpotsDialog<SubmitOrderMsg>(this) {
            @Override
            public void onErroe(Response response, int responseCode, Exception e) throws IOException {
                this.closeSpotsDialog();
            }

            @Override
            public void callBackSucces(Response response, SubmitOrderMsg submitOrderMsg) throws IOException {
                this.closeSpotsDialog();
                if(submitOrderMsg.getStatus() == 1){
                    getData();
                }else {
                    Toast.makeText(ShowConsigneeAdrActivity.this , ""+submitOrderMsg.getMessage() ,Toast.LENGTH_SHORT).show();
                }

            }
        }, params);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("----", "onRestart: 刷新");
        getData();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.d("----", "onStop: ");
//    }
}
