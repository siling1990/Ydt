package com.wbja.stone.ydt;

import io.rong.imkit.RongIM;
import io.rong.imlib.ipc.RongExceptionHandler;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.iflytek.cloud.SpeechUtility;
import com.wbja.stone.ydt.message.ContactNotificationMessageProvider;
import com.wbja.stone.ydt.message.DeAgreedFriendRequestMessage;
import com.wbja.stone.ydt.provider.MyConversationListBehaviorListener;

public class App extends Application {

	@Override
	public void onCreate() {
		// 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
		// 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
		// 注意：此接口在非主进程调用会返回null对象，如�?��非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
		// 参数间使用半角�?,”分隔�?
		// 设置你申请的应用appid,请勿�?='与appid之间添加空格及空转义�?
		SpeechUtility.createUtility(App.this, "appid=" + getString(R.string.app_id));
		// 以下语句用于设置日志�?��（默认开启），设置成false时关闭语音云SDK日志打印
		// Setting.showLogcat(false);
		
		 /**
         * 注意：
         *
         * IMKit SDK调用第一步 初始化
         *
         * context上下文
         *
         * 只有两个进程需要初始化，主进程和 push 进程
         */
        if(getPackageName().equals(getCurProcessName(getApplicationContext())) ||
           "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            RongIM.init(this);

            /**
             * 融云SDK事件监听处理
             *
             * 注册相关代码，只需要在主进程里做。
             */
            if (getPackageName().equals(getCurProcessName(getApplicationContext()))) {

               RongCloudEvent.init(this);
               // DemoContext.init(this);
                Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));
                try {
                    RongIM.registerMessageType(DeAgreedFriendRequestMessage.class);
                    RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
                  //  RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
		super.onCreate();
	}
	public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
