package com.example.cne_shop.okhttp;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import dmax.dialog.SpotsDialog;

/**
 * Created by 博 on 2017/7/1.
 */

public abstract class loadingSpotsDialog<T> extends BaseCallback<T> {

    private SpotsDialog spotsDialog ;
    private Context mContext ;

    public loadingSpotsDialog(Context context){
        this.mContext = context ;
        spotsDialog = new SpotsDialog(context) ;
    }

    public void showSpotsDialog(){
        spotsDialog.show();
    }

    public void closeSpotsDialog(){
        spotsDialog.cancel();
    }

    @Override
    public void onRequestBefore() {
        showSpotsDialog() ;
    }

    @Override
    public void onFailure(Request request, IOException e) {
        closeSpotsDialog();
    }

    @Override
    public void onTokenError(Response response, int responseCode) {
    }
}
