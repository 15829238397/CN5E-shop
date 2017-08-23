package com.example.cne_shop.utils;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.example.cne_shop.bean.ShoppingCart;
import com.example.cne_shop.bean.Ware;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 博 on 2017/7/14.
 */

public class CartProvider {

    private SparseArray<ShoppingCart> datas = null ;
    private Context context = null ;
    private static CartProvider cartProvider ;

    private CartProvider(Context context) {

            this.context = context ;
            this.datas = new SparseArray<>(10) ;
            listToSparseArray() ;

    }

    public static CartProvider getCartProvider(Context context){
        if(cartProvider == null){
            cartProvider = new CartProvider(context) ;
        }
        return cartProvider ;
    }

    public void put (ShoppingCart cart){

        ShoppingCart temp = datas.get((int) cart.getId()) ;
        Log.d("------ ", " "+cart.getId()) ;

        if (temp != null){
            temp.setCount(temp.getCount() + 1);
        }else {
            temp = cart ;
        }

        datas.put((int) cart.getId(), temp);
        conmmit() ;

    }

    public void put (Ware ware){
        ShoppingCart shoppingCart = ware.toShoppinfCart() ;
        this.put(shoppingCart);
    }


    public void update (ShoppingCart cart){
        datas.put((int) cart.getId(), cart);
        conmmit() ;
    }

    public void delete (ShoppingCart cart){
        datas.delete((int) cart.getId());
        conmmit() ;
    }

    public List<ShoppingCart> getAll(){

        return getDataFromlocal() ;
    }

    private List<ShoppingCart>  sparseArrayToList ( ){

        int i = 0 ;
        List<ShoppingCart> carts = new ArrayList<ShoppingCart>();

        for ( i = 0 ; i < datas.size() ; i++){

            carts.add(datas.valueAt(i)) ;
        }

        return carts ;
    }

    private void listToSparseArray(){

        List<ShoppingCart> carts = getDataFromlocal() ;
        if ( carts != null && carts.size() > 0 ){
            for (ShoppingCart cart : carts){
                datas.put((int) cart.getId(), cart);
            }

        }

    }

    private void conmmit(){

        List<ShoppingCart> carts = sparseArrayToList ();

        PreferenceUtil.putString(context ,PreferenceUtil.CAINIAO_SHOPPING , JsonUtil.toJSON(carts));

    }

    public List<ShoppingCart> getDataFromlocal(){

        String json = PreferenceUtil.getString(context ,PreferenceUtil.CAINIAO_SHOPPING , null ) ;
        Log.d("----" , "---"+json) ;
        List<ShoppingCart> carts = new ArrayList<>() ;
        if (json != null && json.length() > 0 ){
            carts = JsonUtil.fromJson(json , new TypeToken<List<ShoppingCart>>(){}.getType()) ;
        }

        return carts ;
    }

}
