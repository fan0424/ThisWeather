package fan.thisweather.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fan.thisweather.MainActivity;
import fan.thisweather.R;
import fan.thisweather.adapter.ShowSearchCityNameAdapter;
import fan.thisweather.utils.NameToCode;
import fan.thisweather.utils.ThreadUtils;

/**
 * Created by fan on 2016/5/19.
 */
public class AddCity extends BaseActivity implements TextWatcher {

    private TextView mTVSearch;
    private ImageView mIVDelect;
    private EditText mETInput;
    private List<String> mCityNames = new ArrayList<>();
    private int selectIndex = -1;

    private ShowSearchCityNameAdapter mAdapter;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 11:

                    Toast.makeText(AddCity.this, "未查到当前城市！", Toast.LENGTH_SHORT).show();
                    mCityNames.clear();
                    handler.sendEmptyMessage(22);

                    break;

                case 22:

                    if(mAdapter == null){
                        mAdapter = new ShowSearchCityNameAdapter(AddCity.this,mCityNames);
                        mListView.setAdapter(mAdapter);
                    }else{
                        mAdapter.setList(mCityNames);
                        mAdapter.notifyDataSetChanged();
                    }

                    break;

                case 33:

                    returnSelectCityName(selectIndex);

                    break;

            }
        }
    };
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcity);

        initView();
        initListener();
    }


    /**
     * 初始化控件
     */
    private void initView() {

        mTVSearch = (TextView) findViewById(R.id.tv_search);
        mIVDelect = (ImageView) findViewById(R.id.iv_seatch_delete);
        mETInput = (EditText) findViewById(R.id.et_input);
        mListView = (ListView) findViewById(R.id.lv_show_search_result);

    }


    /**
     * 初始化监听
     */
    private void initListener() {

        mTVSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSearch(changeCityName(mETInput.getText().toString()));
            }
        });

        mIVDelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mETInput.setText("");
            }
        });

        mETInput.addTextChangedListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectIndex = position;
                handler.sendEmptyMessage(33);
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    /**
     * 文本改变监听
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(s.length() > 0){
            mIVDelect.setVisibility(View.VISIBLE);
            toSearch(changeCityName(s+""));

        }else{
            mIVDelect.setVisibility(View.GONE);
            mCityNames.clear();
            handler.sendEmptyMessage(22);
        }

    }

    /**
     * 矫正城市名字
     */
    private String changeCityName(String s){

        s = s.trim();

        if(s.length() < 3){
            return s;
        }

        char a = s.charAt(s.length()-1);

        if((a+"").equals("市") || (a+"").equals("县") || (a+"").equals("区")){
            s = s.substring(0,s.length()-1);
        }

        return s;

    }


    @Override
    public void afterTextChanged(Editable s) {}

    private void toSearch(final String cityName){

        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                mCityNames = NameToCode.SerchCity(AddCity.this,cityName);

                if(mCityNames.size() == 0){

                    handler.sendEmptyMessage(11);

                }else{

                    handler.sendEmptyMessage(22);

                }

            }
        });


    }


    /**
     * 返回选择的城市名字
     */
    private void returnSelectCityName(int position) {

        Intent intent = new Intent(AddCity.this,MainActivity.class);
        intent.putExtra("addCityName",mCityNames.get(position));
        setResult(444,intent);
        finish();
    }

}
