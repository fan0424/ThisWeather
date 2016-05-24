package fan.thisweather.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * ViewPagerIndicator指示器，
 *
 *  默认高亮颜色为白色 可以通过setTitleTextColor()方法修改
 *  默认背景颜色会深灰色 可以通过View的通用的setbackground.....()方法修改
 *  默认屏幕显示标题数量为4个 可以通过setVisibleTabCount方法修改
 *  设置显示的tab setTabItemTitles();
 *
 * Created by fan on 2016/5/9.
 */
public class ViewPagerIndicator extends LinearLayout{

    /**
     * 默认可见tab数量4个
     */
    private static int COUNT_DEFAULT_TAB = 4;
    /**
     * 默认高亮颜色
     */
    private static int COLOR_TEXT_HIGHLIGHT = 0xFFFFFFFF;

    /**
     * 未选中文本的颜色
     */
    private static final int COLOR_TEXT_NORMAL = 0xFF2C3034;

    private static int TEXT_SIZE = 16;

    /**
     * 矩形宽度
     */
    private int mRectangleWidth;

    /**
     * 矩形高度
     */
    private int mRectangleHeight;

    ViewPager mViewPager;

    private int mInitTranslationX;
    private int mTranslationX;

    /**
     * 标题栏文本
     */
    private List<String> mTitlesa;

    private String[] mTitles;

    private Paint mPaint;
    private Path mPath;


    public ViewPagerIndicator(Context context) {
        this(context,null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //======================下面代码是可以添加命名空间===========
//        // 获取可见Tab的数量
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
//        //设置可见tab的数量
//        mTabVisibleCount = a.getInt(R.styleable.ViewPagerIndicator_visible_tab_count,COUNT_DEFAULT_TAB);
//        if(mTabVisibleCount < 0){
//            mTabVisibleCount = COUNT_DEFAULT_TAB;
//        }
//
//        mHighLightColor = a.getInt(R.styleable.ViewPagerIndicator_color_text_highlight,COLOR_TEXT_HIGHLIGHT);
//
//        a.recycle();
        //======================下面代码不需要命名空间===================

        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(COLOR_TEXT_HIGHLIGHT);
        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setPathEffect(new CornerPathEffect(3));

    }

    /**
     * 设置高亮文本的颜色
     * @param color
     */
    public void setTitleTextColor(int color){
        COLOR_TEXT_HIGHLIGHT = color;
    }

    @Override
    public void setLayoutAnimation(LayoutAnimationController controller) {
        super.setLayoutAnimation(controller);
    }


    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged( w, h, oldw, oldh);

        //矩形宽度
        mRectangleWidth = w / COUNT_DEFAULT_TAB;

        initRectangle();

    }

    protected void dispatchDraw(Canvas canvas){

        canvas.save();
        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 2);
        canvas.drawPath(mPath, mPaint);

        canvas.restore();

        super.dispatchDraw(canvas);
    }

    /**
     * 初始化矩形
     */
    private void initRectangle() {

        //矩形高度
        mRectangleHeight = dip2px(getContext(),3);


//        mPath = new Path();
//		mPath.moveTo(0, 0);
//		mPath.lineTo(mRectangleWidth, 0);
//		mPath.lineTo(mRectangleWidth,-mRectangleHeight);
//		mPath.lineTo(0, -mRectangleHeight);

        int newWid = mRectangleWidth/5;
        mPath = new Path();
        mPath.moveTo(newWid*2, 0);
        mPath.lineTo(newWid*3, 0);
        mPath.lineTo(newWid*3,-mRectangleHeight);
        mPath.lineTo(newWid*2, -mRectangleHeight);

		mPath.close();
    }

    /**
     * 设置文字大小
     * @param size
     */
    public void setTextSize(int size){
        TEXT_SIZE = dip2px(getContext(),size);
    }


    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    private int getScreenWidth()
    {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        int cCount = getChildCount();
        if (cCount == 0)
            return;

        for (int i = 0; i < cCount; i++)
        {
            View view = getChildAt(i);
            LayoutParams lp = (LayoutParams) view
                    .getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenWidth() / COUNT_DEFAULT_TAB;
            view.setLayoutParams(lp);
        }

        setItemClickEvent();

    }


    /**
     * 指示器跟随手指进行滚动
     *
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset)
    {
        int tabWidth = getWidth() / COUNT_DEFAULT_TAB;
        mTranslationX = (int) (tabWidth * (offset + position));

        // 容器移动，在tab处于移动至最后一个时
        if (position >= (COUNT_DEFAULT_TAB - 2) && offset > 0
                && getChildCount() > COUNT_DEFAULT_TAB)
        {

            if (COUNT_DEFAULT_TAB != 1){

                if(position == getChildCount()-2){

                // 最后一个

                }else{

                    this.scrollTo((position - (COUNT_DEFAULT_TAB - 2)) * tabWidth
                            + (int) (tabWidth * offset), 0);

                }
            } else
            {
                this.scrollTo(position * tabWidth + (int) (tabWidth * offset),
                        0);
            }

        }

        invalidate();

    }

    /**
     * 设置标题栏选项文本
     * @param titles
     */
    public void setTabItemTitles(String[] titles)
    {
        if (titles != null && titles.length > 0 && titles.length >= COUNT_DEFAULT_TAB)
        {
            this.removeAllViews();
            mTitles = titles;
            for (String title : mTitles)
            {
                addView(generateTextView(title));
            }

            setItemClickEvent();

        }else{

            throw new IllegalStateException("tab集合的长度小于显示tab的数量，请修改tab集合或显示tab的数量！");

        }
    }

    /**
     * 设置可见的Tab数量
     *
     * @param count
     */
    public void setVisibleTabCount(int count)
    {
        COUNT_DEFAULT_TAB = count;
    }

    /**
     * 根据title创建Tab
     *
     * @param title
     * @return
     */
    private View generateTextView(String title)
    {
        TextView tv = new TextView(getContext());
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth() / COUNT_DEFAULT_TAB;
        tv.setText(title);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TEXT_SIZE);
        tv.setTextColor(COLOR_TEXT_NORMAL);
        tv.setLayoutParams(lp);
        return tv;
    }


    public interface PageOnchangeListener
    {
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels);

        public void onPageSelected(int position);

        public void onPageScrollStateChanged(int state);
    }

    public PageOnchangeListener mListener;

    public void setOnPageChangeListener(PageOnchangeListener listener)
    {
        this.mListener = listener;
    }


    /**
     * 设置关联的ViewPager
     *
     * @param viewPager
     * @param pos
     */
    public void setViewPager(ViewPager viewPager, int pos)
    {

        mPaint.setColor(COLOR_TEXT_HIGHLIGHT);
        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {

            @Override
            public void onPageSelected(int position)
            {

                if (mListener != null)
                {
                    mListener.onPageSelected(position);
                }

                highLightTextView(position);
                // 极端情况的Bug修复
                if (position <= (COUNT_DEFAULT_TAB - 2))
                    scrollTo(0, 0);

            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels)
            {
                scroll(position, positionOffset);
                if (mListener != null)
                {
                    mListener.onPageScrolled(position, positionOffset,
                            positionOffsetPixels);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

                if (mListener != null)
                {
                    mListener.onPageScrollStateChanged(state);
                }

            }
        });
        mViewPager.setCurrentItem(pos);
        highLightTextView(pos);
    }


    /**
     * 重置TAB文本颜色
     */
    private void resetTextViewColor()
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            View view = getChildAt(i);
            if (view instanceof TextView)
            {
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }

    }

    /**
     * 高亮某个Tab的文本
     *
     * @param pos
     */
    private void highLightTextView(int pos)
    {
        resetTextViewColor();
        View view = getChildAt(pos);
        if (view instanceof TextView)
        {
            ((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHT);
        }
    }

    /**
     * 设置Tab的点击事件
     */
    private void setItemClickEvent()
    {
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++)
        {
            final int j = i;
            View view = getChildAt(i);

            view.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mViewPager.setCurrentItem(j);
                }
            });

        }

    }


}
