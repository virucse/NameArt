package com.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;


public class MyImageButton extends AppCompatImageButton {

    public MyImageButton(Context context) {
        super(context);
        init();
    }

    public MyImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myButtonClickListener!=null){
                    myButtonClickListener.onClick(v,object);
                }
            }
        });
    }

    private Object object;
    public void setBtnObject(Object object){
        this.object=object;
    }
    public Object getBtnObject(){
        return this.object;
    }
    private OnMyButtonClickListener myButtonClickListener;
    public void setOnBtnClickListener(OnMyButtonClickListener listener){
        myButtonClickListener=listener;
    }
}
