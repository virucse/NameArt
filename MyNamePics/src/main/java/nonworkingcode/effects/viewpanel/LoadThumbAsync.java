package nonworkingcode.effects.viewpanel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.formationapps.nameart.helper.AppUtils;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRenderer;
import jp.co.cyberagent.android.gpuimage.PixelBuffer;
import nonworkingcode.effects.custuminterface.IOnThumbListener;
import nonworkingcode.effects.filters.GPUImageFilterTools;

public class LoadThumbAsync extends AsyncTask<Uri, Integer, Boolean> {
    private static final String TAG = LoadThumbAsync.class.getName();
    private static LoadThumbAsync _instance;
    private final Bitmap bitmap;
    private final List<Bitmap> bitmaps;
    private final List<GPUImageFilterTools.FilterType> list;
    private final IOnThumbListener listener;
    private final Activity mActivity;
    private final int mode;
    private final int size;
    private ProgressDialog pd;

    private LoadThumbAsync(IOnThumbListener listener, Bitmap bitmap, List<GPUImageFilterTools.FilterType> list) {
        this.bitmaps = new ArrayList();
        this.mActivity = listener.getActivity();
        this.listener = listener;
        this.list = list;
        this.bitmap = bitmap;
        this.mode = -1;
        this.size = list.size();
        init();
    }

    private LoadThumbAsync(IOnThumbListener listener, Bitmap bitmap, int mode, int size) {
        this.bitmaps = new ArrayList();
        this.mActivity = listener.getActivity();
        this.listener = listener;
        this.list = null;
        this.bitmap = bitmap;
        this.mode = mode;
        this.size = size;
        init();
    }

    public static void loadThumbnails(Bitmap bitmap, int mode, int size, IOnThumbListener activity) {
        if (_instance != null) {
            if (_instance.getStatus() != Status.FINISHED) {
                _instance.cancel(true);
            }
            _instance = null;
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            int sizeY = (int) AppUtils.dpToPx(activity.getActivity(), 70.0f);
            _instance = new LoadThumbAsync(activity,
                    Bitmap.createScaledBitmap(bitmap, (int) ((((float) bitmap.getWidth()) / (((float) bitmap.getHeight()) * 1.0f)) * ((float) sizeY)), sizeY, true),
                    mode, size);
            _instance.execute(new Uri[0]);
        }
    }

    public static void loadThumbnails(Bitmap bitmap, List<GPUImageFilterTools.FilterType> list, IOnThumbListener activity) {
        if (_instance != null) {
            if (_instance.getStatus() != Status.FINISHED) {
                _instance.cancel(true);
            }
            _instance = null;
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            int sizeY = (int) AppUtils.dpToPx(activity.getActivity(), 70.0f);
            _instance = new LoadThumbAsync(activity, Bitmap.createScaledBitmap(bitmap,
                    (int) ((((float) bitmap.getWidth()) / (((float) bitmap.getHeight()) * 1.0f)) * ((float) sizeY)), sizeY, true),
                    list);
            _instance.execute(new Uri[0]);
        }
    }

    public static void clear() {
        if (_instance != null) {
            if (_instance.getStatus() != Status.FINISHED) {
                _instance.cancel(true);
            }
            for (Bitmap btm : _instance.bitmaps) {
                if (!(btm == null || btm.isRecycled())) {
                    btm.recycle();
                }
            }
            _instance.bitmaps.clear();
            _instance = null;
            System.gc();
        }
    }

    public static void cancel() {
        if (_instance != null) {
            if (_instance.getStatus() != Status.FINISHED) {
                _instance.cancel(true);
            }
            _instance = null;
        }
    }

    private void init() {
        pd = new ProgressDialog(mActivity);
        pd.setMessage("Please Wait!");
    }

    protected void onPreExecute() {
        pd.show();
        super.onPreExecute();
    }

    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    protected Boolean doInBackground(Uri... params) {
        for (Bitmap btm : this.bitmaps) {
            if (!(btm == null || btm.isRecycled())) {
                btm.recycle();
            }
        }
        bitmaps.clear();
        System.gc();
        if (bitmap == null || bitmap.isRecycled()) {
            return Boolean.valueOf(false);
        }
        GPUImageFilter filter;
        this.bitmaps.add(this.bitmap);
        if (this.list != null) {
            filter = GPUImageFilterTools.createFilterForType(mActivity, list.get(1));
        } else {
            filter = getFilter(1);
        }
        GPUImageRenderer renderer = new GPUImageRenderer(filter);
        renderer.setImageBitmap(bitmap, false);
        filter.destroy();
        PixelBuffer buffer = new PixelBuffer(bitmap.getWidth(), bitmap.getHeight());
        buffer.setRenderer(renderer);
        for (int i = 1; i < this.size; i++) {
            try {
                GPUImageFilter filter1;
                if (this.list != null) {
                    filter1 = GPUImageFilterTools.createFilterForType(this.mActivity, this.list.get(i));
                } else {
                    filter1 = getFilter(i);
                }
                if (filter1 != null) {
                    renderer.setFilter(filter1);
                    bitmaps.add(buffer.getBitmap());
                    filter1.destroy();
                }
            } catch (Exception e) {
                Log.d(TAG, "doInBackground: " + e.getMessage());
            }
        }
        renderer.deleteImage();
        buffer.destroy();
        return Boolean.valueOf(true);
    }

    private GPUImageFilter getFilter(int position) {
        if (position == 0) {
            return new GPUImageFilter();
        }
        String resName = "imagess/overlay/overlay_%d.jpg";
        if (position == 2) {
            resName = "imagess/overlay/overlay_%d.png";
        }
        int mode = 3;
        if (this.mode == 1) {
            resName = "imagess/texture/texture_%d.jpg";
            mode = GPUImageFilterTools.textureModes[position];
        }
        return GPUImageFilterTools.getFilter(this.mActivity, String.format(resName, new Object[]{Integer.valueOf(position)}), mode);
    }

    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        pd.dismiss();
        if (this.listener != null) {
            this.listener.onThumbLoaded(this.bitmaps);
        }
    }
}
