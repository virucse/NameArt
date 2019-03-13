package com.gallery.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.editor.activity.EditorActivity;
import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BaseActivity;
import com.formationapps.nameart.helper.AdsHelper;
import com.formationapps.widget.HorizontalListView;
import com.gallery.utils.GalleryUtil;
import com.gallery.utils.ImageUtils;
import com.gallery.utils.LoadPhotoAsync;
import com.gallery.utils.PhotoItem;
import com.gif.GIFActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nonworkingcode.grid.activity.CollageActivity;
import nonworkingcode.grid.util.CollageConst;
import nonworkingcode.grid.util.CollageUtil;
import nonworkingcode.pip.activity.PIPActivity;

/**
 * Created by Caliber Fashion on 11/28/2016.
 */

public class ImageSelectAcivity extends BaseActivity {
    private static int grid_size;
    private static int mSelectedAlbumIndex;
    private static long selectedAlbumId;

    static {
        grid_size = 0;
        selectedAlbumId = 0;
        mSelectedAlbumIndex = 0;
    }

    private int mMaxImages;
    private int replaceIndex;
    private int appType;
    private ListView mListAlbum;
    private GridView mGridPhotos;
    private TextView mTvCurrentCount, mTvMaxCount;
    private HorizontalListView mHlv;
    private ImageAdapter mAlbumAdapter, mGridViewAdapter, mSelectedAdapter;
    private AdapterView.OnItemClickListener mGridPhotosItemClickListener = new
            AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (appType == GalleryUtil.APP_TYPE_COLLAGE) {
                        if (replaceIndex >= 0) {
                            mSelectedAdapter.replaceData((PhotoItem)
                                    mGridViewAdapter.getItem(position));
                            updateText();
                        } else {
                            PhotoItem photoItem = (PhotoItem) mGridViewAdapter.getItem(position);
                            if (mSelectedAdapter == null) {
                                mSelectedAdapter = new ImageAdapter(ImageSelectAcivity.this,
                                        GalleryUtil.SELECTED_TYPE);
                                mHlv.setAdapter(mSelectedAdapter);
                            }
                            if (mSelectedAdapter.getCount() < mMaxImages) {
                                mSelectedAdapter.add(photoItem);
                                mSelectedAdapter.notifyDataSetChanged();
                                updateText();
                            }
                        }

                    } else if (appType == GalleryUtil.APP_TYPE_PIP) {
                        PhotoItem photoItem = (PhotoItem) mGridViewAdapter.getItem(position);
                        Uri uri = Uri.fromFile(new File(photoItem.getPath()));
                        startActivity(new Intent(ImageSelectAcivity.this,
                                PIPActivity.class).setData(uri));
                        finish();
                    } else if (appType == GalleryUtil.APP_TYPE_EDITOR) {
                        PhotoItem photoItem = (PhotoItem) mGridViewAdapter.getItem(position);
                        Uri uri = Uri.fromFile(new File(photoItem.getPath()));
                        startActivity(new Intent(ImageSelectAcivity.this,
                                EditorActivity.class).setData(uri));
                        finish();
                    } else if (appType == GalleryUtil.APP_TYPE_DEFAULT) {
                        //this is for startActivityForResult but not from Collage module.
                        PhotoItem photoItem = (PhotoItem) mGridViewAdapter.getItem(position);
                        Uri uri = Uri.fromFile(new File(photoItem.getPath()));
                        Intent intent = new Intent();
                        intent.setData(uri);
                        setResult(RESULT_OK, intent);
                        finish();
                    }else  if (appType == GalleryUtil.APP_TYPE_GIF) {
                        if (replaceIndex >= 0) {
                            mSelectedAdapter.replaceData((PhotoItem)
                                    mGridViewAdapter.getItem(position));
                            updateText();
                        } else {
                            PhotoItem photoItem = (PhotoItem) mGridViewAdapter.getItem(position);
                            if (mSelectedAdapter == null) {
                                mSelectedAdapter = new ImageAdapter(ImageSelectAcivity.this,
                                        GalleryUtil.SELECTED_TYPE);
                                mHlv.setAdapter(mSelectedAdapter);
                            }
                            if (mSelectedAdapter.getCount() < mMaxImages) {
                                mSelectedAdapter.add(photoItem);
                                mSelectedAdapter.notifyDataSetChanged();
                                updateText();
                            }
                        }
                    }

                }
            };
    private AdapterView.OnItemClickListener mListAlbumItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSelectedAlbumIndex = position;
            long albumId = mAlbumAdapter.getItemId(position);
            loadPhotoItem(albumId);
            mAlbumAdapter.notifyDataSetChanged();
        }
    };

    public ImageSelectAcivity() {
        mMaxImages = 10;
        replaceIndex = -1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.start_bottom_in,R.anim.start_bottom_out);
        setContentView(R.layout.image_select_activity);
        logAnalyticEvent("GALLERY_ACTIVITY");

        GalleryUtil.onCreate(this);

        //load admob banner
        AdsHelper.loadAdmobBanner(this, (LinearLayout) findViewById(R.id.ad_container));

        appType = getIntent().getIntExtra(GalleryUtil.EXTRA_TYPE, GalleryUtil.APP_TYPE_DEFAULT);
        grid_size = (int) (GalleryUtil.screenWidth / 4.1f);
        initUi();
       // initUiGif();
        if (appType != GalleryUtil.APP_TYPE_COLLAGE) {
            findViewById(R.id.show_im).setVisibility(View.GONE);
        } else if (getIntent().hasExtra(GalleryUtil.EXTRA_INDEX)) {
            replaceIndex = getIntent().getIntExtra(GalleryUtil.EXTRA_INDEX, -1);
        }
        if (replaceIndex >= 0) {
            mMaxImages = 1;
        } else {
            if (appType == GalleryUtil.APP_TYPE_GIF){
                mMaxImages = 20;
            }else {
                mMaxImages = 10;
            }

        }
        if (this.appType == GalleryUtil.APP_TYPE_COLLAGE) {
            this.mSelectedAdapter = new ImageAdapter(this, new ArrayList(), GalleryUtil.SELECTED_TYPE);
            if (replaceIndex >= 0) {
                String file = CollageConst.collageIds[replaceIndex].getPath();
                PhotoItem item = new PhotoItem();
                item.setPath(file);
                this.mSelectedAdapter.add(item);
                updateText();
            }
            this.mHlv.setAdapter(this.mSelectedAdapter);
        }
        if (this.appType == GalleryUtil.APP_TYPE_GIF) {
            findViewById(R.id.show_im).setVisibility(View.VISIBLE);
            this.mSelectedAdapter = new ImageAdapter(this, new ArrayList(), GalleryUtil.SELECTED_TYPE);
            if (replaceIndex >= 0) {
                String file = CollageConst.collageIds[replaceIndex].getPath();
                PhotoItem item = new PhotoItem();
                item.setPath(file);
                this.mSelectedAdapter.add(item);
                updateText();
            }
            this.mHlv.setAdapter(this.mSelectedAdapter);
        }
        setUpData();
    }

    private void initUi() {
        mListAlbum = (ListView) findViewById(R.id.lv_ip_album);
        mGridPhotos = (GridView) findViewById(R.id.gv_ip_photo);
        this.mTvCurrentCount = (TextView) findViewById(R.id.tv_ip_count);
        this.mTvMaxCount = (TextView) findViewById(R.id.tv_ip_max);
        this.mHlv = (HorizontalListView) findViewById(R.id.hlv_ip);
        this.mListAlbum.setLayoutParams(new RelativeLayout.LayoutParams(grid_size + 10, RelativeLayout.LayoutParams.MATCH_PARENT));
        if (appType != GalleryUtil.APP_TYPE_COLLAGE ) {
            findViewById(R.id.show_im).setVisibility(View.GONE);
            mListAlbum.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            mGridPhotos.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            mListAlbum.invalidate();
            mGridPhotos.invalidate();
        }
        GalleryUtil.setImage((ImageButton) findViewById(R.id.ib_ip_back), R.mipmap.ic_cross);
        GalleryUtil.setImage((ImageButton) findViewById(R.id.ib_ip_clear), R.mipmap.ic_clear_all);
    }

    private void setUpData() {
        mTvCurrentCount.setText(String.valueOf(0));
        TextView textView = this.mTvMaxCount;
        Object[] objArr = new Object[1];
        objArr[0] = Integer.valueOf(this.mMaxImages);
        textView.setText(getString(R.string.max_to_d_photos, mMaxImages));
        //initially load all albums from phone
        LoadPhotoAsync.loadAlbumAsync(this, new LoadPhotoAsync.ItemLoadListener() {
            @Override
            public void onPhotoLoaded_LoadPhotoAsync(List<PhotoItem> list) throws Exception {
                loadPhoto(list, GalleryUtil.ALBUM_TYPE);
            }
        });
    }

    public void onNextClick(View view) {
        if (appType == GalleryUtil.APP_TYPE_COLLAGE) {
            int count = mSelectedAdapter == null ? 0 : mSelectedAdapter.getCount();
            if (count > 0) {
                List<PhotoItem> list = mSelectedAdapter.getList();
                if (replaceIndex >= 0) {
                    CollageConst.collageIds[replaceIndex] = Uri.fromFile(new File((list.get(0)).getPath()));
                    CollageConst.collageView.update(ImageUtils.getinstance().loadBitmap(getApplicationContext(),
                            CollageConst.collageIds[replaceIndex], CollageConst.getImageSize()), replaceIndex);
                    CollageConst.effectApplied[replaceIndex] = Boolean.valueOf(false);
                    Intent in = new Intent();
                    in.putExtra(CollageUtil.EXTRA_INDEX, replaceIndex);
                    setResult(RESULT_OK, in);
                    onBackPressed();
                } else {
                    int i = 0;
                    for (PhotoItem item : list) {
                        CollageConst.collageIds[i] = Uri.fromFile(new File(item.getPath()));
                        CollageConst.collageBitmaps[i] = ImageUtils.getinstance().loadBitmap(getApplicationContext(),
                                CollageConst.collageIds[i], list.size());
                        CollageConst.effectApplied[i] = Boolean.valueOf(false);
                        i += 1;
                    }
                    finish();
                    startActivity(new Intent(this, CollageActivity.class));
                }
            } else {
                Toast.makeText(this, "Please select at least one photo",
                        Toast.LENGTH_LONG).show();
            }
        }else if (appType == GalleryUtil.APP_TYPE_GIF) {
            int count = mSelectedAdapter == null ? 0 : mSelectedAdapter.getCount();
            if (count > 0) {
                List<PhotoItem> list = mSelectedAdapter.getList();
                if (replaceIndex>=0){
                    onBackPressed();
                }
                int i = 0;
                    for (PhotoItem item : list) {
                        i += 1;
                    }
                    Intent ingif = new Intent(this,GIFActivity.class);
                   fullList = list;
                 //  list.clear();
               // mTvCurrentCount.setText(String.valueOf(0));
                finish();
                startActivity(ingif);

                }
            } else {
                Toast.makeText(this, "Please select at least one photo",
                        Toast.LENGTH_LONG).show();
            }
        }
    public static List<PhotoItem> fullList;

    private void loadPhoto(List<PhotoItem> list, int type) {
        if (list != null && !list.isEmpty()) {
            if (type == GalleryUtil.ALBUM_TYPE) {
                mAlbumAdapter = new ImageAdapter(this, list, GalleryUtil.ALBUM_TYPE);
                mListAlbum.setAdapter(mAlbumAdapter);

                mSelectedAlbumIndex = 0;
                loadPhotoItem(list.get(mSelectedAlbumIndex).getId());
                mListAlbum.setOnItemClickListener(mListAlbumItemClickListener);
            } else if (type == GalleryUtil.PHOTO_TYPE) {
                if (mGridViewAdapter == null) {
                    mGridViewAdapter = new ImageAdapter(this, list, GalleryUtil.PHOTO_TYPE);
                } else {
                    mGridViewAdapter.addAll(list);
                }
                mGridPhotos.setAdapter(mGridViewAdapter);
                mGridPhotos.setOnItemClickListener(mGridPhotosItemClickListener);
            }
        }
    }

    private void updateText() {
        this.mTvCurrentCount.setText(String.valueOf(mSelectedAdapter.getCount()));
    }

    private void loadPhotoItem(long itemId) {
        selectedAlbumId = itemId;
        //LoadImageProcess.initLoadGalleryAsync(this, itemId, new C05994());
        LoadPhotoAsync.loadAlbumPhotoAsync(this, itemId, new LoadPhotoAsync.ItemLoadListener() {
            @Override
            public void onPhotoLoaded_LoadPhotoAsync(List<PhotoItem> list) throws Exception {
                loadPhoto(list, GalleryUtil.PHOTO_TYPE);
            }
        });
    }

    public void onClick(View v) {
        if (v.getId() == R.id.ib_ip_clear) {
            if(mSelectedAdapter!=null)mSelectedAdapter.clearAll();
        }
    }

    @Override
    public void onBackPressed() {
        // overridePendingTransition(17432576, R.anim.start_bottom_out);
        finish();
    }


    private static class ViewHolder {
        final View vs;

        ViewHolder(View v, int id) {
            this.vs = v.findViewById(id);
            this.vs.setLayoutParams(new LinearLayout.LayoutParams(grid_size, grid_size));
        }
    }

    private static class ViewAlbumHolder extends ViewHolder {
        final TextView tv;

        ViewAlbumHolder(View v) {
            super(v, R.id.im_row_album_adapter_item_album);
            this.tv = (TextView) v.findViewById(R.id.tv_row_album_adapter_item_album);
        }
    }

    private static class ViewSelectHolder extends ViewHolder {
        final ImageButton imgDel;

        ViewSelectHolder(View v) {
            super(v, R.id.im_row_album_adapter_item_scroll);
            this.vs.setLayoutParams(new FrameLayout.LayoutParams(grid_size, grid_size));
            this.imgDel = (ImageButton) v.findViewById(R.id.im_row_delete_adapter_item_scroll);
            GalleryUtil.setImage(this.imgDel, R.mipmap.ic_clear, Color.BLACK);
        }
    }

    class ImageAdapter extends BaseAdapter {
        private ImageSelectAcivity activity;
        private List<PhotoItem> list;
        private int type;
        private LayoutInflater mInflater;

        public ImageAdapter(ImageSelectAcivity activity, List<PhotoItem> list, int type) {
            this.activity = activity;
            this.list = list;
            this.type = type;
            mInflater = this.activity.getLayoutInflater();
        }

        public ImageAdapter(ImageSelectAcivity activity, int type) {
            this.activity = activity;
            this.list = new ArrayList<PhotoItem>();
            this.type = type;
            mInflater = this.activity.getLayoutInflater();
        }

        public void clearAll() {
            list.clear();
            notifyDataSetChanged();
        }

        public void addAll(List<PhotoItem> imgItems) {
            list.clear();
            list.addAll(imgItems);
            notifyDataSetChanged();
        }

        public void add(PhotoItem item) {
            this.list.add(item);
            notifyDataSetChanged();
        }

        public void replaceData(PhotoItem item) {
            list.clear();
            list.add(item);
            notifyDataSetChanged();
        }

        public List<PhotoItem> getList() {
            return list;
        }

        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * Get the data item associated with the specified position in the data set.
         *
         * @param position Position of the item whose data we want within the adapter's
         *                 data set.
         * @return The data at the specified position.
         */
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        /**
         * Get the row id associated with the specified position in the list.
         *
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        @Override
        public long getItemId(int position) {
            return list.get(position).getId();
        }

        /**
         * Get a View that displays the data at the specified position in the data set. You can either
         * create a View manually or inflate it from an XML layout file. When the View is inflated, the
         * parent View (GridView, ListView...) will apply default layout parameters unless you use
         * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
         * to specify a root view and to prevent attachment to the root.
         *
         * @param position    The position of the item within the adapter's data set of the item whose view
         *                    we want.
         * @param convertView The old view to reuse, if possible. Note: You should check that this view
         *                    is non-null and of an appropriate type before using. If it is not possible to convert
         *                    this view to display the correct data, this method can create a new view.
         *                    Heterogeneous lists can specify their number of view types, so that this View is
         *                    always of the right type (see {@link #getViewTypeCount()} and
         *                    {@link #getItemViewType(int)}).
         * @param parent      The parent that this view will eventually be attached to
         * @return A View corresponding to the data at the specified position.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            View view = convertView;
            if (view == null) {
                view = this.mInflater.inflate(R.layout.gallery_image_adapter, parent, false);
                if (this.type == GalleryUtil.ALBUM_TYPE) {
                    setUpVisibility(view, R.id.adapter_item_album);
                    holder = new ViewAlbumHolder(view);
                } else if (this.type == GalleryUtil.SELECTED_TYPE) {
                    setUpVisibility(view, R.id.adapter_item_scroll);
                    holder = new ViewSelectHolder(view);
                } else {
                    //this is type_photo
                    setUpVisibility(view, R.id.adapter_item_photo);
                    holder = new ViewHolder(view, R.id.im_row_album_adapter_item_photo);
                }
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            if (this.type == GalleryUtil.ALBUM_TYPE) {
                if (mSelectedAlbumIndex == position) {
                    view.setBackgroundColor(Color.DKGRAY);
                } else {
                    view.setBackgroundColor(Color.LTGRAY);
                }
                ((ViewAlbumHolder) holder).tv.setText(list.get(position).getFileName() + "");
            } else if (this.type == GalleryUtil.SELECTED_TYPE) {
                ((ViewSelectHolder) holder).imgDel.setTag(position + "");
                ((ViewSelectHolder) holder).imgDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = Integer.parseInt(v.getTag() + "");
                        list.remove(index);
                        notifyDataSetChanged();
                        updateText();
                    }
                });
            }
            //ImageLoaderAsynMemoryCache.getInstance().DisplayImage(list.get(position).getPath(),holder.vs);
                Glide.with(getApplicationContext()).load(list.get(position).getPath()).into((ImageView) (holder.vs));
            return view;
        }

        private void setUpVisibility(View view, int id) {
            view.findViewById(R.id.adapter_item_album).setVisibility(View.GONE);
            view.findViewById(R.id.adapter_item_scroll).setVisibility(View.GONE);
            view.findViewById(R.id.adapter_item_photo).setVisibility(View.GONE);
            view.findViewById(id).setVisibility(View.VISIBLE);
        }
    }
}
