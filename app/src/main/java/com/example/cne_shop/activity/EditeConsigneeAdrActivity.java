package com.example.cne_shop.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.cne_shop.R;
import com.example.cne_shop.application.MyApplication;
import com.example.cne_shop.base.BaseActivity;
import com.example.cne_shop.bean.SubmitOrderMsg;
import com.example.cne_shop.bean.city.XmlParserHandler;
import com.example.cne_shop.bean.city.model.PrivinceModel;
import com.example.cne_shop.contents.Contents;
import com.example.cne_shop.okhttp.OkhttpHelper;
import com.example.cne_shop.okhttp.loadingSpotsDialog;
import com.example.cne_shop.widget.CnToolbar;
import com.example.cne_shop.widget.MyEditText;
import com.squareup.okhttp.Response;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import butterknife.BindView;

/**
 * Created by 博 on 2017/7/27.
 */

public class EditeConsigneeAdrActivity extends BaseActivity {

    private List<String> province ;
    private List<List<String>> city ;
    private List<List<List<String>>> zip_codes ;
    @BindView(R.id.consigneeAdr)
    TextView consigneeAdr ;
    @BindView(R.id.toolBar)
    CnToolbar cnToolbar ;
    @BindView(R.id.consignee_name)
    MyEditText consigneeName ;
    @BindView(R.id.consignee_phone)
    MyEditText consigneePhone ;
    @BindView(R.id.consignee_address)
    MyEditText consigneeAdrs ;
    private List<List<List<String>>> county ;
    private String zip_code ;
    private boolean isEdite ;
    private boolean isDefault ;
    private int id ;
    private String edte_zip_code ;

    private OkhttpHelper okhtp = OkhttpHelper.getOkhttpHelper() ;

    @Override
    protected int getContentViewId() {
        return R.layout.add_consigneeadr_activity;
    }

    protected void initView() {

        consigneePhone.setInputType(InputType.TYPE_CLASS_NUMBER); //输入类型
        consigneePhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)}); //最大输入长度

        Intent intent = getIntent() ;
        isEdite = intent.getBooleanExtra("isEdite" , false) ;
        Log.d("----", "initView: isEdite ");

        if(isEdite){

            consigneeName.setText(intent.getStringExtra("consigneeName"));
            consigneePhone.setText(intent.getStringExtra("consigneePhone"));
            consigneeAdr.setText(intent.getStringExtra("selectAdr"));
            consigneeAdrs.setText(intent.getStringExtra("addAdr"));

            isDefault = intent.getBooleanExtra("isDefault" , false) ;
            edte_zip_code = intent.getStringExtra("edte_zip_code") ;
            Log.d("----", "initView: isDefault " + isDefault);
            id = intent.getIntExtra("id" , 0) ;

            cnToolbar.setTitle("编辑收货地址");

        }

    }


    private boolean getData(String phoneCode  ){

        if(consigneeName.getText()==null || consigneeName.getText().toString().trim().equals("") || (consigneeName.getText().toString().trim().length() == 0)){
            Toast.makeText(EditeConsigneeAdrActivity.this , "请填写收货人" , Toast.LENGTH_SHORT).show();
            return false;
        }

        if(consigneeAdr.getText()==null || consigneeAdr.getText().toString().trim().equals("") || (consigneeAdr.getText().toString().trim().length() == 0)){
            Toast.makeText(EditeConsigneeAdrActivity.this , "请选择收货地址" , Toast.LENGTH_SHORT).show();
            return false;
        }

        if(consigneeAdrs.getText()==null || consigneeAdrs.getText().toString().trim().equals("") || (consigneeAdrs.getText().toString().trim().length() == 0)){
            Toast.makeText(EditeConsigneeAdrActivity.this , "请填写收货地址详细信息" , Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(phoneCode)){
            Toast.makeText(this , "请输入手机号码" , Toast.LENGTH_SHORT).show();
            return false;
        }

        String rule = "^1(3|5|7|8|4)\\d{9}" ;
        Pattern p = Pattern.compile(rule) ;
        Matcher m = p.matcher(phoneCode) ;

        if (!m.matches()){
            Toast.makeText(this , "手机号码格式错误" , Toast.LENGTH_SHORT).show();
            return false;
        }
        return true ;
    }

    protected void addListener() {

        cnToolbar.setRightButtonIcOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdite){
                    editeConsigneeAdr ();
                }else {
                    submitConsigneeAdr();
                }

            }
        });

        cnToolbar.setLeftButtonIcOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyMode();
                finish();
            }
        });

        consigneeAdr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getAddressData() ;
                    showAdrPickerView(province , city , county) ;
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void beforLayout() {

    }

    private void editeConsigneeAdr() {

        if(!getData(consigneePhone.getText().toString().trim())){
            return;
        }

        Map<String , String> params = new HashMap<String , String>() ;

        params.put("id" , id+"") ;
        params.put("consignee" , consigneeName.getText().toString().trim()) ;
        params.put("phone" , consigneePhone.getText().toString().trim()) ;
        params.put("addr" , consigneeAdr.getText().toString().trim()
                + " "
                + consigneeAdrs.getText().toString().trim()) ;
        params.put("zip_code" , edte_zip_code ) ;
        params.put("is_default" , true + "") ;

        requestAdrData(Contents.API.UPDATE_CONSIGNEE , params) ;

    }

    private void submitConsigneeAdr() {

        if(!getData(consigneePhone.getText().toString().trim())){
            return;
        }

        Map<String , String> params = new HashMap<String , String>() ;
        params.put("user_id" , MyApplication.getInstance().getUser().getId() + "") ;
        params.put("consignee" , consigneeName.getText().toString().trim()) ;
        params.put("phone" , consigneePhone.getText().toString().trim()) ;
        params.put("addr" , consigneeAdr.getText().toString().trim()
                +" "
                + consigneeAdrs.getText().toString().trim()) ;
        params.put("zip_code" , zip_code ) ;

        requestAdrData(Contents.API.CREATE_CONSIGNEE , params) ;

    }

    private void requestAdrData(String uri ,  Map<String , String> params) {

        okhtp.doPost( uri , new loadingSpotsDialog<SubmitOrderMsg>(this) {
            @Override
            public void onErroe(Response response, int responseCode, Exception e) throws IOException {
                this.closeSpotsDialog();
                closeKeyMode() ;
            }

            @Override
            public void callBackSucces(Response response, SubmitOrderMsg submitOrderMsg) throws IOException {
                this.closeSpotsDialog();
                closeKeyMode() ;

                if(submitOrderMsg.getStatus() == 1){

                    setResult(RESULT_OK);
                    finish();

                }else {
                    Toast.makeText(EditeConsigneeAdrActivity .this , "提交失败" + submitOrderMsg.getMessage() , Toast.LENGTH_SHORT).show();
                }
            }

        }, params);

    }

    private void closeKeyMode(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(consigneeName.getWindowToken(),0);
        imm.hideSoftInputFromWindow(consigneeAdrs.getWindowToken(),0);
        imm.hideSoftInputFromWindow(consigneePhone.getWindowToken(),0);
    }

    private void getAdrMsg(List<PrivinceModel> privinceModels){
        province = new ArrayList<String>() ;
        city = new ArrayList<List<String>>() ;
        county = new ArrayList<List<List<String>>>() ;
        zip_codes =  new ArrayList<List<List<String>>>() ;

        for (int i = 0; i < privinceModels.size() ; i++) {

            province.add( privinceModels.get(i).getName()) ;
            List<String> cityNames = new ArrayList<String>() ;
            List<List<String>> District = new ArrayList<List<String>>();
            List<List<String>> DistrictCode = new ArrayList<List<String>>();
            for (int j = 0; j <privinceModels.get(i).getCityModels().size() ; j++) {

                cityNames.add(privinceModels.get(i).getCityModels().get(j).getName());

                List<String> DistrictNames = new ArrayList<String>() ;
                List<String> DistrictNamesCode = new ArrayList<String>() ;

                for (int k = 0; k <privinceModels.get(i).getCityModels().get(j).getDistrictModels().size()  ; k++) {

                    DistrictNames.add(privinceModels.get(i).getCityModels().get(j).getDistrictModels().get(k).getName());
                    DistrictNamesCode.add(privinceModels.get(i).getCityModels().get(j).getDistrictModels().get(k).getZipcode());
                }
                District.add(DistrictNames);
                DistrictCode.add(DistrictNamesCode) ;
            }
            city.add(cityNames) ;
            county.add(District) ;
            zip_codes.add(DistrictCode) ;
        }


    }

    private void getAddressData() throws ParserConfigurationException, SAXException, IOException {

        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XmlParserHandler sfh = new XmlParserHandler();

        AssetManager am = this.getAssets();
        InputStream is = am.open("province_data.xml");

        sp.parse(is , sfh);
        getAdrMsg(sfh.getPrivinceModels()) ;
    }

    private void showAdrPickerView(final List<String> province,
                                   final List<List<String>> city,
                                   final List<List<List<String>>> county){

        OptionsPickerView  pvOptions = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                consigneeAdr.setText(province.get(options1) + city.get(options1).get(option2) + county.get(options1).get(option2).get(options3));
                zip_code = zip_codes.get(options1).get(option2).get(options3) ;
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("城市选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
//                .setLinkage(false)//设置是否联动，默认true
                .setLabels("", "", "")//设置选择的三级单位
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(true)//是否显示为对话框样式
                .build();

//        Log.d("----" , "--------------"+county.get(1).get(1).get(1)) ;

        pvOptions.setPicker(province, city  , county);//添加数据源
        pvOptions.show();

    }

}
