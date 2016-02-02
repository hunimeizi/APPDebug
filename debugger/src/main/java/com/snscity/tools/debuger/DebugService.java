package com.snscity.tools.debuger;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.snscity.common.debug.activity.DebugTabActivity;

import java.lang.reflect.Field;
import java.util.List;

public class DebugService extends Service
{

    //定义浮动窗口布局
    LinearLayout mFloatLayout;
    LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;

    ImageView mFloatView;

    private boolean isHide = true;
    @Override
    public void onCreate()
    {
        super.onCreate();
        if (isHide) {
            isHide = false;
            createFloatView();
            stopServiceIfOnBackground();
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    private void createFloatView()
    {
        wmParams = new LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        //设置window type
        wmParams.type = LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
//        wmParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        DisplayMetrics dm =getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        wmParams.x = SharedPreferencesManager.getInstance(getApplicationContext()).getInt("float_x",screenWidth);
        wmParams.y = SharedPreferencesManager.getInstance(getApplicationContext()).getInt("float_y",screenHeight/2);
        //设置悬浮窗口长宽数据
        wmParams.width = LayoutParams.WRAP_CONTENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;

		 /*// 设置悬浮窗口长宽数据
        wmParams.width = 200;
        wmParams.height = 80;*/

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.debug_float_layout, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        mFloatView = (ImageView)mFloatLayout.findViewById(R.id.float_id);
        mFloatView.setBackgroundResource(R.drawable.debug_bug);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //设置监听浮动窗口的触摸移动
        mFloatView.setOnTouchListener(new OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                // TODO Auto-generated method stub
                //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth()/2;
                //减25为状态栏的高度
                wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight()/2 - getStateBarHeight(DebugService.this);
                //刷新
                SharedPreferencesManager.getInstance(getApplicationContext()).putExtra("float_x",wmParams.x);
                SharedPreferencesManager.getInstance(getApplicationContext()).putExtra("float_y",wmParams.y);
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                return false;  //此处必须返回false，否则OnClickListener获取不到监听
            }
        });

        mFloatView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                stopSelf();
                return false;
            }
        });

        mFloatView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isHide) {
                    Intent intent = new Intent(DebugService.this, DebugTabActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(mFloatLayout != null)
        {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }
    }

    private void stopServiceIfOnBackground() {
        new Thread(){
            @Override
            public void run() {
                while(!isHide){
                    SystemClock.sleep(2000);
                    if(isBackground(getApplicationContext(),getPackageName())){
                        Log.e(getPackageName(),"后台运行");
                        isHide = true;
                        stopSelf();
                    }
                }
            }
        }.start();
    }


    public boolean isBackground(Context context,String packgeName) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    //Log.i(packgeName + "--前台--", appProcess.processName);
                    return false;
                } else {
                    //Log.i(packgeName + "--后台--", appProcess.processName);
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStateBarHeight(Context context) {

        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
            return sbar;
        } catch (Exception e1) {
            e1.printStackTrace();
            return 0;
        }
    }
}