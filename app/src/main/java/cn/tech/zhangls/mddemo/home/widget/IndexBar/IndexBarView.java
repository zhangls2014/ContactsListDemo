package cn.tech.zhangls.mddemo.home.widget.IndexBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.tech.zhangls.mddemo.R;
import cn.tech.zhangls.mddemo.home.widget.IndexBar.bean.BaseIndexPinyinBean;

public class IndexBarView extends View {
    /**
     * 索引字符
     *
     * #在最后面（默认的数据源）
     */
    public static String[] INDEX_STRING = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    /**
     * 索引数据源
     */
    private List<String> mIndexData;
    /**
     * 是否需要根据实际的数据来生成索引数据源
     * 例如：只有 A B C 三种tag，那么索引栏就 A B C 三项
     */
    private boolean isNeedRealIndex;
    /**
     * View的宽、高
     */
    private int mWidth, mHeight;
    /**
     * 每个index区域的高度
     */
    private int mGapHeight;
    /**
     * 绘图的画笔
     */
    private Paint mPaint;
    /**
     * 手指按下时的背景色
     */
    private int mPressedBgColor;
    /**
     * 用于特写显示正在被触摸的index值
     */
    private TextView mPressedShowTextView;
    /**
     * Adapter的数据源
     */
    private List<? extends BaseIndexPinyinBean> mSourceData;
    /**
     * LinearLayoutManager
     */
    private LinearLayoutManager mLayoutManager;

    public IndexBarView(Context context) {
        this(context, null);
    }

    public IndexBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * indexBar初始化
     * @param context 上下文对象
     * @param attrs 属性
     * @param defStyleAttr 风格属性
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //默认的TextSize
        int textSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
        //默认按下是纯黑色
        mPressedBgColor = Color.BLACK;
        //获取XML文件中设置的属性值
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.IndexBarView, defStyleAttr, 0);
        //将从XML文件中获取的属性值设置到View中
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            //modify 2016 09 07 :如果引用成AndroidLib 资源都不是常量，无法使用switch case
            if (attr == R.styleable.IndexBarView_indexBarTextSize) {
                textSize = typedArray.getDimensionPixelSize(attr, textSize);
            } else if (attr == R.styleable.IndexBarView_indexBarPressBackground) {
                mPressedBgColor = typedArray.getColor(attr, mPressedBgColor);
            }
        }
        //回收属性数组
        typedArray.recycle();

        //不需要真实的索引数据源
        if (!isNeedRealIndex) {
            mIndexData = Arrays.asList(INDEX_STRING);
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        mPaint.setColor(Color.GRAY);

        //设置index触摸监听器
        setmOnIndexPressedListener(new onIndexPressedListener() {
            @Override
            public void onIndexPressed(int index, String text) {
                if (mPressedShowTextView != null) { //显示hintTexView
                    mPressedShowTextView.setVisibility(View.VISIBLE);
                    mPressedShowTextView.setText(text);
                }
                //滑动Rv
                if (mLayoutManager != null) {
                    int position = getPosByTag(text);
                    if (position != -1) {
                        mLayoutManager.scrollToPositionWithOffset(position, 0);
                    }
                }
            }

            @Override
            public void onMotionEventEnd() {
                //隐藏hintTextView
                if (mPressedShowTextView != null) {
                    mPressedShowTextView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //取出宽高的MeasureSpec  Mode 和Size
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidth = 0, measureHeight = 0;//最终测量出来的宽高

        //得到合适宽度：
        Rect indexBounds = new Rect();//存放每个绘制的index的Rect区域
        String index;//每个要绘制的index内容
        for (int i = 0; i < mIndexData.size(); i++) {
            index = mIndexData.get(i);
            mPaint.getTextBounds(index, 0, index.length(), indexBounds);//测量计算文字所在矩形，可以得到宽高
            measureWidth = Math.max(indexBounds.width(), measureWidth);//循环结束后，得到index的最大宽度
            measureHeight = Math.max(indexBounds.width(), measureHeight);//循环结束后，得到index的最大高度，然后*size
        }
        measureHeight *= mIndexData.size();
        switch (wMode) {
            case MeasureSpec.EXACTLY:
                measureWidth = wSize;
                break;
            case MeasureSpec.AT_MOST:
                measureWidth = Math.min(measureWidth, wSize);//wSize此时是父控件能给子View分配的最大空间
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        //得到合适的高度：
        switch (hMode) {
            case MeasureSpec.EXACTLY:
                measureHeight = hSize;
                break;
            case MeasureSpec.AT_MOST:
                measureHeight = Math.min(measureHeight, hSize);//wSize此时是父控件能给子View分配的最大空间
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        setMeasuredDimension(measureWidth, measureHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int t = getPaddingTop();//top的基准点(支持padding)
        Rect indexBounds = new Rect();//存放每个绘制的index的Rect区域
        String index;//每个要绘制的index内容
        for (int i = 0; i < mIndexData.size(); i++) {
            index = mIndexData.get(i);
            mPaint.getTextBounds(index, 0, index.length(), indexBounds);//测量计算文字所在矩形，可以得到宽高
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();//获得画笔的FontMetrics，用来计算baseLine。因为drawText的y坐标，代表的是绘制的文字的baseLine的位置
            int baseline = (int) ((mGapHeight - fontMetrics.bottom - fontMetrics.top) / 2);//计算出在每格index区域，竖直居中的baseLine值
            canvas.drawText(index, mWidth / 2 - indexBounds.width() / 2, t + mGapHeight * i + baseline, mPaint);//调用drawText，居中显示绘制index
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(mPressedBgColor);//手指按下时背景变色
                //注意这里没有break，因为down时，也要计算落点 回调监听器
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                //通过计算判断落点在哪个区域：
                int pressI = (int) ((y - getPaddingTop()) / mGapHeight);
                //边界处理（在手指move时，有可能已经移出边界，防止越界）
                if (pressI < 0) {
                    pressI = 0;
                } else if (pressI >= mIndexData.size()) {
                    pressI = mIndexData.size() - 1;
                }
                //回调监听器
                if (null != mOnIndexPressedListener) {
                    mOnIndexPressedListener.onIndexPressed(pressI, mIndexData.get(pressI));
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                setBackgroundResource(android.R.color.transparent);//手指抬起时背景恢复透明
                //回调监听器
                if (null != mOnIndexPressedListener) {
                    mOnIndexPressedListener.onMotionEventEnd();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //add by zhangxutong 2016 09 08 :解决源数据为空 或者size为0的情况,
        if (null == mIndexData || mIndexData.isEmpty()) {
            return;
        }
        mWidth = w;
        mHeight = h;
        mGapHeight = (mHeight - getPaddingTop() - getPaddingBottom()) / mIndexData.size();
    }


    /**
     * 当前被按下的index的监听器
     */
    public interface onIndexPressedListener {
        void onIndexPressed(int index, String text);//当某个Index被按下

        void onMotionEventEnd();//当触摸事件结束（UP CANCEL）
    }

    private onIndexPressedListener mOnIndexPressedListener;

    public void setmOnIndexPressedListener(onIndexPressedListener mOnIndexPressedListener) {
        this.mOnIndexPressedListener = mOnIndexPressedListener;
    }

    /**
     * 显示当前被按下的index的TextView
     */
    public void setPressedShowTextView(TextView mPressedShowTextView) {
        this.mPressedShowTextView = mPressedShowTextView;
    }

    /**
     * 设置LinearLayoutManager
     */
    public void setLayoutManager(LinearLayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
    }

    /**
     * 一定要在设置数据源{@link #setSourceData(List)}之前调用
     *
     */
    public void setNeedRealIndex(boolean needRealIndex) {
        isNeedRealIndex = needRealIndex;
        if (isNeedRealIndex) {
            if (mIndexData != null) {
                mIndexData = new ArrayList<>();
            }
        }
    }

    public void setSourceData(List<? extends BaseIndexPinyinBean> mSourceData) {
        this.mSourceData = mSourceData;
        initSourceData();//对数据源进行初始化
    }

    /**
     * 初始化原始数据源，并取出索引数据源
     *
     */
    private void initSourceData() {
        //add by zhangxutong 2016 09 08 :解决源数据为空 或者size为0的情况,
        if (null == mSourceData || mSourceData.isEmpty()) {
            return;
        }
        int size = mSourceData.size();
        for (int i = 0; i < size; i++) {
            BaseIndexPinyinBean indexPinyinBean = mSourceData.get(i);
            StringBuilder pySb = new StringBuilder();
            String target = indexPinyinBean.getTag();//取出需要被拼音化的字段
            //遍历target的每个char得到它的全拼音
            for (int i1 = 0; i1 < target.length(); i1++) {
                //利用TinyPinyin将char转成拼音
                //查看源码，方法内 如果char为汉字，则返回大写拼音
                //如果c不是汉字，则返回String.valueOf(c)
                pySb.append(Pinyin.toPinyin(target.charAt(i1)));
            }
            indexPinyinBean.setPinyin(pySb.toString());//设置城市名全拼音

            //以下代码设置城市拼音首字母
            String tagString = pySb.toString().substring(0, 1);
            if (tagString.matches("[A-Z]")) {//如果是A-Z字母开头
                indexPinyinBean.setTag(tagString);
                if (isNeedRealIndex) {//如果需要真实的索引数据源
                    if (!mIndexData.contains(tagString)) {//则判断是否已经将这个索引添加进去，若没有则添加
                        mIndexData.add(tagString);
                    }
                }
            } else {//特殊字母这里统一用#处理
                indexPinyinBean.setTag("#");
                if (isNeedRealIndex) {//如果需要真实的索引数据源
                    if (!mIndexData.contains("#")) {
                        mIndexData.add("#");
                    }
                }
            }
        }
        sortData();
    }

    /**
     * 对数据源排序
     */
    private void sortData() {
        //对右侧栏进行排序 将 # 丢在最后
        Collections.sort(mIndexData, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                if (lhs.equals("#")) {
                    return 1;
                } else if (rhs.equals("#")) {
                    return -1;
                } else {
                    return lhs.compareTo(rhs);
                }
            }
        });

        //对数据源进行排序
        Collections.sort(mSourceData, new Comparator<BaseIndexPinyinBean>() {
            @Override
            public int compare(BaseIndexPinyinBean lhs, BaseIndexPinyinBean rhs) {
                if (lhs.getTag().equals("#")) {
                    return 1;
                } else if (rhs.getTag().equals("#")) {
                    return -1;
                } else {
                    return lhs.getPinyin().compareTo(rhs.getPinyin());
                }
            }
        });
    }


    /**
     * 根据传入的pos返回tag
     *
     * @param tag tag
     * @return pos
     */
    private int getPosByTag(String tag) {
        //add by zhangxutong 2016 09 08 :解决源数据为空 或者size为0的情况,
        if (null == mSourceData || mSourceData.isEmpty() || TextUtils.isEmpty(tag)) {
            return -1;
        }
        for (int i = 0; i < mSourceData.size(); i++) {
            if (tag.equals(mSourceData.get(i).getTag())) {
                return i;
            }
        }
        return -1;
    }
}
