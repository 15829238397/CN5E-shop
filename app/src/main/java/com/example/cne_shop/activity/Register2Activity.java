package com.example.cne_shop.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cne_shop.R;
import com.example.cne_shop.base.BaseActivity;
import com.example.cne_shop.bean.User;
import com.example.cne_shop.bean.msg.LoginRespMsg;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by 博 on 2017/7/24.
 */

public class Register2Activity extends BaseActivity {

    @BindView(R.id.phone)
    TextView phone ;
    @BindView(R.id.toolBar)
    CnToolbar cnToolbar ;
    @BindView(R.id.identifyCode)
    MyEditText identifyCode ;
    @BindView(R.id.reRequset)
    Button getIdentifyButton ;
    private int CountDownTime = 60 ;
    private Timer timer ;
    private Handler mHandler ;
    private SMSEvenHandler smsEvenHandler ;
    private String countryCode ;
    private String phoneNum ;
    private String passWord ;
    private String showPhoneNum ;
    private static OkhttpHelper okhttpHelper;
    private Dialog dialog ;

    private static final int HANDLER_CODE = 0X123 ;
    private static final int COUNTDOWN_MAX_VALUE = 60 ;

    @Override
    protected int getContentViewId() {
        return R.layout.mine_register2_activity;
    }

    @Override
    protected void initView() {

        okhttpHelper = OkhttpHelper.getOkhttpHelper() ;
        dialog = new Dialog(this) ;

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if( HANDLER_CODE == msg.what ){
                    if(CountDownTime != 0) {
                        CountDownTime-- ;
                        getIdentifyButton.setText(CountDownTime + "秒后可重新发送");
                    }else{
                        getIdentifyButton.setText("重新发送");
                        stopCountDown() ;
                    }
                }
            }
        };

        smsEvenHandler = new SMSEvenHandler() ;
        SMSSDK.registerEventHandler(smsEvenHandler);

        Intent intent = getIntent() ;
        countryCode = intent.getStringExtra(RegisterActivity.COUNTRY_CODE) ;
        passWord = intent.getStringExtra(RegisterActivity.PASSWORD) ;
        phoneNum = intent.getStringExtra(RegisterActivity.PHONE) ;
        showPhoneNum = cutApartPhoneNum(phoneNum) ;
        phone.setText(showPhoneNum);

        beginCountDown() ;
    }
    private void beginCountDown(){
        CountDownTime = COUNTDOWN_MAX_VALUE ;
        timer = new Timer() ;
        getIdentifyButton.setClickable(false);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(HANDLER_CODE) ;
            }
        },0 ,1000);
    }

    private void stopCountDown(){
        timer.cancel();
        getIdentifyButton.setClickable(true);
    }

    private String cutApartPhoneNum(String phone){
        if(null == phone){
            return " " ;
        }
        return phone.substring(0 , 3) + " " + phone.substring(3 ,6) + " " + phone.substring(6) ;

    }

    protected void addListener(){

        cnToolbar.setLeftButtonIcOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getIdentifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reRequestIdentifyCode() ;
                beginCountDown();
            }
        });

        cnToolbar.setRightButtonIcOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finishReg() ;
            }
        });
    }

    @Override
    protected void beforLayout() {

    }

    private void finishReg(){

        if(null == getCode()){
            Toast.makeText(this , "请填写验证码" , Toast.LENGTH_SHORT).show();
            return;
        }

        SMSSDK.submitVerificationCode(countryCode , phoneNum , getCode()) ;
    }

    private String getCode(){
        return identifyCode.getText().toString().trim() ;
    }

    private void reRequestIdentifyCode(){
        SMSSDK.getVerificationCode(countryCode , phoneNum);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(smsEvenHandler);
    }

    class SMSEvenHandler extends EventHandler {
        @Override
        public void afterEvent(final int event, final int result, final Object data) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (data instanceof Throwable) {
                        Throwable throwable = (Throwable)data;
                        String msg = throwable.getMessage();
                        Toast.makeText(Register2Activity.this, msg, Toast.LENGTH_SHORT).show();
                    }else {
                        if (result == SMSSDK.RESULT_COMPLETE){
                            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                                afterVerificationCodeSubmit() ;
                                dialog.setTitle("正在提交信息");
                                dialog.show();

                            }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                                afterVerificationCodeRequested((Boolean) data);
                            }
                        }}
                }
            });
        }
    }
    private void afterVerificationCodeRequested(boolean smart) {
        if(smart){
            Toast.makeText(this , "获取验证码成功" , Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this , "获取验证码失败" , Toast.LENGTH_SHORT).show();
        }

    }

    private void afterVerificationCodeSubmit() {

        String uri = Contents.API.REGISTER ;
        Map< String , String > params = new HashMap<String, String>() ;
        params.put("phone" , phoneNum ) ;
        params.put("password" , DESUtil.encode(Contents.DES_KEY , passWord)) ;

        okhttpHelper.doPost(uri, new loadingSpotsDialog<LoginRespMsg<User>>(Register2Activity.this ) {
            @Override
            public void onErroe(Response response, int responseCode, Exception e) throws IOException {
                this.closeSpotsDialog();
                if(dialog!=null &&dialog.isShowing()){
                    dialog.dismiss();
                }
            }

            @Override
            public void callBackSucces(Response response, LoginRespMsg<User> userLoginRespMsg) throws IOException {
                this.closeSpotsDialog();

                if(dialog!=null &&dialog.isShowing()){
                    dialog.dismiss();
                }

                if(userLoginRespMsg.getStatus() == 1){
                    closeKeyMode();
                    Toast.makeText(Register2Activity.this, "模拟注册成功" , Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Register2Activity.this , ShopMainActivity.class) ;
                    startActivity(intent);
                    finish();

                }else {
                    showErrorMsg() ;
                }
            }
        }, params);
    }


    private void closeKeyMode(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(identifyCode.getWindowToken(),0);
    }

    private void showErrorMsg(){
        closeKeyMode();
        Toast.makeText(this, "注册失败" , Toast.LENGTH_LONG).show();
    }
}
