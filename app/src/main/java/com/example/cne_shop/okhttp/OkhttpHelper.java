package com.example.cne_shop.okhttp ;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.example.cne_shop.application.MyApplication;
import com.example.cne_shop.myException.GET_RESPONSE_MESSAGE_FAILURE;
import com.example.cne_shop.myException.GSON_ANALYZE_MESSAGE_FAILURE;
import com.example.cne_shop.okhttp.BaseCallback;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by 博 on 2017/7/1.
 */

public class OkhttpHelper {

    //所有请求公用一个OkhttpClient
    private static OkHttpClient client ;
    private Gson gson ;
    private Handler handler ;
    private static final int TOKEN_ERROR = 401 ;
    private static final int TOKEN_EXPRISE = 402 ;
    private static final int TOKEN_MISSING = 403 ;


    private OkhttpHelper(){
        client = new OkHttpClient() ;
        client.setConnectTimeout(5 , TimeUnit.SECONDS);
        client.setWriteTimeout(5 , TimeUnit.SECONDS);
        client.setReadTimeout(5 , TimeUnit.SECONDS);

        gson = new Gson() ;

        handler = new Handler(Looper.getMainLooper()) ;

    }

    public static OkhttpHelper getOkhttpHelper(){
        return new OkhttpHelper() ;
    }

    //提交get请求
    public void doGet (String uri , BaseCallback callback ){
        doGet(uri , callback, null );
    }

    public void doGet ( String uri , BaseCallback callback ,  Map<String , String> formData ){
        Request request = buildRequest(uri , METHOD_TYPE.GET , formData ) ;
        doRequest(request,callback);
    }

    //提交post请求
    public void doPost (String uri , BaseCallback callback , Map<String , String> formData){
        Request request = buildRequest(uri , METHOD_TYPE.POST , formData) ;
        doRequest(request,callback);
    }

    //提交请求
    private void doRequest(Request request , final BaseCallback callback){

        //在进行提交之前进行
        callback.onRequestBefore();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //提交失败
                callbackFailure(callback , request , e); ;
                Log.e("Okhttp 提交请求失败" , "------>"+e) ;
            }

            @Override
            public void onResponse(Response response) throws IOException {

                if(response.isSuccessful()){
                    //提交成功，得到回返信息
                    String jqury = response.body().string() ;
                    Log.d("----" , "------------------------------------jqury:"+jqury) ;
                    if (callback.type == String.class){
                        callback.callBackSucces(response , jqury);
                    }else{
                        try {
                            Object obj = gson.fromJson(jqury , callback.type) ;
                            callbackSuccess(callback , response , obj);
                        }catch (Exception e){
                            try {
                                callbackError(callback , response , null);
                                throw new GSON_ANALYZE_MESSAGE_FAILURE("gson解析信息失败") ;
                            } catch (GSON_ANALYZE_MESSAGE_FAILURE gson_analyze_message_failure) {
                                Log.e("Okhttp 提交请求失败" , "------>"+e) ;
                            }
                        }
                    }

                }
                else if (response.code() == TOKEN_ERROR ||response.code() == TOKEN_EXPRISE ||response.code() == TOKEN_MISSING  ){
                    callbackTokenError(callback , response) ;
                }
                else {
                    callbackError(callback , response , null);

                }
            }
        });
    }

    private Request buildRequest (String uri , METHOD_TYPE method_type , Map<String , String> formData){
        Request.Builder builder = new Request.Builder();

        if(method_type == METHOD_TYPE.GET){

            uri = getUriWithParams(uri , formData);
            Log.d("----" , "------------------------------------uri:"+uri) ;
            builder.url(uri) ;
            builder.get() ;
        }else if(method_type == METHOD_TYPE.POST){
            Log.d("----" , "------------------------------------uri:"+uri) ;
            builder.url(uri) ;
            RequestBody requestBody = buildFormData(formData) ;
            builder.post(requestBody) ;
        }
        return builder.build() ;
    }

    private String getUriWithParams( String uri ,Map<String , String> formData){

        if (formData == null){
            formData = new HashMap<>(1);
        }

        String symbol = null ;
        int signNum = 0 ;

        String token = MyApplication.getInstance().getToken() ;
        if (!TextUtils.isEmpty(token)){
            formData.put("token" , MyApplication.getInstance().getToken());
        }

        for(String key : formData.keySet()){
            symbol = (signNum++ == 0 )? "?" : "&" ;
            uri = uri+symbol+key+"="+formData.get(key) ;
        }

        return uri ;

    }


    private RequestBody buildFormData (Map<String , String> formData){

        if(formData != null) {
            FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder() ;
            for (Map.Entry<String , String> objectMap : formData.entrySet()){
                formEncodingBuilder.add( objectMap.getKey() , objectMap.getValue() ) ;
            }

            String token = MyApplication.getInstance().getToken() ;

            Log.d("----","----------------token------"+token+"-----------------------");

            if (!TextUtils.isEmpty(token)){
                formEncodingBuilder.add("token" , token) ;
            }
            return formEncodingBuilder.build() ;
        }
        return null ;
    }

    private void callbackTokenError (final BaseCallback baseCallback , final Response response){
        handler.post(new Runnable() {
            @Override
            public void run() {
                baseCallback.onTokenError(response , response.code());
            }
        });
    }

    private void callbackSuccess (final BaseCallback baseCallback , final Response response , final Object object){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    baseCallback.callBackSucces(response , object);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void callbackError (final BaseCallback baseCallback , final Response response , final Object object){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    baseCallback.onErroe(response , response.code() , new GET_RESPONSE_MESSAGE_FAILURE("获取服务器信息失败"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void callbackFailure (final BaseCallback baseCallback , final Request request , final IOException e){
        handler.post(new Runnable() {
            @Override
            public void run() {
                baseCallback.onFailure(request , e);
            }
        });
    }

    //定义枚举类型
    enum METHOD_TYPE{
        GET,
        POST
    }
}
