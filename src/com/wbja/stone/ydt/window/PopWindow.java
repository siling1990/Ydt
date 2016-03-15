package com.wbja.stone.ydt.window;

import com.wbja.stone.ydt.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

public class PopWindow extends PopupWindow {  
    private View conentView;  
  
    public PopWindow( Activity context) {  
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        conentView = inflater.inflate(R.layout.popu_layout, null);  
        int h = context.getWindowManager().getDefaultDisplay().getHeight();  
        int w = context.getWindowManager().getDefaultDisplay().getWidth();  
        // ����SelectPicPopupWindow��View 
        this.setContentView(conentView);  
        // ����SelectPicPopupWindow��������Ŀ�  
        this.setWidth(w / 3);  
        // ����SelectPicPopupWindow��������ĸ�  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        // ����SelectPicPopupWindow��������ɵ��  
        this.setFocusable(true);  
        this.setOutsideTouchable(true);  
        // ˢ��״̬  
        this.update();  
        // ʵ����һ��ColorDrawable��ɫΪ��͸��  
        ColorDrawable dw = new ColorDrawable(0000000000);  
        // ��back���������ط�ʹ����ʧ,������������ܴ���OnDismisslistener �����������ؼ��仯�Ȳ���  
        this.setBackgroundDrawable(dw);  
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);  
        // ����SelectPicPopupWindow�������嶯��Ч��  
        this.setAnimationStyle(R.style.AnimationPreview);  
        LinearLayout addTaskLayout = (LinearLayout) conentView  
                .findViewById(R.id.setPatient);  
        LinearLayout teamMemberLayout = (LinearLayout) conentView  
                .findViewById(R.id.deleteFile);  
        addTaskLayout.setOnClickListener(new OnClickListener() {  
  
            @Override  
            public void onClick(View arg0) {
            	PopWindow.this.dismiss();  
            }  
        });  
  
        teamMemberLayout.setOnClickListener(new OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
            	PopWindow.this.dismiss();  
            }  
        });  
    }  
  
    /** 
     * ��ʾpopupWindow 
     *  
     * @param parent 
     */  
    public void showPopupWindow(View parent) {  
        if (!this.isShowing()) {  
            // ��������ʽ��ʾpopupwindow  
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);  
        } else {  
            this.dismiss();  
        }  
    }  
}  