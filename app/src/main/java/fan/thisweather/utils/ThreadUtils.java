package fan.thisweather.utils;

import android.os.Handler;

/**
 * Created by fan on 2016/2/18.
 */
public class ThreadUtils {
    /**
     * 子线程操作
     * @param r
     */
    public static void runOnBackThread(Runnable r){
        new Thread(r).start();
    }

    public static Handler handler = new Handler();

    /**
     * 主线程操作
     * @param r
     */
    public static void runOnUIThread(Runnable r){
        handler.post(r);
    }
}
