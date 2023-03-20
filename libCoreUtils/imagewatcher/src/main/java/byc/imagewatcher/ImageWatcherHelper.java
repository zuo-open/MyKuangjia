package byc.imagewatcher;

import android.app.Activity;
import android.net.Uri;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import byc.imagewatcher.view.imagewatcher.R;


public class ImageWatcherHelper {
    private static final int VIEW_IMAGE_WATCHER_ID = R.id.view_image_watcher;
    private final Activity holder;//持有ImageWatcher的activity
    private final ViewGroup activityDecorView;//改activity持有的DecorView
    private ImageWatcher mImageWatcher;
    private ImageWatcher.Loader loader;//Glide加载图片回调
    private Integer statusBarHeight;
    private Integer resErrorImage;
    private ImageWatcher.OnPictureLongPressListener listener;//长按回调
    private ImageWatcher.IndexProvider indexProvider;//底部索引导航
    private ImageWatcher.LoadingUIProvider loadingUIProvider;//加载图片动画
    private ImageWatcher.OnStateChangedListener onStateChangedListener;//状态改变回调

    private ImageWatcherHelper(Activity activity) {
        holder = activity;
        activityDecorView = (ViewGroup) activity.getWindow().getDecorView();
    }

    public static ImageWatcherHelper with(Activity activity, ImageWatcher.Loader l) { // attach
        if (activity == null) throw new NullPointerException("activity is null");
        if (l == null) throw new NullPointerException("loader is null");
        ImageWatcherHelper iwh = new ImageWatcherHelper(activity);
        iwh.loader = l;
        return iwh;
    }

    /**
     * // 如果不是透明状态栏，你需要给ImageWatcher标记 一个偏移值，以修正点击ImageView查看的启动动画的Y轴起点的不正确
     *
     * @param statusBarHeight
     * @return
     */
    public ImageWatcherHelper setTranslucentStatus(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
        return this;
    }

    /**
     * 配置error图标 如果不介意使用lib自带的图标，并不一定要调用这个API
     *
     * @param resErrorImage
     * @return
     */
    public ImageWatcherHelper setErrorImageRes(int resErrorImage) {
        this.resErrorImage = resErrorImage;
        return this;
    }

    public ImageWatcherHelper setOnPictureLongPressListener(ImageWatcher.OnPictureLongPressListener listener) {
        this.listener = listener;
        return this;
    }

    public ImageWatcherHelper setIndexProvider(ImageWatcher.IndexProvider ip) {
        indexProvider = ip;
        return this;
    }

    public ImageWatcherHelper setLoadingUIProvider(ImageWatcher.LoadingUIProvider lp) {
        loadingUIProvider = lp;
        return this;
    }

    /**
     * 设置状态改变回调
     *
     * @param listener
     * @return
     */
    public ImageWatcherHelper setOnStateChangedListener(ImageWatcher.OnStateChangedListener listener) {
        onStateChangedListener = listener;
        return this;
    }

    /**
     * 显示图片展示控件
     *
     * @param i                当前展示图片索引
     * @param imageGroupList   //
     * @param urlList//图片url集合
     */
    public void show(ImageView i, SparseArray<ImageView> imageGroupList, List<Uri> urlList) {
        init();
        mImageWatcher.show(i, imageGroupList, urlList);
    }

    public void show(List<Uri> urlList, int initPos) {
        init();
        mImageWatcher.show(urlList, initPos);
    }

    /**
     * 将相关配置参数传入ImageWatcher对象
     */
    private void init() {
        mImageWatcher = new ImageWatcher(holder);
        mImageWatcher.setId(VIEW_IMAGE_WATCHER_ID);//设置id
        mImageWatcher.setLoader(loader);//设置图片加载loader
        mImageWatcher.setDetachAffirmative(); // helper 设置图片释放模式
        if (statusBarHeight != null) mImageWatcher.setTranslucentStatus(statusBarHeight);
        if (resErrorImage != null) mImageWatcher.setErrorImageRes(resErrorImage);
        if (listener != null) mImageWatcher.setOnPictureLongPressListener(listener);
        if (indexProvider != null) mImageWatcher.setIndexProvider(indexProvider);
        if (loadingUIProvider != null) mImageWatcher.setLoadingUIProvider(loadingUIProvider);
        if (onStateChangedListener != null)
            mImageWatcher.setOnStateChangedListener(onStateChangedListener);

        removeExistingOverlayInView(activityDecorView); // 理论上是无意义的操作。在ImageWatcher 'dismiss' 时会移除自身。但检查一下不错
        activityDecorView.addView(mImageWatcher);
    }

    public boolean handleBackPressed() {
        return mImageWatcher != null && mImageWatcher.handleBackPressed();
    }

    /**
     * 遍历删除ImageWatcher
     * @param parent
     */
    private void removeExistingOverlayInView(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child.getId() == VIEW_IMAGE_WATCHER_ID) {
                parent.removeView(child);
            }
            if (child instanceof ViewGroup) {
                removeExistingOverlayInView((ViewGroup) child);
            }
        }
    }

    public interface Provider {
        ImageWatcherHelper iwHelper();
    }
}
