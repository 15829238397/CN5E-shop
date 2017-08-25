package com.example.cne_shop.activity;

import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cne_shop.R;
import com.example.cne_shop.application.MyApplication;
import com.example.cne_shop.base.BaseActivity;
import com.example.cne_shop.bean.msg.LoginRespMsg;
import com.example.cne_shop.bean.User;
import com.example.cne_shop.contents.Contents;
import com.example.cne_shop.okhttp.OkhttpHelper;
import com.example.cne_shop.okhttp.loadingSpotsDialog;
import com.example.cne_shop.utils.DESUtil;
import com.example.cne_shop.widget.CnToolbar;
import com.example.cne_shop.widget.MyEditText;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by 博 on 2017/8/25.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.toolBar)
    CnToolbar cnToolbar ;
    @BindView(R.id.button_log)
    Button login_button ;
    @BindView(R.id.forgetPass)
    TextView forget_button ;
    @BindView(R.id.register)
    TextView register_button ;
    @BindView(R.id.userId)
    MyEditText phone ;
    @BindView(R.id.password)
    MyEditText password ;
    OkhttpHelper okhttpHelper;

    @Override
    protected int getContentViewId() {
        return R.layout.mine_login_activity;
    }

    @Override
    protected void initView() {
        phone.setInputType(InputType.TYPE_CLASS_NUMBER); //输入类型
        phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)}); //最大输入长度
        password.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
        okhttpHelper = OkhttpHelper.getOkhttpHelper() ;

        initPbtoolbar() ;
        Text() ;
    }

    @Override
    protected void addListener() {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin(v) ;
            }
        });

        forget_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("----" , "register------------------------------") ;
                LoginActivity.this.startActivity(new Intent(LoginActivity.this , RegisterActivity.class));
            }
        });
    }

    @Override
    protected void beforLayout() {

    }

    private void closeKeyMode(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(password.getWindowToken(),0);
    }


    private void initPbtoolbar(){

        cnToolbar.setLeftButtonIcOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyMode();
                finish();
            }
        });
    }

    private void doLogin( View v ){

        String phoneNum = phone.getText().toString() ;

        if ( phoneNum == null ){
            Toast.makeText( v.getContext() , "请输入手机号码" , Toast.LENGTH_SHORT).show();
            return;
        }else if(phoneNum.length() != 11){
            Toast.makeText( v.getContext() , "请输入正确的手机号码" , Toast.LENGTH_SHORT).show();
            return;
        }

        String pwd = password.getText().toString() ;
        if (pwd == null){
            Toast.makeText( v.getContext() , "请输入密码" , Toast.LENGTH_SHORT).show() ;
            return;
        }

        String uri = Contents.API.LOGIN ;
        Map< String , String > params = new HashMap<String, String>() ;
        params.put("phone" , phoneNum ) ;
        params.put("password" , DESUtil.encode(Contents.DES_KEY , pwd)) ;

        okhttpHelper.doPost(uri, new loadingSpotsDialog<LoginRespMsg<User>>(LoginActivity.this ) {
            @Override
            public void onErroe(Response response, int responseCode, Exception e) throws IOException {
                this.closeSpotsDialog();
            }

            @Override
            public void callBackSucces(Response response, LoginRespMsg<User> userLoginRespMsg) throws IOException {
                this.closeSpotsDialog();

                if(userLoginRespMsg.getStatus() == 1){

                    MyApplication.getInstance().putUser(userLoginRespMsg.getData() , userLoginRespMsg.getTocken());
                    closeKeyMode() ;

                    if (null == MyApplication.getInstance().getIntent()){
                        setResult(RESULT_OK);
                        finish();
                    }else {
                        MyApplication.jumpToTargetoActivity(LoginActivity.this);
                        finish();
                    }

                }else {
                    showLoginErrorMsg() ;
                    phone.setText("");
                    password.setText("");
                }
            }
        }, params);
    }

    public void Text(){
        phone.setText("15829238397");
        password.setText("bb001314");
    }

    private void showLoginErrorMsg(){
        closeKeyMode();
        Toast.makeText(this, "密码错误" , Toast.LENGTH_LONG).show();
    }


}
