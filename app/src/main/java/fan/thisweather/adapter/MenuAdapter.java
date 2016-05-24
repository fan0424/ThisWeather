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
 * Created by fan on 2016/5/16.
 */
public class MenuAdapter extends BaseAdapter {

    private Context context;
    private List<String> mMenuList;
    private int selectIndex;

    public MenuAdapter(Context context, List<String> mMenuList,int selectIndex) {
        this.context = context;
        this.mMenuList = mMenuList;
        this.selectIndex = selectIndex;
    }

    public void setSelectIndex(int selectIndex){
        this.selectIndex = selectIndex;
    }

    @Override
    public int getCount() {
        return mMenuList.size();
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
            convertView = View.inflate(context,R.layout.item_listview_city_name,null);
        }

        TextView mTvMenuName = getAdapterView(convertView,R.id.tv_menu_name);
        if(position == selectIndex){
            mTvMenuName.setBackgroundColor(context.getResources().getColor(R.color.colorBlue));
        }else{
            mTvMenuName.setBackgroundColor(context.getResources().getColor(R.color.colorTran));
        }
        mTvMenuName.setText(mMenuList.get(position));
        return convertView;
    }

    public void setList(List<String> mMenuList){
        this.mMenuList = mMenuList;
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
