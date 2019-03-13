package com.gallery.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Caliber Fashion on 11/29/2016.
 */

public class LoadPhotoAsync extends AsyncTask<Long, Integer, List<PhotoItem>> {
    public static final String TAG = LoadPhotoAsync.class.getName();
    private static LoadPhotoAsync instance;
    private ItemLoadListener photoItemLoadListener;
    private Context context;

    private LoadPhotoAsync(Context context, ItemLoadListener listener) {
        this.context = context;
        photoItemLoadListener = listener;
    }

    public static void loadAlbumAsync(Context context, ItemLoadListener itemLoadListener) {
        if (!(instance == null || instance.getStatus() == Status.FINISHED)) {
            instance.cancel(true);
            instance = null;
        }
        instance = new LoadPhotoAsync(context, itemLoadListener);
        instance.execute(new Long[0]);
    }

    public static void loadAlbumPhotoAsync(Context context, long id, ItemLoadListener itemLoadListener) {
        if (!(instance == null || instance.getStatus() == Status.FINISHED)) {
            instance.cancel(true);
            instance = null;
        }
        instance = new LoadPhotoAsync(context, itemLoadListener);
        instance.execute(new Long[]{Long.valueOf(id)});
    }

    public static void destroy() {
        try {
            if (instance != null && instance.getStatus() != Status.FINISHED) {
                instance.cancel(true);
                instance = null;
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected List<PhotoItem> doInBackground(Long... params) {
        if (params == null || params.length == 0) {
            return getPhotoAlbum();
        }
        return getGalleryPhotos(params[0].longValue());
        //return getPhotoAlbum();
    }

    @Override
    protected void onPostExecute(List<PhotoItem> photoItems) {
        super.onPostExecute(photoItems);
        //Toast.makeText(AppUtils._appContext,"PhotoS:"+photoItems.size(),Toast.LENGTH_SHORT).show();
        try {
            this.photoItemLoadListener.onPhotoLoaded_LoadPhotoAsync(photoItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PhotoItem> getPhotoAlbum() {
        List<PhotoItem> albumList = new ArrayList();
        try {
            String orderBy = "bucket_id";
            Cursor imagecursor = this.context.getContentResolver().query
                    (MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            new String[]{"bucket_id", "bucket_display_name", "_data"}, null, null, "bucket_id");
            long id = -1;
            if (imagecursor != null && imagecursor.getCount() > 0) {
                while (imagecursor.moveToNext()) {
                    long LocatlId = imagecursor.getLong(0);
                    if (id != LocatlId) {
                        PhotoItem item = new PhotoItem();
                        item.setId(LocatlId);
                        item.setName(imagecursor.getString(1));
                        item.setPath(imagecursor.getString(2));
                        id = LocatlId;
                        albumList.size();
                        albumList.add(item);
                    }
                }
                albumList.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getPhotoAlbum: " + e.getMessage());
        }
        Collections.sort(albumList, new Comparator<PhotoItem>() {
            @Override
            public int compare(PhotoItem o1, PhotoItem o2) {
                return o1.getFileName().compareTo(o2.getFileName());
            }
        });
        return albumList;
    }

    public ArrayList<PhotoItem> getGalleryPhotos(long albumId) {
        ArrayList<PhotoItem> galleryList = new ArrayList();
        try {
            String orderBy = "_id DESC ";
            Cursor imagecursor = this.context.getContentResolver().query
                    (MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            new String[]{"_data", "mime_type", "_display_name",
                                    "bucket_display_name", "_size", "_id", "bucket_id"},
                            "bucket_id='" + albumId + "'", null, "_id DESC ");
            if (imagecursor != null && imagecursor.getCount() > 0) {
                while (imagecursor.moveToNext()) {
                    PhotoItem item = new PhotoItem();
                    item.setId(imagecursor.getLong(imagecursor.getColumnIndex("_id")));
                    item.setName(imagecursor.getString(imagecursor.getColumnIndex("_display_name")));
                    item.setPath(imagecursor.getString(imagecursor.getColumnIndex("_data")));
                    try {
                        if (new File(item.getPath()).exists()) {
                            galleryList.add(item);
                        }
                    } catch (Exception e) {
                    }
                }
            }
            imagecursor.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return galleryList;
    }

    public interface ItemLoadListener {
        public void onPhotoLoaded_LoadPhotoAsync(List<PhotoItem> list) throws Exception;
    }

}
