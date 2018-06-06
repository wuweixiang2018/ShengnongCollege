package com.education.shengnongcollege.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

public class ImageLoadManager {

    public static RequestOptions getCommonOptions(int defaultIcon) {
        RequestOptions options = RequestOptions.formatOf(DecodeFormat.PREFER_RGB_565);
        if (defaultIcon > 0) {
//            if (defaultIcon == 10) {
//                JkysLog.e("", "");
//            }
            options = options.placeholderOf(defaultIcon)
                    .error(defaultIcon)
                    .fallback(defaultIcon);
        }
        return options;
    }

    public static RequestOptions getRoundedOptions(int defaultIcon, int roundingRadius) {
        RequestOptions options = getCommonOptions(defaultIcon);
        options = options.transform(new RoundedCorners(roundingRadius));
        return options;
    }

    private static String getWebpUrl(String url) {
        //webp暂时去掉
        return url;
    }

    public static RequestManager getRequestManager(Object obj) {
        RequestManager requestManager = null;
        if (obj instanceof Activity) {
            requestManager = Glide.with(getContext(obj));
        } else if (obj instanceof FragmentActivity) {
            requestManager = Glide.with(getContext(obj));
        } else if (obj instanceof Fragment) {
            requestManager = Glide.with(getContext(obj));
        } else if (obj instanceof android.support.v4.app.Fragment) {
            requestManager = Glide.with(getContext(obj));
        } else if (obj instanceof Context) {
            requestManager = Glide.with(getContext(obj));
        } else if (obj instanceof View) {
            requestManager = Glide.with((View) obj);
        }
        return requestManager;
    }

    public static Context getContext(Object obj) {
        Context context = null;
        if (obj instanceof Activity) {
            context = ((Activity) obj).getApplicationContext();
        } else if (obj instanceof FragmentActivity) {
            context = ((FragmentActivity) obj).getApplicationContext();
        } else if (obj instanceof Fragment) {
            context = ((Fragment) obj).getActivity().getApplicationContext();
        } else if (obj instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) obj).getActivity().getApplicationContext();
        } else if (obj instanceof Context) {
            context = (Context) obj;
        }
        return context;
    }

    public static RequestManager getRequestManagerByContext(Object obj) {
        RequestManager requestManager = null;
        if (obj instanceof Activity) {
            requestManager = Glide.with(((Activity) obj).getApplicationContext());
        } else if (obj instanceof FragmentActivity) {
            requestManager = Glide.with(((FragmentActivity) obj).getApplicationContext());
        } else if (obj instanceof Fragment) {
            requestManager = Glide.with(((Fragment) obj).getActivity().getApplicationContext());
        } else if (obj instanceof android.support.v4.app.Fragment) {
            requestManager = Glide.with(((android.support.v4.app.Fragment) obj).getActivity().getApplicationContext());
        } else if (obj instanceof Context) {
            requestManager = Glide.with((Context) obj);
        }
        return requestManager;
    }

    public static Bitmap getBitmap(Context context, String url) {
        return getBitmap(context, url, Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }

    public static Bitmap getBitmap(Context context, String url, int width, int height) {
//        final RequestManager requestManager = getRequestManager(obj);
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .submit(width, height)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            if (bitmap == null && url.toLowerCase().startsWith("http://static.qa.91jkys.com")) {
                String newUrl = url.replace("http://static.qa.91jkys.com",
                        "http://static-image.91jkys.com");
                bitmap = getBitmap(context, newUrl, width, height);
            }
        }

        return bitmap;
    }

    public static void loadImageByDelay(final Object obj, final String url, final WeakReference<ImageView> ivWR,
                                        final int defaultImg, final long delay) {
        ThreadPoolTools.getInstance().postMainTask(new Runnable() {
            @Override
            public void run() {
                loadImage(obj, url, ivWR, defaultImg);
            }
        }, delay);
    }

    public static void loadFromDrawable(Context context, int imageId, ImageView imageView) {
        Glide.with(context).asDrawable().load("drawable://" + imageId).into(imageView);
//        imageLoader.displayImage("drawable://" + imageId,
//                imageView);
    }

    public static void loadImage(Object withParam, File file, ImageView iv,
                                 int defaultImg) {
        RequestManager requestManager = getRequestManagerByContext(withParam);
        if (requestManager != null) {
            RequestOptions options = getCommonOptions(defaultImg);
            requestManager.load(file).apply(options).into(iv);
        }
    }

    public static void loadImage(Object withParam, File file, ImageView iv,
                                 RequestOptions options) {
        RequestManager requestManager = getRequestManagerByContext(withParam);
        if (requestManager != null) {
            requestManager.load(file).apply(options).into(iv);
        }
    }

    public static void loadImage(Object withParam, String url, Target target,
                                 int defaultImg) {
        RequestManager requestManager = getRequestManagerByContext(withParam);
        if (requestManager != null) {
            RequestOptions options = getCommonOptions(defaultImg);
            requestManager.load(url).apply(options).into(target);
        }
    }

    public static void loadImage(Object withParam, String url, Target target,
                                 RequestOptions options) {
        RequestManager requestManager = getRequestManagerByContext(withParam);
        if (requestManager != null) {
            requestManager.load(url).apply(options).into(target);
        }
    }

    public static void loadImage(Object withParam, String url, ImageView iv) {
        loadImage(withParam, url, new WeakReference<ImageView>(iv), -1);
    }

    public static void loadImage(Object withParam, String url, WeakReference<ImageView> ivWR) {
        loadImage(withParam, url, ivWR, -1);
    }

    public static void loadImage(Object withParam, String url, ImageView iv, int defaultImg) {
        RequestOptions options = getCommonOptions(defaultImg);
        loadImage(withParam, url, new WeakReference<ImageView>(iv), options);
    }

    public static void loadImage(Object withParam, String url, WeakReference<ImageView> ivWR, int defaultImg) {
        RequestOptions options = getCommonOptions(defaultImg);
        loadImage(withParam, url, ivWR, options);
    }

    public static void loadImageRounded(Object withParam, String url, ImageView iv,
                                        int defaultImg,
                                        int roundingRadius) {
        RequestOptions options = getRoundedOptions(defaultImg, roundingRadius);
        loadImage(withParam, url, new WeakReference<ImageView>(iv), options);
    }

    /**
     * 加载圆角图片
     *
     * @param withParam      Activity FragmentActivity Fragment Context View
     * @param url
     * @param ivWR
     * @param defaultImg
     * @param roundingRadius
     */
    public static void loadImageRounded(Object withParam, String url, WeakReference<ImageView> ivWR,
                                        int defaultImg,
                                        int roundingRadius) {
        RequestOptions options = getRoundedOptions(defaultImg, roundingRadius);
        loadImage(withParam, url, ivWR, options);
    }

    public static void loadImageRounded(Object withParam, String url, ImageView iv,
                                        RequestOptions options, int roundingRadius) {
        options.transform(new RoundedCorners(roundingRadius));
        loadImage(withParam, url, new WeakReference<ImageView>(iv), options);
    }

    public static void loadImageRounded(Object withParam, String url, WeakReference<ImageView> ivWR,
                                        RequestOptions options, int roundingRadius) {
        options.transform(new RoundedCorners(roundingRadius));
        loadImage(withParam, url, ivWR, options);
    }

    public static void clear(Object withParam, ImageView iv) {
        RequestManager requestManager = getRequestManager(withParam);
        if (requestManager != null) {
            requestManager.clear(iv);
        }
    }

    public static void loadImage(Object withParam, final String url, final WeakReference<ImageView> ivWR,
                                 final RequestOptions options) {

        if (url.toLowerCase().startsWith("http://static.qa.91jkys.com")) {
            loadImage(withParam, url, ivWR, options, true);
        } else {
            loadImage(withParam, url, ivWR, options, false);
        }

    }

    public static class MyGlideUrl extends GlideUrl {

//        private String mUrl;

        public MyGlideUrl(String url) {
            super(url);
//            mUrl = new String(url);
        }

        @Override
        public String getCacheKey() {
            return super.getCacheKey();
//            return mUrl.replace("http://static.qa.91jkys.com", "")
//                    .replace("http://static-image.91jkys.com", "");
        }
    }

    /**
     * 加载图片
     *
     * @param withParam   Activity FragmentActivity Fragment Context View
     * @param url
     * @param ivWR
     * @param options
     * @param isRecursion 是否递归
     */
    private static void loadImage(final Object withParam, final String url, final WeakReference<ImageView> ivWR,
                                  final RequestOptions options, final boolean isRecursion) {
        RequestManager requestManager = getRequestManagerByContext(withParam);
        if (requestManager != null) {
            requestManager.load(new MyGlideUrl(url)).apply(options).into(new ViewTarget<ImageView, Drawable>(ivWR.get()) {
//                @Override
//                public void onLoadStarted(@Nullable Drawable placeholder) {
//                    super.onLoadStarted(placeholder);
//                    ImageView iv = ivWR.get();
//                    if (iv == null)
//                        return;
//                    //会触发GridView调用getView,getView又调用loadImage,如果getview里面不做处理(判断与上次加载同一张图片,则不加载),
//                    // 会陷入循环,导致替换前缀http://static-image.91jkys.com的请求不生效,故暂时该方法不加
//
//                    iv.setImageDrawable(placeholder);
//                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    ImageView iv = ivWR.get();
                    if (iv == null)
                        return;
                    if (isRecursion) {
                        ThreadPoolTools.getInstance().postMainTask(new Runnable() {
                            @Override
                            public void run() {
                                String newUrl = url.replace("http://static.qa.91jkys.com",
                                        "http://static-image.91jkys.com");
                                loadImage(withParam, newUrl, ivWR, options, false);
                            }
                        });

                    } else {
                        int errorholderId = options.getErrorId();
                        if (errorholderId > 0)
                            iv.setImageResource(errorholderId);
                    }
                }

                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition transition) {
                    ImageView iv = ivWR.get();
                    if (iv == null)
                        return;
                    iv.setImageDrawable(resource);
                }

//                private boolean isMatch(Object tag, String url) {
//                    if (tag == null)
//                        return true;
//                    if (!(tag instanceof String))
//                        return true;
//                    String tagStr = (String) tag;
//                    String urlPre1 = "http://static.qa.91jkys.com";
//                    String urlPre2 = "http://static-image.91jkys.com";
//                    if (url.toLowerCase().startsWith(urlPre1) || url.toLowerCase().startsWith(urlPre2)) {
//                        String tagStrSuffix = tagStr.replace(urlPre1, "").replace(urlPre2, "");
//                        String urlSuffix = url.replace(urlPre1, "").replace(urlPre2, "");
//                        if (tagStrSuffix.equals(urlSuffix)) {
//                            return true;
//                        }
//                    } else {
//                        if (tagStr.equals(url)) {
//                            return true;
//                        }
//                    }
//                    return false;
//                }


            });
        }
    }


//    /**
//     * 加载图片
//     *
//     * @param withParam   Activity FragmentActivity Fragment Context View
//     * @param url
//     * @param ivWR
//     * @param options
//     * @param isRecursion 是否递归
//     */
//    private static void loadImage(final Object withParam, final String url, final WeakReference<ImageView> ivWR,
//                                  final RequestOptions options, final boolean isRecursion) {
//        RequestManager requestManager = getRequestManagerByContext(withParam);
//        if (requestManager != null) {
//            requestManager.asBitmap().apply(options).load(url).into(new SimpleTarget<Bitmap>() {
//                private boolean isMatch(Object tag, String url) {
//                    if (tag == null)
//                        return true;
//                    if (!(tag instanceof String))
//                        return true;
//                    String tagStr = (String) tag;
//                    String urlPre1 = "http://static.qa.91jkys.com";
//                    String urlPre2 = "http://static-image.91jkys.com";
//                    if (url.toLowerCase().startsWith(urlPre1) || url.toLowerCase().startsWith(urlPre2)) {
//                        String tagStrSuffix = tagStr.replace(urlPre1, "").replace(urlPre2, "");
//                        String urlSuffix = url.replace(urlPre1, "").replace(urlPre2, "");
//                        if (tagStrSuffix.equals(urlSuffix)) {
//                            return true;
//                        }
//                    } else {
//                        if (tagStr.equals(url)) {
//                            return true;
//                        }
//                    }
//                    return false;
//                }
//
//                @Override
//                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                    ImageView iv = ivWR.get();
//                    if (iv == null)
//                        return;
//                    Object tag = iv.getTag(R.id.glide_iv_tag);
//                    if (isMatch(tag, url)) {
//                        iv.setImageBitmap(resource);
//                    }
//                }
//
//                @Override
//                public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                    super.onLoadFailed(errorDrawable);
//                    ImageView iv = ivWR.get();
//                    if (iv == null)
//                        return;
//                    Object tag = iv.getTag(R.id.glide_iv_tag);
//                    if (isMatch(tag, url)) {
//                        if (isRecursion) {
//                            String newUrl = url.replace("http://static.qa.91jkys.com",
//                                    "http://static-image.91jkys.com");
//                            loadImage(withParam, newUrl, ivWR, options, false);
//                        } else {
//                            int errorholderId = options.getErrorId();
//                            if (errorholderId > 0)
//                                iv.setImageResource(errorholderId);
//                        }
//
//                    }
//                }
//            });
//        }
//    }


}
