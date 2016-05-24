package fan.thisweather.utils;

import com.google.gson.Gson;

import fan.thisweather.bean.ObtainWeatherBean;

/**
 * Created by fan on 2016/5/17.
 */
public class ParseJson {

    public static ObtainWeatherBean stringToBean(String json){

        Gson gson = new Gson();
        /**
         * 1.要解析的json字符串
         * 2.按照哪个JavaBean解析
         */
        return gson.fromJson(json,ObtainWeatherBean.class);

    }

}
