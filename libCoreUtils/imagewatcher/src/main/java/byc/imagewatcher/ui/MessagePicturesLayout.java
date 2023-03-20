package byc.imagewatcher.ui;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import byc.imagewatcher.view.imagewatcher.R;

public class MessagePicturesLayout extends FrameLayout implements View.OnClickListener {

    public static final int MAX_DISPLAY_COUNT = 9;
    private final LayoutParams lpChildImage = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    private final int mSingleMaxSize;//单个图像大小(px)
    private final int mSpace;//间距
    private final List<ImageView> iPictureList = new ArrayList<>();//展示ImageView
    private final SparseArray<ImageView> mVisiblePictureList = new SparseArray<>();//可见ImageView
    private final TextView tOverflowCount;//展示超过9张TextView

    private Callback mCallback;
    private boolean isInit;//是否初始化
    private List<Uri> mDataList;//图片加载地址集合
    private List<Uri> mThumbDataList;//缩略图加载地址集合

    public MessagePicturesLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
        mSingleMaxSize = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 216, mDisplayMetrics) + 0.5f);
        mSpace = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, mDisplayMetrics) + 0.5f);

        for (int i = 0; i < MAX_DISPLAY_COUNT; i++) {
            ImageView squareImageView = new SquareImageView(context);
            squareImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            squareImageView.setVisibility(View.GONE);
            squareImageView.setOnClickListener(this);
            addView(squareImageView);
            iPictureList.add(squareImageView);
        }

        //添加超过九张文字说明(第九张图片与超出TextView折叠在一起，图片在下，文字在上)
        tOverflowCount = new TextView(context);
        tOverflowCount.setTextColor(0xFFFFFFFF);
        tOverflowCount.setTextSize(24);
        tOverflowCount.setGravity(Gravity.CENTER);
        tOverflowCount.setBackgroundColor(0x66000000);
        tOverflowCount.setVisibility(View.GONE);
        addView(tOverflowCount);
    }

    public void set(List<Uri> urlThumbList, List<Uri> urlList) {
        mThumbDataList = urlThumbList;
        mDataList = urlList;
        if (isInit) {
            notifyDataChanged();
        }
    }

    //刷新数据
    private void notifyDataChanged() {
        final List<Uri> thumbList = mThumbDataList;
        //判断缩略图集合是否为空
        final int urlListSize = thumbList != null ? mThumbDataList.size() : 0;

        if (thumbList == null || thumbList.size() < 1) {
            //没有缩略图不显示布局
            setVisibility(View.GONE);
            return;
        } else {
            //显示布局
            setVisibility(View.VISIBLE);
        }

        //缩略图url集合大小超过展示图片url集合大小，抛出异常
        if (thumbList.size() > mDataList.size()) {
            throw new IllegalArgumentException("dataList.size(" + mDataList.size() + ") > thumbDataList.size(" + thumbList.size() + ")");
        }
        //计算九宫格行列数
        int column = 3;
        if (urlListSize == 1) {
            column = 1;
        } else if (urlListSize == 4) {
            column = 2;
        }
        int row = 0;
        if (urlListSize > 6) {
            row = 3;
        } else if (urlListSize > 3) {
            row = 2;
        } else if (urlListSize > 0) {
            row = 1;
        }

        //动态计算每个图片的大小
        final int imageSize = urlListSize == 1 ? mSingleMaxSize : (int) ((getWidth() * 1f - mSpace * (column - 1)) / column);
        //设置图片LayoutParams的宽高
        lpChildImage.width = imageSize;
        lpChildImage.height = lpChildImage.width;

        //设置超出TextView
        tOverflowCount.setVisibility(urlListSize > MAX_DISPLAY_COUNT ? View.VISIBLE : View.GONE);
        tOverflowCount.setText("+ " + (urlListSize - MAX_DISPLAY_COUNT));
        tOverflowCount.setLayoutParams(lpChildImage);
        //清空可见图片集合
        mVisiblePictureList.clear();
        for (int i = 0; i < iPictureList.size(); i++) {
            final ImageView iPicture = iPictureList.get(i);
            if (i < urlListSize) {
                iPicture.setVisibility(View.VISIBLE);
                mVisiblePictureList.put(i, iPicture);
                iPicture.setLayoutParams(lpChildImage);
                iPicture.setBackgroundResource(R.drawable.default_picture);
                Glide.with(getContext()).load(thumbList.get(i)).into(iPicture);
                //设置ImageView摆放位置
                iPicture.setTranslationX((i % column) * (imageSize + mSpace));
                iPicture.setTranslationY((i / column) * (imageSize + mSpace));
            } else {
                iPicture.setVisibility(View.GONE);
            }
            //设置超出TextView摆放位置
            if (i == MAX_DISPLAY_COUNT - 1) {
                tOverflowCount.setTranslationX((i % column) * (imageSize + mSpace));
                tOverflowCount.setTranslationY((i / column) * (imageSize + mSpace));
            }
        }
        //重置控件高度
        getLayoutParams().height = imageSize * row + mSpace * (row - 1);
    }

    @Override
    public void onClick(View v) {
        if (mCallback != null) {
            mCallback.onThumbPictureClick((ImageView) v, mVisiblePictureList, mDataList);
        }
    }

    //缩略图点击回调
    public interface Callback {
        void onThumbPictureClick(ImageView i, SparseArray<ImageView> imageGroupList, List<Uri> urlList);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //控件大小发生改变时火调
        super.onSizeChanged(w, h, oldw, oldh);
        isInit = true;
        notifyDataChanged();
    }
}
