package com.gif;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.formationapps.nameart.activity.BaseActivity;

import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.ShareActivity;
import com.formationapps.nameart.helper.AdsHelper;
import com.gallery.activity.ImageSelectAcivity;
import com.gallery.utils.ImageUtils;
import com.gallery.utils.PhotoItem;
import com.waynejo.androidndkgif.GifEncoder;
import com.xiaopo.flying.sticker.StickerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import formationapps.helper.stickers.StickerFragment;
import nonworkingcode.effects.activity.EffectActivity;
import nonworkingcode.effects.util.EffectsUtils;

import static com.gif.GIFPanel.REQ_FOR_FILTER;

public class GIFActivity extends BaseActivity implements OnDelaySelectListener {

    public static List<PhotoItem> gifList;
    public static List<Bitmap> bitmapList;
    public  int currentPage = 0;
    private ImageButton ibBack;
    private ImageButton ibSave;
    private ImageButton btn_prv;
    private GIFPanel mGIFPanel;
    private ImageView actualimg;
    private RadioButton rbSelecter;
    private Bitmap sourceBitmap, filterBitmap;
    private RelativeLayout mainRelView, mBorderLayout;
    private Handler handler;
    private Runnable Update;
    private Timer timer;
    private int DEALY_MS = 100;
    private int DEALY = 500;
    private boolean btn_prv_IsClicked = true;
    private String filePath, destPath;
    private String quality;
    private int width,height;
    private boolean isPlayingGif=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        logAnalyticEvent("GIF_ACTIVITY");
        isPlayingGif=false;
        mGIFPanel = (GIFPanel) findViewById(R.id.panel_filter_menu);
        actualimg = (ImageView) findViewById(R.id.actualimg);
        mainRelView = (RelativeLayout) findViewById(R.id.main_rel_view);

        Display display = getWindowManager().getDefaultDisplay();
        Point p=new Point();
        display.getSize(p);
        width = p.x;//display.getWidth();
        height = p.y;

        if (mBorderLayout == null)
            mBorderLayout = (RelativeLayout) findViewById(R.id.borderView);

        if (stickerView == null) {
            stickerView = (StickerView) findViewById(R.id.sticker_view);
            setStickerOperation();
        }

        AdsHelper.loadAdmobBanner(this, (LinearLayout) findViewById(R.id.ad_container));

        gifList = ImageSelectAcivity.fullList;
        bitmapList = changeIntoBitmap();
        init();


        ibBack = (ImageButton) findViewById(R.id.ib_gif_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ibSave = (ImageButton) findViewById(R.id.ib_gif_apply);
        ibSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogForQuality();

            }
        });
        btn_prv = (ImageButton) findViewById(R.id.btn_prv_play_stop);
        btn_prv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_prv_IsClicked) {
                    isPlayingGif=true;
                    startPlayingPreview();
                } else {
                    isPlayingGif=false;
                    stopPlayingPreview();
                }
            }
        });

        if (!isNetworkConnected()) {
            showNetworkErrorMessage(this);
        }
    }

    private void stopPlayingPreview(){
        btn_prv.setImageResource(R.drawable.icons_play);
        if (timer!=null){
            timer.cancel();
        }
        btn_prv_IsClicked = true;
    }

    private void startPlayingPreview(){
        btn_prv.setImageResource(R.drawable.icons_pause);
        slidingTimer();
        btn_prv_IsClicked = false;
    }

    GifISelectDialog gifISelectDialog;

    public void onFilterButtonClick() {
        gifISelectDialog = GifISelectDialog.newInstance(bitmapList, new OnImagePosSelectListener() {
            @Override
            public void onImagePosSelected(int pos) {
                currentPage=pos;
                sourceBitmap = bitmapList.get(pos);
                EffectsUtils.getInstance().setBitmap(sourceBitmap, true, true);
                startActivityForResult(
                        new Intent(GIFActivity.this, EffectActivity.class), REQ_FOR_FILTER);
                gifISelectDialog.getDialog().dismiss();
            }
        });
        gifISelectDialog.show(getSupportFragmentManager(), "dialog");

    }

    public void changeSpeedOfGif(){
        getSupportFragmentManager().beginTransaction().add(R.id.rel_content_temp,
                new SpeedFragment(),"speed").commit();
    }

    public void showSticker() {
        getSupportFragmentManager().beginTransaction().add(R.id.rel_content_temp,
                new StickerFragment(), "text").commit();
    }

    Thread thread;
    public void slidingTimer() {
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       if(isPlayingGif&&bitmapList!=null&&bitmapList.size()>0){
                           if(currentPage>=bitmapList.size()-1){
                               currentPage=0;
                           }
                           actualimg.setImageBitmap(bitmapList.get(currentPage));
                           currentPage++;
                           actualimg.postDelayed(this,DEALY);
                       }else{
                           actualimg.removeCallbacks(this);
                       }
                   }
               });
            }
        });
        thread.start();

       /* handler = new Handler();
        Update = new Runnable() {
            @Override
            public void run() {
                if (currentPage >= bitmapList.size() - 1) {
                    currentPage = 0;
                }
                // add code for preview.
                actualimg.setImageBitmap(bitmapList.get(currentPage));
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
                currentPage++;
            }
        }, DEALY_MS, DEALY);*/

    }

    public List<Bitmap> changeIntoBitmap() {

        bitmapList = new ArrayList<>();
        if (gifList != null&&gifList.size() > 0  ) {
            for (int i = 0; i < gifList.size(); i++) {
                PhotoItem photoItem = gifList.get(i);
                filePath = photoItem.getPath();
                Uri uri=Uri.fromFile(new File(filePath));
                Bitmap bitmap = ImageUtils.getinstance().loadImage(this,uri,width,height);//BitmapFactory.decodeFile(filePath);
                //Bitmap bm = scaleBitmap(bitmap, (int) (width*.8), (int) (hight*.6));
                bitmapList.add(bitmap);
            }
        }
        return bitmapList;
    }

    private void init() {
        try {
            actualimg.setImageBitmap(bitmapList.get(0));
        } catch (IndexOutOfBoundsException e){

        }catch (Exception e){

        }

    }

    public void addBorder(Drawable drawable) {
        Drawable bitmap = mBorderLayout.getBackground();
        recycleDrawable(bitmap);
        mBorderLayout.setBackground(drawable);
    }

    public void recycleDrawable(Drawable d) {
        if (d != null) {
            Bitmap b = ((BitmapDrawable) d).getBitmap();
            if (b != null && !b.isRecycled()) {
                //b.recycle();
            }
        }
    }

    public void showDialogForQuality() {
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_gif_quality_layout, null);

        RadioGroup group = (RadioGroup) alertLayout.findViewById(R.id.rg_gif_quality);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbSelecter = (RadioButton) alertLayout.findViewById(checkedId);
                quality = rbSelecter.getText().toString();
            }
        });
        quality=((RadioButton)alertLayout.findViewById(R.id.radio3)).getText().toString();
        AlertDialog.Builder alert = new AlertDialog.Builder(GIFActivity.this);
       // alert.setTitle("Select Quality");
        alert.setView(alertLayout);
        alert.setCancelable(false);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                EncodedGif task = new EncodedGif();
                task.execute();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (stickerView != null)
            stickerView.setLocked(false);
        mainRelView.invalidate();
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentByTag("speed");
        if (f !=null){
            getSupportFragmentManager().beginTransaction().remove(f).commit();
            mGIFPanel.resetLinSelector();
            return;
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("text");
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            mGIFPanel.resetLinSelector();
            return;
        }
        if (!mGIFPanel.onBackPressed()) {
            showBackpressedDialog();
            // overridePendingTransition(17432576, R.anim.start_bottom_out);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_FOR_FILTER) {
            filterBitmap = EffectsUtils.getInstance().getBitmap(true, true);
            List<Bitmap> tempList = new ArrayList<>(bitmapList.size());
            for (int i = 0; i < bitmapList.size(); i++) {
                if (i == currentPage) {
                    tempList.add(filterBitmap);
                } else {
                    tempList.add(bitmapList.get(i));
                }
            }
            bitmapList.clear();
            bitmapList = tempList;
        }
    }

    @Override
    public void onDelaySelectListener(int delay) {
        DEALY=delay;
    }


    private class EncodedGif extends AsyncTask<Void, Integer, Void> {
        ProgressBar ppb;
        TextView pPercent;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ppb=findViewById(R.id.progress_bar_gif);
            ppb.setVisibility(View.VISIBLE);
            pPercent=findViewById(R.id.progress_percent);
            pPercent.setVisibility(View.INVISIBLE);
            if (stickerView !=null){
                stickerView.setHandlingStickerNull();
            }
            btn_prv.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            publishProgress(0);
            Bitmap bitmap=createBitmap(0);

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/Name Art/" + System.currentTimeMillis() + ".gif");
            destPath = myDir.getPath();
            GifEncoder gifEncoder = new GifEncoder();

            try {
                if (quality.equals("SIMPLE_FAST")) {
                    gifEncoder.init(bitmap.getWidth(), bitmap.getHeight()
                            , destPath, GifEncoder.EncodingType.ENCODING_TYPE_SIMPLE_FAST);
                } else if (quality.equals("FAST")) {
                    gifEncoder.init(bitmap.getWidth(), bitmap.getHeight(), destPath, GifEncoder.EncodingType.ENCODING_TYPE_FAST);
                } else if (quality.equals("STABLE_HIGH_MEMORY")) {
                    gifEncoder.init(bitmap.getWidth(), bitmap.getHeight(), destPath, GifEncoder.EncodingType.ENCODING_TYPE_STABLE_HIGH_MEMORY);
                } else {
                    gifEncoder.init(bitmap.getWidth(), bitmap.getHeight(), destPath, GifEncoder.EncodingType.ENCODING_TYPE_NORMAL_LOW_MEMORY);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i("CollageActivity", "doInBackground: " + e.getMessage());
            }
            gifEncoder.encodeFrame(bitmap, DEALY);
            for (int i =1; i < bitmapList.size(); i++) {
                try {
                    publishProgress(i);
                    bitmap=createBitmap(i);
                    if (bitmap.getConfig() == Bitmap.Config.ARGB_8888) {
                        gifEncoder.encodeFrame(bitmap, DEALY);
                    } else {
                        Log.i("GIFActivity", "doInBackground.config: " + bitmap.getConfig());
                    }
                } catch (Exception e) {
                    Log.i("GIFActivity", "doInBackground.encoder: " + e.getMessage());
                }

            }
            gifEncoder.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ibSave.setVisibility(View.GONE);
            pPercent.setVisibility(View.INVISIBLE);
            ppb.setVisibility(View.INVISIBLE);

            btn_prv.setVisibility(View.VISIBLE);

            BaseActivity.image_file = new File(destPath);

            switchToNextActivity();

            //TastyToast.makeText(getApplicationContext(), "Saved to SD card", TastyToast.LENGTH_SHORT,
                    //TastyToast.SUCCESS);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            try {
                stopPlayingPreview();
                actualimg.setImageBitmap(bitmapList.get(values[0]));
                mainRelView.invalidate();
                int per;
                if(values[0]==0){
                    per=0;
                }else{
                    per=(bitmapList.size()*values[0]/100)*100;
                }

                pPercent.setText(per+" %");
            }catch (IndexOutOfBoundsException e){ }
            catch (Exception e){ }
            super.onProgressUpdate(values);
        }
    }

    private void switchToNextActivity(){
        Intent intent = new Intent(this, ShareActivity.class);
        startActivity(intent);
    }

    private Bitmap createBitmap(final int pos){
        mainRelView.setDrawingCacheEnabled(true);
        mainRelView.buildDrawingCache();
        Bitmap tempBitmap = Bitmap.createBitmap(mainRelView.getDrawingCache(true));
        Bitmap temp2=Bitmap.createBitmap(tempBitmap.getWidth(),tempBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c=new Canvas(temp2);
        c.drawBitmap(tempBitmap,0,0,null);
        mainRelView.destroyDrawingCache();
        mainRelView.setWillNotCacheDrawing(false);
        if(tempBitmap!=null&&!tempBitmap.isRecycled()){
            //tempBitmap.recycle();
        }
        int reqWidth=(int) (width*0.6);
        int rewHeight=(reqWidth*temp2.getHeight())/temp2.getWidth();
      Bitmap bm =  Bitmap.createScaledBitmap(temp2,reqWidth,rewHeight,true);
        if(temp2!=null&&!temp2.isRecycled()){
            //temp2.recycle();
        }
        return bm;
    }
}
