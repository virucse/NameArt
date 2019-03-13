package com.formationapps.nameart.activity;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.PopupMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.formationapps.nameart.BuildConfig;
import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.AdsHelper;
import com.formationapps.nameart.helper.SaveToStorage;
import com.google.android.gms.ads.AdView;

public class FolderActivity extends BaseActivity {

	ImageAdapter myImageAdapter;
	ArrayList<String> itemList = new ArrayList<String>();
	private int w;
	public static File APP_FILE_PATH;
	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		logAnalyticEvent("FOLDER_ACTIVITY");
		
		setContentView(R.layout.activity_folder);
		
		APP_FILE_PATH  = SaveToStorage.getFolderFile(FolderActivity.this);


		try {
			final GridView gridview = (GridView) findViewById(R.id.gridview);
			myImageAdapter = new ImageAdapter(this);
			gridview.setAdapter(myImageAdapter);
			gridview.setOnItemClickListener(myOnItemClickListener);
			gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
					PopupMenu popupMenu = new PopupMenu(FolderActivity.this, view);
					popupMenu.getMenuInflater().inflate(R.menu.folder_item_long_click_menu, popupMenu.getMenu());
					popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(MenuItem item) {
							myImageAdapter.remove(position);
							return true;
						}
					});
					popupMenu.show();
					return true;
				}
			});
		}catch (Exception e){
			if(BuildConfig.DEBUG){
				Toast.makeText(this,"Exception:"+e.getMessage(),Toast.LENGTH_LONG).show();
			}
		}

		try {
			myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
			myAsyncTaskLoadFiles.execute();
		}catch (Exception e){
			if(BuildConfig.DEBUG){
				Toast.makeText(this,"TaskAdapter Exception:"+e.getMessage(),Toast.LENGTH_LONG).show();
			}
		}

		AdsHelper.loadAdmobBanner(this, (LinearLayout) findViewById(R.id.adConatiner));
	}

	private void deleteFile(File file){
		// Set up the projection (we only need the ID)
		String[] projection = { MediaStore.Images.Media._ID };

		// Match on the file path
		String selection = MediaStore.Images.Media.DATA + " = ?";
		String[] selectionArgs = new String[] { file.getAbsolutePath() };

		// Query for the ID of the media matching the file path
		Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		ContentResolver contentResolver = getContentResolver();
		Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
		if (c.moveToFirst()) {
			// We found the ID. Deleting the item via the content provider will also remove the file
			long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
			Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
			contentResolver.delete(deleteUri, null, null);
		} else {
			// File not found in media store DB
		}
		c.close();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	OnItemClickListener myOnItemClickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(itemList!=null&&itemList.size()>0) {
				Intent intent = new Intent(FolderActivity.this,
						PreviewActivity.class);
				intent.putExtra("IMG_PATH", itemList.get(position));
				intent.putExtra("FOLDER_ACTIVITY", "FOLDER");
				startActivityForResult(intent,0);
			}
		}
	};
	AsyncTaskLoadFiles myAsyncTaskLoadFiles;

	public class AsyncTaskLoadFiles extends AsyncTask<Void, String, Void> {

		File targetDirector;
		ImageAdapter myTaskAdapter;

		public AsyncTaskLoadFiles(ImageAdapter adapter) {
			myTaskAdapter = adapter;
		}

		@Override
		protected void onPreExecute() {
			targetDirector = APP_FILE_PATH;
			myTaskAdapter.clear();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try{
				File[] files = targetDirector.listFiles();
				if(files==null){
					return null;
				}
				for (File file : files) {
					publishProgress(file.getAbsolutePath());
					if (isCancelled())
						break;
				}
			}catch (Exception e){

			}
			return null;
		}

		protected void onProgressUpdate(String... values) {
			myTaskAdapter.add(values[0]);
			super.onProgressUpdate(values);
		}

		protected void onPostExecute(Void result) {
			myTaskAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}

	}

	public class ImageAdapter extends BaseAdapter {

		public Context mContext;
		private LayoutInflater mInflater;

		public ImageAdapter(Context c) {
			mContext = c;
			mInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		void add(String path) {
			itemList.add(path);
			notifyDataSetChanged();
		}

		void clear() {
			itemList.clear();
		}

		void remove(int index) {
			String f=itemList.get(index);
			itemList.remove(index);
			deleteFile(new File(f));
			notifyDataSetChanged();
		}

		public int getCount() {
			// Log.i("itemListSize", String.valueOf(itemList));
			return itemList.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return itemList.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) { // if it's not recycled, initialize some// attributes
				convertView=mInflater.inflate(R.layout.template_image_adapter,null);
			}
			ImageView imageView=(ImageView)convertView.findViewById(R.id.template_image_view);
			Glide.with(FolderActivity.this).load(itemList.get(position)).into(imageView);
			return convertView;
		}

		public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
				int reqHeight) {

			Bitmap bm = null;
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);

			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			bm = BitmapFactory.decodeFile(path, options);

			return bm;
		}

		public int calculateInSampleSize(

		BitmapFactory.Options options, int reqWidth, int reqHeight) {
			// Raw height and width of image
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > reqHeight || width > reqWidth) {
				if (width > height) {
					inSampleSize = Math.round((float) height
							/ (float) reqHeight);
				} else {
					inSampleSize = Math.round((float) width / (float) reqWidth);
				}
			}

			return inSampleSize;
		}

	}

}
