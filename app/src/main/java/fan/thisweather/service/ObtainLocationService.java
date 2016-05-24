package fan.thisweather.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by fan on 2016/5/16.
 */
public class ObtainLocationService extends Service {

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private Intent intent = new Intent("fan.location");

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initLocation();
        mLocationClient.start();

    }

    private void initLocation(){
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        public void onReceiveLocation(BDLocation location) {

            boolean isSucc = true;

            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果

            } else if (location.getLocType() == BDLocation.TypeServerError) {
                isSucc = false;
                Toast.makeText(ObtainLocationService.this, "定位失败!请检查网络是否通畅！", Toast.LENGTH_SHORT).show();
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                isSucc = false;
                Toast.makeText(ObtainLocationService.this, "定位失败!请检查网络是否通畅！", Toast.LENGTH_SHORT).show();
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                isSucc = false;
                Toast.makeText(ObtainLocationService.this, "定位失败!请检查网络是否通畅！", Toast.LENGTH_SHORT).show();
            }

            //获取定位的城市
            String mNowLocation = location.getCity();
            //获取定位的具体城区
            String mDistrict = location.getDistrict();
            mLocationClient.stop();

            if(isSucc){
                intent.putExtra("location", mNowLocation);
                intent.putExtra("locationDistrict", mDistrict);
                sendBroadcast(intent);
            }

        }
    }

    @Override
    public void onDestroy() {
        mLocationClient.stop();
        super.onDestroy();
    }
}
