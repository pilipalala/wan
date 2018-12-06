package com.wyj.wan.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wyj.wan.app.App;

import java.io.File;

/**
 * Created by admin
 * on 2017/8/31.
 * TODO
 * <p>
 * 注意with()方法中传入的实例会决定Glide加载图片的生命周期，
 * 如果传入的是Activity或者Fragment的实例，那么当这个Activity或Fragment被销毁的时候，
 * 图片加载也会停止。如果传入的是ApplicationContext，那么只有当应用程序被杀掉的时候，图片加载才会停止。
 * <p>
 * // 加载本地图片
 * File file = new File(getExternalCacheDir() + "/image.jpg");
 * Glide.with(this).load(file).into(imageView);
 * <p>
 * // 加载应用资源
 * int resource = R.drawable.image;
 * Glide.with(this).load(resource).into(imageView);
 * <p>
 * // 加载二进制流
 * byte[] image = getImageBytes();
 * Glide.with(this).load(image).into(imageView);
 * <p>
 * <p>
 * // 加载Uri对象
 * Uri imageUri = getImageUri();
 * Glide.with(this).load(imageUri).into(imageView);
 */

public class GlideUtils {


    /**
     * Glide特点
     * 使用简单
     * 可配置度高，自适应程度高
     * 支持常见图片格式 Jpg png gif webp
     * 支持多种数据源  网络、本地、资源、Assets 等
     * 高效缓存策略    支持Memory和Disk图片缓存 默认Bitmap格式采用RGB_565内存使用至少减少一半
     * 生命周期集成   根据Activity/Fragment生命周期自动管理请求
     * 高效处理Bitmap  使用Bitmap Pool使Bitmap复用，主动调用recycle回收需要回收的Bitmap，减小系统回收压力
     * 这里默认支持Context，Glide支持Context,Activity,Fragment，FragmentActivity
     */


    //加载指定大小
    public static void loadImageViewSize(Context mContext, String path, int width, int height, ImageView mImageView) {
        Glide.with(mContext).load(path).override(width, height).into(mImageView);
    }

    //设置加载中以及加载失败图片
    public static void loadImageViewLoding(Context mContext, String path, ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mContext).load(path).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    //设置加载中以及加载失败图片并且指定大小
    public static void loadImageViewLodingSize(Context mContext, String path, int width, int height, ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mContext).load(path).override(width, height).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    //设置跳过内存缓存
    public static void loadImageViewCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).skipMemoryCache(true).into(mImageView);
    }

    //设置下载优先级
    public static void loadImageViewPriority(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).priority(Priority.NORMAL).into(mImageView);
    }


    /*加载网络图片*/
    public static void loadImageUrl(String url, ImageView imageView) {
        Glide.with(App.getContext())
                .load(url)
//                .placeholder(R.mipmap.ai_3)
//                .error(R.mipmap.ai_10)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     * 加载本地图片
     * File file = new File(getExternalCacheDir() + "/image.jpg");
     * Glide.with(this).load(file).into(imageView);
     */
    public static void loadImagePath(Context context, File file, ImageView imageView) {
        Glide.with(context)
                .load(file)
//                .placeholder(R.drawable.loading)
//                .onError(R.drawable.onError)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    /**
     * 加载应用资源
     * <p>
     * int resource = R.drawable.image;
     * Glide.with(this).load(resource).into(imageView);
     */
    public static void loadImageResource(Context context, int resource, ImageView imageView) {
        Glide.with(context)
                .load(resource)
//                .placeholder(R.drawable.loading)
//                .onError(R.drawable.onError)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    /**
     * 策略解说：
     * <p>
     * all:缓存源资源和转换后的资源
     * </p><p>
     * none:不作任何磁盘缓存
     * </p><p>
     * source:缓存源资源
     * </p><p>
     * result：缓存转换后的资源
     */

    //设置缓存策略
    public static void loadImageViewDiskCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
    }

    /**
     * api也提供了几个常用的动画：比如crossFade()
     */

    //设置加载动画
    public static void loadImageViewAnim(Context mContext, String path, int anim, ImageView mImageView) {
        Glide.with(mContext).load(path).animate(anim).into(mImageView);
    }

    /**
     * 会先加载缩略图
     */

    //设置缩略图支持
    public static void loadImageViewThumbnail(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).thumbnail(0.1f).into(mImageView);
    }

    /**
     * api提供了比如：centerCrop()、fitCenter()等
     */

    //设置动态转换
    public static void loadImageViewCrop(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().into(mImageView);
    }
    //设置监听的用处 可以用于监控请求发生错误来源，以及图片来源 是内存还是磁盘
    //设置监听请求接口
    public static void loadImageViewListener(Context mContext, String path, ImageView mImageView, RequestListener<String, GlideDrawable> requstlistener) {
        Glide.with(mContext).load(path).listener(requstlistener).into(mImageView);
    }
    //设置预加载的内容
    public static void loadImageViewContent(Context mContext, String path, SimpleTarget<GlideDrawable> simpleTarget) {
        Glide.with(mContext).load(path).centerCrop().into(simpleTarget);
    }
    //清理磁盘缓存
    public static void GuideClearDiskCache(Context mContext) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }

    //清理内存缓存
    public static void GuideClearMemory(Context mContext) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }


}
