package fan.thisweather.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fan.thisweather.R;

/**
 * Created by fan on 2016/5/19.
 */
public class ShowSearchCityNameAdapter extends BaseAdapter {

    private Context context;
    private List<String> mCityNames;

    public ShowSearchCityNameAdapter(Context context, List<String> mCityNames) {
        this.context = context;
        this.mCityNames = mCityNames;
    }

    @Override
    public int getCount() {
        return mCityNames.size();
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

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_listview_show_search_cityname,null);
        }

        TextView mTvMenuName = getAdapterView(convertView,R.id.tv_show_search_city_name);

        mTvMenuName.setText(mCityNames.get(position));

        return convertView;

    }

    public void setList(List<String> mMenuList){
        this.mCityNames = mMenuList;
    }


    public static <T extends View> T getAdapterView(View convertView, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            convertView.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = convertView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

}
