package fan.thisweather.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import fan.thisweather.R;
import fan.thisweather.bean.ObtainWeatherBean;

/**
 * Created by fan on 2016/5/18.
 */
public class ShowWeatherInfoAdapter extends BaseAdapter {

    private ObtainWeatherBean mWeatherInfos;
    private Context context;

    public ShowWeatherInfoAdapter(Context context,ObtainWeatherBean mWeatherInfos) {
        this.mWeatherInfos = mWeatherInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mWeatherInfos.getData().getForecast().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        if (convertView == null) {
//            convertView = View.inflate(context, R.layout.item_weather_all_info,null);
//        }
//
//        TextView mTvWeek = getHolderView(convertView,R.id.tv_weather_info_week);
//        TextView mTvLow = getHolderView(convertView,R.id.tv_weather_info_low);
//        TextView mTvHigh = getHolderView(convertView,R.id.tv_weather_info_high);
//        TextView mTvType = getHolderView(convertView,R.id.tv_weather_info_type);
//        TextView mTvFengli = getHolderView(convertView,R.id.tv_weather_info_fengli);
//
//
//
//        mTvWeek.setText(mWeatherInfos.getData().getForecast().get(position).getDate());
//        mTvLow.setText(changeTemp(mWeatherInfos.getData().getForecast().get(position).getLow()));
//        mTvHigh.setText(changeTemp(mWeatherInfos.getData().getForecast().get(position).getHigh()));
//        mTvType.setText(mWeatherInfos.getData().getForecast().get(position).getType());
//        mTvFengli.setText(mWeatherInfos.getData().getForecast().get(position).getFengli());

        ViewHolder vh;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_weather_all_info,null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        vh.mTvWeek.setText(mWeatherInfos.getData().getForecast().get(position).getDate());
        vh.mTvLow.setText(changeTemp(mWeatherInfos.getData().getForecast().get(position).getLow()));
        vh.mTvHigh.setText(changeTemp(mWeatherInfos.getData().getForecast().get(position).getHigh()));
        vh.mTvType.setText(mWeatherInfos.getData().getForecast().get(position).getType());
        vh.mTvFengli.setText(mWeatherInfos.getData().getForecast().get(position).getFengli());

        return convertView;
    }

    /**
     * 修改温度
     * @return
     */
    private String changeTemp(String temp){

        if(temp.contains("低温")||temp.contains("高温")){
            temp = temp.substring(2);
        }

        return temp.trim();
    }

    public void setShowData(ObtainWeatherBean mWeatherInfos){
        this.mWeatherInfos = mWeatherInfos;
    }

//    public static <T extends View> T getHolderView(View convertView, int id) {
//
//        SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
//
//        if (viewHolder == null) {
//            viewHolder = new SparseArray<View>();
//            convertView.setTag(viewHolder);
//        }
//
//        View childView = viewHolder.get(id);
//
//        if (childView == null) {
//            childView = convertView.findViewById(id);
//            viewHolder.put(id, childView);
//        }
//
//        return (T) childView;
//    }

    class ViewHolder{

        TextView mTvWeek;
        TextView mTvLow;
        TextView mTvHigh;
        TextView mTvType;
        TextView mTvFengli;

        public ViewHolder(View convertView) {
            mTvWeek = (TextView) convertView.findViewById(R.id.tv_weather_info_week);
            mTvLow = (TextView) convertView.findViewById(R.id.tv_weather_info_low);
            mTvHigh = (TextView) convertView.findViewById(R.id.tv_weather_info_high);
            mTvType = (TextView) convertView.findViewById(R.id.tv_weather_info_type);
            mTvFengli = (TextView) convertView.findViewById(R.id.tv_weather_info_fengli);
        }
    }

}
