package fan.thisweather.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

/**
 * 截图
 * Created by fan on 2016/5/18.
 */
public class ScreenShot {

    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b = view.getDrawingCache();
        return b;
    }

}
