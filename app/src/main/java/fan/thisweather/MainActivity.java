package fan.thisweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fan.thisweather.adapter.MenuAdapter;
import fan.thisweather.adapter.ShowWeatherInfoAdapter;
import fan.thisweather.bean.CityBean;
import fan.thisweather.bean.ObtainWeatherBean;
import fan.thisweather.service.ObtainLocationService;
import fan.thisweather.ui.AddCity;
import fan.thisweather.ui.BaseActivity;
import fan.thisweather.utils.CacheUtils;
import fan.thisweather.utils.NameToCode;
import fan.thisweather.utils.ParseJson;
import fan.thisweather.utils.SubName;
import fan.thisweather.utils.ThreadUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ListView mListViewMenu;
    private List<String> mMenuList = new ArrayList<>();
    private static int FLUSH_UI = 33;
    private String weatherJson;

    private SharedPreferences mSp;

    private CityBean mNewCityBean;
    private int mCurrentIndexCityName = 0;

    private ObtainWeatherBean weatherBean;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 33:

                    if(mAdapter == null){
                        mAdapter = new MenuAdapter(MainActivity.this,mMenuList,mCurrentIndexCityName);
                        mListViewMenu.setAdapter(mAdapter);
                    }else{
                        mAdapter.notifyDataSetChanged();
                    }
                    break;

                case 66:

                    weatherJson = (String) msg.obj;
                    weatherBean = ParseJson.stringToBean(weatherJson);

                    if(weatherBean.getStatus() == 1000){

                        //保存获取到的json
                        if(mCurrentIndexCityName == 0){
                            CacheUtils.saveCache(MainActivity.this,SubName.ObtainNet,weatherJson);
                        }
                        setData();

                    }else{
                        Toast.makeText(MainActivity.this, "天气获取失败！", Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };

    private MenuAdapter mAdapter;
    private Intent runLocationService;
    private TextView mTVType;
    private TextView mTVCityName;
    private TextView mTVWendu;
    private LocationReceiver mMyLocation;
    private DrawerLayout mDrawerly;
    private TextView mTVSuggest;
    private ShowWeatherInfoAdapter mWeatherAllAdapter;
    private ListView mWeatherAllListView;
    private BroadcastReceiver netReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);

        initCity();
        initView();
        initListener();
        initOldData();
        initLocation();

    }


    /**
     * 设置数据
     */
    private void setData(){

        String tempName;
        if(mCurrentIndexCityName == 0){
            tempName = CacheUtils.getLocationName(MainActivity.this,"locationName");
        }else{
            tempName = mMenuList.get(mCurrentIndexCityName);
        }
        mTVCityName.setText(tempName);
        mTVSuggest.setText("\u3000\u3000"+weatherBean.getData().getGanmao());

        mTVType.setText(weatherBean.getData().getForecast().get(0).getType());
        mTVWendu.setText(weatherBean.getData().getWendu());

        setAllWeather();


        handler.sendEmptyMessage(33);

    }


    /**
     * 初始化侧边城市栏
     */
    private void initCity() {

        mSp = getSharedPreferences(SubName.sp_name, MODE_PRIVATE);

        // 判断是不是第一次运行
        boolean isFirstRun = mSp.getBoolean(SubName.key_is_first_run, true);
        if (isFirstRun) {
            // 创建快捷方式图标
            installShortCut();
            CacheUtils.saveCityName(this,"当前位置");
            mSp.edit().putBoolean(SubName.key_is_first_run, false).commit();
        }

        //获取保存的城市名字
        mMenuList = CacheUtils.getCityNames(this);

        handler.sendEmptyMessage(33);


    }

    /**
     * 初始化控件
     */
    private void initView() {

        //侧拉菜单
        mListViewMenu = (ListView) findViewById(R.id.lv_menu);

        toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0,0);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        mTVType = (TextView) findViewById(R.id.tv_type);
        mTVCityName = (TextView) findViewById(R.id.tv_city_name);
        mTVWendu = (TextView) findViewById(R.id.tv_wendu);


        mDrawerly = (DrawerLayout) findViewById(R.id.drawer);

        mTVSuggest = (TextView) findViewById(R.id.tv_weather_info_suggest);

        mWeatherAllListView = (ListView)findViewById(R.id.lv_weather_all);

        //创建脚布局
        View addView = View.inflate(this,R.layout.item_listview_city_name,null);
        TextView mTVAddCity = (TextView) addView.findViewById(R.id.tv_menu_name);
        mTVAddCity.setText("+ 添加");
        //把脚布局添加到城市里面中
        mListViewMenu.addFooterView(addView);

    }

    /**
     * 控件的监听
     */
    private void initListener() {


        toolbar.inflateMenu(R.menu.menu_menu);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.menu_add_city){
                    addCity();
                }
                return true;
            }
        });

        //侧滑菜单监听
        mDrawerly.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        //城市列表点击条目监听
        mListViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mDrawerly.closeDrawers();

                if(position == mMenuList.size()){

                    addCity();

                    return ;
                }

                mCurrentIndexCityName = position;

                ByCurrentCityToSearchWeather(mCurrentIndexCityName);

                mAdapter.setSelectIndex(mCurrentIndexCityName);
                mAdapter.notifyDataSetChanged();

            }
        });

        mListViewMenu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0 || position == mMenuList.size()){
                    return false;
                }

                showHint(position);

                return true;
            }
        });


        netReceiver = new NetBroadCastReciver();

        //注册BroadCastReciver,设置监听的频道。就是filter中的
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netReceiver, filter);

    }


    private void showHint(final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        //下面的set方法返回值还是builder，所以也可以连着写builder.setTitle().setMe....;
        builder.setTitle("确定要删除该城市？");
        //禁用返回键
//        builder.setCancelable(false);
        //确定按钮   Positive：积极地
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                delectCity(position);
            }
        });

        //取消按钮   Negative：消极的
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //最后一定要显示出来
        builder.show();
    }


    /**
     * 删除城市
     */
    private void delectCity(int position){

        CacheUtils.delectCityName(MainActivity.this,mMenuList.get(position));

        mMenuList.remove(position);

        if(mCurrentIndexCityName == position){

            mCurrentIndexCityName = 0;
            //根据城市名字查询天气
            ByCurrentCityToSearchWeather(mCurrentIndexCityName);
        }

        mAdapter.setSelectIndex(mCurrentIndexCityName);
        mAdapter.notifyDataSetChanged();

    }


    /**
     * 根据当前选择的城市查询天气
     * @param position
     */
    private void ByCurrentCityToSearchWeather(int position){

        if(position == 0){

            unregisterReceiver(mMyLocation);
            stopService(runLocationService);
            initLocation();

        }else{

            mTVCityName.setText(mMenuList.get(position));
            //根据点击的城市去查询天气
            ByClickToWeather(mMenuList.get(position));

        }
    }


    /**
     * 添加城市
     */
    private void addCity(){

        //启动新activity添加城市
        startActivityForResult(new Intent(MainActivity.this,AddCity.class),99);
        //过度动画
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }


    /**
     * 接收上个页面返回结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == 444){

            String name = data.getStringExtra("addCityName");

            //如果集合中存在
            if(!mMenuList.contains(name)){
                mMenuList.add(name);
                CacheUtils.saveCityName(this,name);
            }

            mCurrentIndexCityName = mMenuList.indexOf(name);

            //根据城市名字查询天气
            ByCurrentCityToSearchWeather(mCurrentIndexCityName);
            mAdapter.setSelectIndex(mCurrentIndexCityName);
            mAdapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 初始化旧数据
     */
    private void initOldData(){

        //1.读取缓存
        String cacheJson = CacheUtils.readCache(this, SubName.ObtainNet);
        if(!TextUtils.isEmpty(cacheJson)){
            //2.如果缓存存在，解析展示
			parseJson(cacheJson);
        }
    }

    /**
     * 解析缓存
     */
    private void parseJson(String Json){
        weatherBean = ParseJson.stringToBean(Json);
        setData();
    }


    /**
     * 根据选中的城市去查询天气
     */
    private void ByClickToWeather(String cityName){

        obtainCityCode(cityName,cityName);
    }

    /**
     * 初始化位置
     */
    private void initLocation() {

        mMyLocation = new LocationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("fan.location");
        registerReceiver(mMyLocation, intentFilter);

        //启动位置服务
        runLocationService = new Intent(this,ObtainLocationService.class);

        startService(runLocationService);
    }

    /**
     * 位置接收器
     * @author len
     *
     */
    public class LocationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //拿到进度，更新UI

            //获取城市
            String mNativeLocation = intent.getStringExtra("location");
            //获取城区
            String mDistrict = intent.getStringExtra("locationDistrict");

            if(mNativeLocation != null){

                CacheUtils.saveLocationName(MainActivity.this,"locationName",mDistrict);

                //对城市名字进行修改
                mNativeLocation = changeCityName(mNativeLocation);
                mDistrict = changeCityName(mDistrict);

                mNewCityBean = new CityBean(mNativeLocation,mDistrict,mTVCityName.getText().toString());

                //通过城市名字获取城市编码
                obtainCityCode(mNativeLocation,mDistrict);
            }else{

                Toast.makeText(context, "当前位置获取失败，请检查网络或手动添加城市！", Toast.LENGTH_LONG).show();
            }

        }

    }

    /**
     * 通过城市名字获取城市编码
     * @param mCity 城市名字
     * @param mDistrict 城区名字
     */
    private void obtainCityCode(String mCity, String mDistrict){

        //获取城市id
        String id = NameToCode.cityNameToCityCode(MainActivity.this,mCity,mDistrict);

        if(id == null){
            Toast.makeText(this, "城市id获取失败！", Toast.LENGTH_SHORT).show();
            return;
        }

        obtainWeather(id);

    }


    /**
     * OKHettp
     */
    private final OkHttpClient client = new OkHttpClient();

    /**
     * 获取天气信息
     * @param cityId
     */
    private void obtainWeather(String cityId){

        String GroupUrl = SubName.ObtainNet + cityId;
        final String netUrl = GroupUrl;

        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                try {
                    execute(netUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 执行获取天气
     * @throws Exception
     */
    public void execute(String netUrl) throws Exception {
        Request request = new Request.Builder()
                .url(netUrl)
                .build();
        Response response = client.newCall(request).execute();

        //获取网络请求成功
        if(response.isSuccessful()){

            //获取的json
            String obtainWeatherData = response.body().string();
            Message msg = Message.obtain();
            msg.what = 66;
            msg.obj = obtainWeatherData;
            handler.sendMessage(msg);

        }else{
            Toast.makeText(this, "天气请求失败！请检查网络是否畅通！", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 修剪城市名
     * @param cityName
     * @return
     */
    private String changeCityName(String cityName){

        char a = cityName.charAt(cityName.length()-1);

        if((a+"").equals("市") || (a+"").equals("县") || (a+"").equals("区")){
            cityName = cityName.substring(0,cityName.length()-1);
        }

        return cityName;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }


    /**
     * 设置所有天气数据
     */
    public void setAllWeather(){

        if(mWeatherAllAdapter == null){

            mWeatherAllAdapter = new ShowWeatherInfoAdapter(this,weatherBean);
            mWeatherAllListView.setAdapter(mWeatherAllAdapter);

        }else{

            mWeatherAllAdapter.setShowData(weatherBean);
            mWeatherAllAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 网络监听
     */
    public class NetBroadCastReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //判断wifi是打开还是关闭
            if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())){ //此处无实际作用，只是看开关是否开启
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_DISABLED:
                        break;

                    case WifiManager.WIFI_STATE_DISABLING:
                        break;
                }
            }
            //此处是主要代码，
            //如果是在开启wifi连接和有网络状态下
            if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if(NetworkInfo.State.CONNECTED==info.getState()){
                    //连接状态
//                    Log.e("fan", "有网络连接");
                    //执行后续代码
//                    if(mCurrentIndexCityName == 0){
//                        initLocation();
//                    }else{
                    ByCurrentCityToSearchWeather(mCurrentIndexCityName);
//                    }

                }else{
//                    Log.e("fan", "无网络连接");
                }
            }

        }
    }

    /**
     * 创建快捷方式
     */
    private void installShortCut() {

//        Intent intent = new Intent();
//        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
//        // 快捷图标名字
//        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "security");
//        // 快捷图标的图标
//        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory
//                .decodeResource(getResources(), R.drawable.ic_launcher));
//        // 快捷方式的功能
//        // Intent todo = new Intent(this,HomeActivity.class); //两个软件之前不能用过显示意图调用
//        Intent todo = new Intent();
//        todo.setAction("cn.fan.creat.shortcut");
//        todo.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, todo);
//        // intent.putExtra(EXTRA_SHORTCUT_DUPLICATE, false);
//        sendBroadcast(intent);

    }


    @Override
    protected void onDestroy() {
        stopService(runLocationService);
        unregisterReceiver(mMyLocation);
        unregisterReceiver(netReceiver);
        super.onDestroy();
    }
}
