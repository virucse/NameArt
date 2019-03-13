package nonworkingcode.grid.mcacheloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.formationapps.nameart.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nonworkingcode.grid.util.CollageUtil;
import nonworkingcode.grid.util.ImageUtils;

/*

take a reference from
http://www.technotalkative.com/android-asynchronous-image-loading-in-listview/
 */
public class ImageLoaderInMemory {
    static ImageLoaderInMemory _instance;
    final int stub_id;
    private ExecutorService executorService;
    private FileCache fileCache;
    private Map<View, String> imageViews;
    private Context mContex;
    private MemoryCache memoryCache;
    private Bitmap tempBitmap;

    private ImageLoaderInMemory(Context context) {
        this.memoryCache = new MemoryCache();
        this.imageViews = Collections.synchronizedMap(new WeakHashMap());
        this.stub_id = R.mipmap.icon_72;
        this.mContex = context;
        this.fileCache = new FileCache(context);
        this.executorService = Executors.newFixedThreadPool(5);
    }

    private ImageLoaderInMemory(Context context, int resId) {
        this.memoryCache = new MemoryCache();
        this.imageViews = Collections.synchronizedMap(new WeakHashMap());
        this.mContex = context;
        this.stub_id = resId;
        this.fileCache = new FileCache(context);
        this.executorService = Executors.newFixedThreadPool(5);
    }

    public static void init(Context context) {
        _instance = new ImageLoaderInMemory(context);
    }

    public static ImageLoaderInMemory getInstance() {
        if (_instance == null) {
            _instance = new ImageLoaderInMemory(CollageUtil._appContext);
        }
        return _instance;
    }

    public void setTempBitmap(Bitmap b) {
        tempBitmap = b;
    }

    /*public void displayGpuFilter(GPUImageFilterTools.FilterType type,ImageView imageView){
        String key=type.name();
        DisplayImage(key,imageView);
    }*/
    public void DisplayImage(String uri, View imageView) {
        this.imageViews.put(imageView, uri);
        Bitmap bitmap = this.memoryCache.get(uri);
        if (bitmap == null) {
            queuePhoto(uri, imageView);
            if (imageView instanceof ImageView) {
                ((ImageView) imageView).setImageResource(this.stub_id);
            } else if (imageView instanceof ViewSwitcher) {
                ((ViewSwitcher) imageView).setDisplayedChild(0);
            }
        } else if (imageView instanceof ImageView) {
            ((ImageView) imageView).setImageBitmap(bitmap);
        } else if (imageView instanceof ViewSwitcher) {
            ((ViewSwitcher) imageView).setDisplayedChild(1);
            View v = ((ViewSwitcher) imageView).getCurrentView();
            if (v instanceof ImageView) {
                ((ImageView) v).setImageBitmap(bitmap);
            }
        }
    }

    private void queuePhoto(String uri, View imageView) {
        this.executorService.submit(new PhotosLoader(new PhotoToLoad(uri, imageView)));
    }

    private Bitmap getBitmap(String uri) {
        Bitmap bitmap = null;
        File f = this.fileCache.getFile(uri);
        Bitmap b = decodeFile(f);
        if (b != null) {
            return b;
        }
        if (uri.startsWith("http")) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(uri).openConnection();
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                conn.setInstanceFollowRedirects(true);
                InputStream is = conn.getInputStream();
                OutputStream os = new FileOutputStream(f);
                LazyUtils.CopyStream(is, os);
                os.close();
                return decodeFile(f);
            } catch (Exception ex) {
                ex.printStackTrace();
                return bitmap;
            }
        } else if (uri.startsWith("asset")) {
            try {
                return decodeFile1(uri.split(":")[1]);
            } catch (Exception e) {
                e.printStackTrace();
                return bitmap;
            }
        } else {
            try {
                return decodeFile(new File(uri));
            } catch (Exception e2) {
                e2.printStackTrace();
                return bitmap;
            }
        }
    }

    private Bitmap decodeFile(File f) {
        return ImageUtils.getinstance().loadImage(this.mContex, f, (int) 128, (int) 128);
    }

    private Bitmap decodeFile1(String fileName) {
        try {
            Options o = new Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(this.mContex.getAssets().open(fileName), null, o);
            int width_tmp = o.outWidth;
            int height_tmp = o.outHeight;
            int scale = 1;
            while (width_tmp / 2 >= 64 && height_tmp / 2 >= 64) {
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            Options o2 = new Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(this.mContex.getAssets().open(fileName), null, o2);
        } catch (IOException e) {
            return null;
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.uri)) {
            return true;
        }
        return false;
    }

    public void clearCache() {
        this.memoryCache.clear();
        this.fileCache.clear();
        destroyTempBitmap(tempBitmap);
    }

    private void destroyTempBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            //bitmap.recycle();
        }
    }

    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (!imageViewReused(this.photoToLoad)) {
                if (bitmap != null) {
                    if (photoToLoad.imageView instanceof ImageView) {
                        ((ImageView) photoToLoad.imageView).setImageBitmap(bitmap);
                    } else if (photoToLoad.imageView instanceof ViewSwitcher) {
                        ((ViewSwitcher) photoToLoad.imageView).setDisplayedChild(1);
                        View v = ((ViewSwitcher) photoToLoad.imageView).getCurrentView();
                        if (v instanceof ImageView) {
                            ((ImageView) v).setImageBitmap(bitmap);
                        }
                    }
                } else if (photoToLoad.imageView instanceof ImageView) {
                    ((ImageView) photoToLoad.imageView).setImageResource(stub_id);
                } else if (photoToLoad.imageView instanceof ViewSwitcher) {
                    ((ViewSwitcher) photoToLoad.imageView).setDisplayedChild(0);
                }
            }
        }
    }

    private class PhotoToLoad {
        public View imageView;
        public String uri;

        public PhotoToLoad(String u, View i) {
            this.uri = u;
            this.imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        public void run() {
            if (!imageViewReused(this.photoToLoad)) {
                Bitmap bmp = getBitmap(this.photoToLoad.uri);
                /*if(bmp==null){
                    //that would be GPUFilter
                    GPUImageFilterTools.FilterType type=GPUImageFilterTools.FilterType.valueOf(photoToLoad.uri+"");
                    GPUImageFilter filter=GPUImageFilterTools.createFilterForType(BaseFragmentActivity.appContext, type);
                    GPUImageRenderer renderer=new GPUImageRenderer(filter);
                    Bitmap bb=tempBitmap.copy(Bitmap.Config.ARGB_8888,true);
                    renderer.setImageBitmap(bb,true);
                    PixelBuffer pixelBuffer=new PixelBuffer(bb.getWidth(),bb.getHeight());
                    pixelBuffer.setRenderer(renderer);
                    bmp=pixelBuffer.getBitmap();
                    renderer.deleteImage();
                    pixelBuffer.destroy();
                    destroyTempBitmap(bb);

                }*/
                memoryCache.put(this.photoToLoad.uri, bmp);
                if (!imageViewReused(this.photoToLoad)) {
                    ((Activity) this.photoToLoad.imageView.getContext()).runOnUiThread(new BitmapDisplayer(bmp, this.photoToLoad));
                }
            }
        }
    }
}
