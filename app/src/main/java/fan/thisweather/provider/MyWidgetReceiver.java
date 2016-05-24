package fan.thisweather.provider;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import fan.thisweather.service.DeskTopWidgetService;

/**
 * Created by fan on 2016/5/20.
 */
public class MyWidgetReceiver extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

    }

    /**
     * 添加第一个小控件时调用
     */
    @Override
    public void onEnabled(final Context context) {
        super.onEnabled(context);
        Intent intent = new Intent(context,DeskTopWidgetService.class);
        context.startService(intent);

    }

    /**
     * 添加第二或之后的调用
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    /**
     * 删除两个或两个以上时调用
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

    }

    /**
     * 删除全部时调用
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Intent intent = new Intent(context,DeskTopWidgetService.class);
        context.stopService(intent);

    }

}
