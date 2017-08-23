package com.example.cne_shop.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cne_shop.R;

/**
 * Created by 博 on 2017/7/13.
 */

public class NumControlerView extends LinearLayout implements View.OnClickListener{

    private View view ;
    private Context context ;
    private LayoutInflater inflater ;
    private Button addButoon ;
    private Button subButton ;
    private TextView num ;

    private int value ;
    private int minValue ;
    private int maxValue ;
    private Drawable addButtonBackGround ;
    private Drawable subButtonBackGround ;
    private Drawable numTextBackGround ;
    private TypedArray typedArray ;

    private onNumChangedListener listener ;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public NumControlerView(Context context) {
        this(context , null) ;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public NumControlerView(Context context, @Nullable AttributeSet attrs) {
        this(context , attrs ,0) ;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public NumControlerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context ;
        inflater = LayoutInflater.from(context) ;
        typedArray = context.obtainStyledAttributes(attrs , R.styleable.NumControlerView) ;

        initView();
        typedArray.recycle();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initView(){

        view = inflater.inflate(R.layout.num_controller_layout , null , false) ;

        addButoon = (Button) view.findViewById(R.id.addButton) ;
        subButton = (Button) view.findViewById(R.id.subButton) ;
        num = (TextView) view.findViewById(R.id.num) ;

        getAttrValue() ;

        addButoon.setBackground(addButtonBackGround);
        subButton.setBackground(subButtonBackGround);
        num.setBackground(numTextBackGround);

        num.setText(value+"");

        addButoon.setOnClickListener(this);
        subButton.setOnClickListener(this);
        num.setOnClickListener(this);

        addView(view);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        num.setText(value+"");
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void getAttrValue(){

        maxValue = typedArray.getInt(R.styleable.NumControlerView_maxValue , 0) ;
        value = typedArray.getInt(R.styleable.NumControlerView_value , 0) ;
        minValue = typedArray.getInt(R.styleable.NumControlerView_minValue , 0) ;

        addButtonBackGround = typedArray.getDrawable(R.styleable.NumControlerView_addButtonBackground  ) ;
        subButtonBackGround = typedArray.getDrawable(R.styleable.NumControlerView_subButtonBackground ) ;
        numTextBackGround = typedArray.getDrawable(R.styleable.NumControlerView_numTextBackGround ) ;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addButton){

            if (addValue() && listener != null)
            listener.addValueListener(v , getValue());
        }else if (v.getId() == R.id.subButton){
            
            if (subValue() && listener != null)
            listener.subValueListener(v , getValue());
        }
    }

    private Boolean addValue(){
        if( num.getText()!=null && value != maxValue ) {
            value++ ;
            num.setText(value+"");
            return true ;
        }else {
            Toast.makeText( context ,"已达到允许的最大值" , Toast.LENGTH_SHORT ).show();
            return false ;
        }
    }

    private Boolean subValue(){
        if( num.getText()!=null && value != minValue ) {
            value-- ;
            num.setText(value+"");
            return true ;
        }else {
            Toast.makeText( context ,"已达到允许的最小值" , Toast.LENGTH_SHORT ).show();
            return false ;
        }
    }

    public interface onNumChangedListener{
        void addValueListener(View v, int value) ;
        void subValueListener(View v, int value) ;
    }

    public void setValueChangeListener(onNumChangedListener listener){
        this.listener = listener ;
    }

}
