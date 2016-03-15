package com.wbja.stone.ydt.window;

import com.wbja.stone.ydt.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
/**
* 自定义dialog
* @author Mr.Xu
*
*/
public class DialogKx extends Dialog {
        //定义回调事件，用于dialog的点击事件
        public interface OnCustomDialogListener{
                public void back(String name);
        }
       
        private String name;
    	private CheckBox zhuzheng;
    	private RadioButton pfQ,pfZ,pfZH,yzQ,yzZ,yzZH;
    	private  RadioGroup group1;
    	private  RadioGroup group2;
        private OnCustomDialogListener customDialogListener;

        public DialogKx(Context context,String name,OnCustomDialogListener customDialogListener) {
                super(context);
                this.name = name;
                this.customDialogListener = customDialogListener;
        }
       
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.dialog_zhuzheng);
                //设置标题
                setTitle(name);
                pfQ=(RadioButton)findViewById(R.id.pfQ);
                pfZ=(RadioButton)findViewById(R.id.pfZ);
                pfZH=(RadioButton)findViewById(R.id.pfZH);
                yzQ=(RadioButton)findViewById(R.id.yzQ);
                yzZ=(RadioButton)findViewById(R.id.yzZ);
                yzZH=(RadioButton)findViewById(R.id.yzZH);
                group1 = (RadioGroup)this.findViewById(R.id.r1);
                group2 = (RadioGroup)this.findViewById(R.id.r2);
                
        		zhuzheng=(CheckBox)findViewById(R.id.zhuzheng);
        		
                Button clickBtn = (Button) findViewById(R.id.clickbtn);
                clickBtn.setOnClickListener(clickListener);
                Button btcancle = (Button) findViewById(R.id.btcancle);
                btcancle.setOnClickListener(clickListener);

        }
       
        private View.OnClickListener clickListener = new View.OnClickListener() {
               
                @Override
                public void onClick(View v) {
                	switch(v.getId()){
                	
                	case R.id.clickbtn:
                		StringBuilder strB = new StringBuilder();
                		if(zhuzheng.isChecked()){
                			strB.append("\n是否主症：是");
                		}else{
                			strB.append("\n是否主症：否");
                		}
                		RadioButton ra=(RadioButton)findViewById(group1.getCheckedRadioButtonId());
                		RadioButton rb=(RadioButton)findViewById(group2.getCheckedRadioButtonId());
               		strB.append("\n频繁程度：" +ra.getText());
        				strB.append("\n严重程度：" + rb.getText());
                		customDialogListener.back(strB.toString());
                         DialogKx.this.dismiss();
                		break;
                	case R.id.btcancle:
                		DialogKx.this.dismiss();
                		break;
                		default:
                			break;
                	}
                       
                }
        };

}