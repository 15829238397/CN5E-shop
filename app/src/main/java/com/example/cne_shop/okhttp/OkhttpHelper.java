package com.example.cne_shop.okhttp;

import android.app.Service;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.example.cne_shop.myException.GET_RESPONSE_MESSAGE_FAILURE;
import com.example.cne_shop.myException.GSON_ANALYZE_MESSAGE_FAILURE;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by 博 on 2017/8/22.
 */

public class OkhttpHelper {

    //所有的请求公用一个OkhttpClient
    private static OkHttpClient client ;
    private Handler handler ;
    private Gson gson ;

    private static final int TOKEN_ERROR = 401 ;
    private static final int TOKEN_EXPRISE = 402 ;
    private static final int TOKEN_MISSING = 403 ;

    private OkhttpHelper(){

        //初始化client信息
        client = new OkHttpClient() ;
        client.setConnectTimeout(5 , TimeUnit.SECONDS);
        client.setWriteTimeout(5 , TimeUnit.SECONDS);
        client.setReadTimeout(5 , TimeUnit.SECONDS);

        gson = new Gson() ;
    }

    //提供一个静态方法供外部请求。
    public static OkhttpHelper getOkhttpHelper(){
        return new OkhttpHelper() ;
    }

    //提交get请求的方法。
    public void doGet (String uri , BaseCallback callback){
        doGet(uri , callback , null);
    }

    //带参数请求的方法
    public void doGet (String uri , BaseCallback callback , Map<String , String> formData){
        Request request = buildRequest(uri , METHOD_TYPE.GET , formData);
        doRequest(request , callback);
    }

    public void doPost(String uri , BaseCallback callback , Map<String , String> formData){
        Request request = buildRequest(uri , METHOD_TYPE.POST , formData);
        doRequest(request , callback);
    }

    private void doRequest(final Request request , final BaseCallback callback){

        callback.onRequestBefore();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //提交失败
                callbackFailure(callback , request ,e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()){
                    String jqury = response.body().string() ;

                    if (callback.type == String.class){
                        callback.callBackSucces(response , jqury);
                    }else {
                        try{
                            Object object = gson.fromJson(jqury , callback.type) ;
                            callbackSuccess(callback , response , jqury);
                        }catch (Exception e){
                            callbackError(callback , response , null);
                            throw new GSON_ANALYZE_MESSAGE_FAILURE("gson解析信息失败") ;
                        }
                    }
                }else{
                    callbackError(callback , response , null);
                }

            }
        });


    }

    private Request buildRequest (String uri , METHOD_TYPE method_type , Map<String , String> formData){

        Request.Builder builder = new Request.Builder() ;
        if(method_type == METHOD_TYPE.GET){

            uri = getUriWithParams(uri , formData) ;
            builder.url(uri) ;
            builder.get() ;

        }else if (method_type == METHOD_TYPE.POST){
            builder.url(uri) ;
            RequestBody requestBody = buildFormData(formData) ;
            builder.post(requestBody) ;
        }
        return builder.build() ;
    }

    private RequestBody buildFormData (Map<String , String> formData){

        if(formData != null) {
            FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder() ;
            for (Map.Entry<String , String> objectMap : formData.entrySet()){
                formEncodingBuilder.add( objectMap.getKey() , objectMap.getValue() ) ;
            }

            return formEncodingBuilder.build() ;
        }
        return null ;
    }

    //对参数进行处理
    private String getUriWithParams(String uri , Map<String , String > formData){
        String symbol = null ;
        int signNum = 0 ;

       if(formData == null){
           return uri ;
       }

        for(String key : formData.keySet()){
            symbol = (signNum++ == 0 )? "?" : "&" ;
            uri = uri+symbol+key+"="+formData.get(key) ;
        }
        return uri ;
    }

    /**
     * 以下方法保证对于okhttp的拦截处理运行在主线程
     *
     */
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



    enum METHOD_TYPE {
        GET,
        POST
    }

    private void demo(){

        OkHttpClient client = new OkHttpClient() ;
        client.setConnectTimeout(5 , TimeUnit.SECONDS);
        client.setWriteTimeout(5 , TimeUnit.SECONDS);
        client.setReadTimeout(5 , TimeUnit.SECONDS);

        String url = "http://www.jianshu.com/p/6ffde18fb034" ;
        //盛放参数
        RequestBody requestBody = new FormEncodingBuilder()
                .add("name","xxx").build() ;

        Request request = new Request.Builder().url(url).post(requestBody).build() ;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

}
