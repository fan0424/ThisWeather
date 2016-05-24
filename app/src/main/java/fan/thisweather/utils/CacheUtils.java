package fan.thisweather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fan on 2016/5/19.
 */
public class CacheUtils {

    /**
     * 获取json缓存数据
     * @param context
     * @param key 请求url地址
     * @return
     */
    public static String readCache(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("cache", context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    /**
     * 保存json缓存数据
     * @param context
     * @param key	接口的url
     * @param value	json字符串
     */
    public static void saveCache(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("cache", context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    /**
     * 保存已存城市名字
     * @param context
     * @param newCityName 要存入的名字
     */
    public static void saveCityName(Context context, String newCityName) {
        SharedPreferences sp = context.getSharedPreferences("cache", context.MODE_PRIVATE);
        String cityNames = sp.getString("cityName", "");
        String newCityNames = "";
        if(TextUtils.isEmpty(cityNames)){
            //如果没有存储，直接存储当前newCityName
            newCityNames = newCityName;
        }else{
            //如果已有内容，需要拼接
            newCityNames = cityNames+"#"+newCityName;
        }

        sp.edit().putString("cityName", newCityNames).commit();
    }


    /**
     * 删除城市
     * @param context
     * @param name
     */
    public static void delectCityName(Context context,String name){
        SharedPreferences sp = context.getSharedPreferences("cache", context.MODE_PRIVATE);
        String newCity = "";
        String cityNameStr = sp.getString("cityName", "");
        name = "#"+name;
        String[] cityNameArr = cityNameStr.split(name);

        for (int i = 0; i < cityNameArr.length; i++) {
            newCity += cityNameArr[i];
        }

        sp.edit().putString("cityName", newCity).commit();

    }

    /**
     * 获取已存城市
     * @param context
     * @return
     */
    public static List<String> getCityNames(Context context) {
        SharedPreferences sp = context.getSharedPreferences("cache", context.MODE_PRIVATE);
        List<String> CityNames = new ArrayList<String>();
        String cityNameStr = sp.getString("cityName", "");
        String[] cityNameArr = cityNameStr.split("#");
        for ( int i = 0 ; i < cityNameArr.length ; i ++ ) {
            CityNames.add(cityNameArr[i]);
        }
        return CityNames;
    }

    /**
     * 保存当前位置名字
     * @param context
     * @param key
     * @param value
     */
    public static void saveLocationName(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("location", context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }


    /**
     * 获取当前位置名字
     * @param context
     * @param key
     */
    public static String getLocationName(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("location", context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

}
