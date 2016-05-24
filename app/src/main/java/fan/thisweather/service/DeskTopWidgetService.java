package fan.thisweather.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.util.Timer;

import fan.thisweather.R;
import fan.thisweather.provider.MyWidgetReceiver;
import fan.thisweather.utils.DateAndTimeUtils;

/**
 * Created by fan on 2016/5/20.
 */
public class DeskTopWidgetService extends Service {

    private Timer timer;
    private AppWidgetManager awm;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        awm = AppWidgetManager.getInstance(this);

        refreshInfo();

        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver,filter);

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {

                refreshInfo();

             }
        }
    };

    /**
     * 更新信息
     */
    private void refreshInfo(){

        //ComponentName 就是对四大组件的包装w
        ComponentName provider = new ComponentName(DeskTopWidgetService.this, MyWidgetReceiver.class);

        //RemoteViews 远端的view
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.service_widget);


        //设置桌面控件的文字，动作由桌面launcher软件来干
        views.setTextViewText(R.id.tv_weiget_date, DateAndTimeUtils.getCurrentDate());
        views.setTextViewText(R.id.tv_weiget_time, DateAndTimeUtils.getCurrentTime());
        //更新桌面小控件
        //参数一：更新谁，参数二：更新成什么样子
        awm.updateAppWidget(provider, views);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
    }


}
