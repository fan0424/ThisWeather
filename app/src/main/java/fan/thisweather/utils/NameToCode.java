package fan.thisweather.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fan.thisweather.R;

/**
 * 通过城市名字获取城市编码
 * Created by fan on 2016/5/17.
 */
public class NameToCode {

    private static final int BUFFER_SIZE = 1024;
    public static final String DB_NAME = "weather.db";
    public static final String DB_PATH = Environment.getExternalStorageDirectory().getPath()+"/weather/system/";
    public static final String DB_FILE = DB_PATH + DB_NAME;

    //查询城区
//    public static final String SearchDistrict = "select * from weathers where area_name = ? ";
//    //查询城市
//    public static final String SearchCity = "select * from weathers where city_name = ? ";
    //查询语句
//    public static final String SearchCityCode = "select * from weathers where area_name like '%?%'";

    /**
     * 城市名字转城市编码
     * @return
     */
    public static String cityNameToCityCode(Context context,String mCity, String mDistrict){

        SQLiteDatabase db = openDateBase(context);
        String CityId = null;
        String DistrictId = null;
        Cursor cursor = db.rawQuery("select * from weathers where area_name like '%"+mDistrict +"%'", null);

        //如果获取不到数据
        if(cursor.getCount() == 0){

            Cursor cityCursor = db.rawQuery("select * from weathers where area_name like '%"+mCity +"%'", null);

            if(cityCursor.getCount() > 0){

                if(cityCursor.moveToNext()){
                    CityId = cityCursor.getString(cityCursor.getColumnIndex("weather_id"));
                }

            }
            cityCursor.close();

        }else{

            if(cursor.moveToNext()){
                DistrictId = cursor.getString(cursor.getColumnIndex("weather_id"));
            }
        }
        cursor.close();

        if(DistrictId == null){
            return CityId;
        }

        return DistrictId;

    }


    /**
     * 查找城市名字
     * @return
     */
    public static List SerchCity(Context context,String mCity){

        SQLiteDatabase db = openDateBase(context);
        List<String> CityNames = new ArrayList();

        Cursor cursor = db.rawQuery("select * from weathers where area_name like '%"+mCity +"%'", null);

        //如果获取不到数据
        while(cursor.moveToNext()){

            CityNames.add(cursor.getString(cursor.getColumnIndex("area_name")));

        }
        cursor.close();

        return CityNames;

    }


    /**
     * 打开数据库
     * @return
     */
    public static SQLiteDatabase openDateBase(Context context)
    {
        File file = new File(DB_FILE);
        // 如果文件不存在,将 raw 下的db文件复制到 data/data下面
        if (!file.exists())
        {
            File filePath = new File(DB_PATH);
            if(!file.exists())
                filePath.mkdirs();

            // // 打开raw中得数据库文件，获得stream流
            InputStream stream = context.getResources().openRawResource(R.raw.weather);
            try
            {
                // 将获取到的stream 流写入道data中
                FileOutputStream outputStream = new FileOutputStream(DB_FILE);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = stream.read(buffer)) > 0)
                {
                    outputStream.write(buffer, 0, count);
                }
                outputStream.close();
                stream.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DB_FILE,null);
        return database;
    }

}
