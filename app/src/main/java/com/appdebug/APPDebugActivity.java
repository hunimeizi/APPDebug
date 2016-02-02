package com.appdebug;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.snscity.common.debug.activity.DebugMessageModel;
import com.snscity.common.debug.activity.RespInfo;
import com.snscity.tools.debuger.DebugService;

import java.util.Date;
import java.util.List;

public class APPDebugActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appdebug);
        //添加消息  放在网络请求内
        String createTime = new Date().toLocaleString();
        String paramsUrl = "params";
        RespInfo info = new RespInfo(createTime, "www.baidu.com", paramsUrl, "strResult", "strResult".length() + " B.");
        DebugMessageModel.messageList.add(info);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isServiceRunning(getApplicationContext(), DebugService.class.getName())) {
            startService(new Intent(getApplicationContext(), DebugService.class));
        }
    }

    /**
     * 检查后台的server是否运行，通过输入server name
     *
     * @param context
     * @param className
     * @return
     */
    public boolean isServiceRunning(Context context, String className) {

        boolean isRunning = false;

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
