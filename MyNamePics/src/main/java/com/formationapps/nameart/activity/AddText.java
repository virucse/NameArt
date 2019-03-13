package com.formationapps.nameart.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout.Alignment;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.formationapps.nameart.BuildConfig;
import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.formationapps.nameart.adapter.ListAdapter;
import com.formationapps.nameart.helper.AppUtils;
import com.formationapps.nameart.helper.MyStickerText;
import com.formationapps.nameart.helper.TypefacesUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddText extends AppCompatActivity {
    int cl;
    EditText edt_name;
    String[] font;
    GridView grid_view;
    ImageView img_color;
    ProgressDialog progressDialog;
    String textString;
    Typeface tf;
    TextView txt_title;
    private int currentBackgroundColor;
    private int maincolor;

    public void getFont() {
        try {
            font = getAssets().list("fonts");
            for (int i = 0; i < font.length; i++) {
                font[i] = "fonts/" + font[i];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        textString = getString(R.string.add_text_text);
        this.tf = null;
        this.currentBackgroundColor = -1;
        this.maincolor = ViewCompat.MEASURED_STATE_MASK;
        this.cl = Color.parseColor("#000000");
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtext);
        final Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar_addtext);
        RelativeLayout close = (RelativeLayout) mToolBar.findViewById(R.id.close_addtext);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClose(v);
            }
        });
        RelativeLayout done = (RelativeLayout) mToolBar.findViewById(R.id.done_addtext);
        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onDone(v);
            }
        });

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                NameArtMenu.setFont(mToolBar, AppUtils.defaultTypeface);
            }
        }, 0);

        progressDialog = new ProgressDialog(this);
        txt_title = (TextView) findViewById(R.id.txt_title);
        edt_name = (EditText) findViewById(R.id.edt_name);
        setSupportActionBar(mToolBar);
        getFont();
        txt_title.setTypeface(TypefacesUtils.get(this, font[0]));
        grid_view = (GridView) findViewById(R.id.grid_view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                grid_view.setAdapter(new ListAdapter(AddText.this, font));
            }
        }, 1500);
        grid_view.setAdapter(new ListAdapter(this, font));
        grid_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    ((ListAdapter) grid_view.getAdapter()).notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });
        grid_view.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (edt_name.getText().toString().isEmpty()) {
                    Toast.makeText(AddText.this, "Add Text", Toast.LENGTH_SHORT).show();
                    return;
                }
                txt_title.setTypeface(TypefacesUtils.get(AddText.this, font[position]));
            }
        });
        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txt_title.setText(AddText.this.edt_name.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        img_color = (ImageView) findViewById(R.id.img_color);
        img_color.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AddText.this.colorpicker();
            }
        });
    }

    protected void colorpicker() {
        new AmbilWarnaDialog(this, this.cl, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                AddText.this.txt_title.setTextColor(color);
                AddText.this.txt_title.setHintTextColor(color);
                AddText.this.cl = color;
                AddText.this.maincolor = color;
            }
        }).show();
    }

    private void changeBackgroundColor(int selectedColor) {
        this.currentBackgroundColor = selectedColor;
        this.txt_title.setTextColor(selectedColor);
        this.maincolor = selectedColor;
    }

    public void onClose(View view) {
        if (AppUtils.first_time) {
            //startActivity(new Intent(this, NameEditorActivity.class));
            finish();
            AppUtils.first_time = false;
        } else {
            finish();
        }
    }

    public void onDone(View view) {
        if (this.txt_title.getText().toString().isEmpty()) {
            Toast.makeText(this, "Add Text", Toast.LENGTH_SHORT).show();
        } else {
            textString = this.txt_title.getText().toString();
            tf = this.txt_title.getTypeface();
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(this.edt_name.getWindowToken(), 0);
            new AsyncTaskNextActivity().execute(new String[]{BuildConfig.FLAVOR});
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_close) {
            if (AppUtils.first_time) {
                startActivity(new Intent(this, ArtActivity.class));
                finish();
                AppUtils.first_time = false;
            } else {
                finish();
            }
        }
        if (id == R.id.action_save) {
            if (this.txt_title.getText().toString().isEmpty()) {
                Toast.makeText(this, "Add Text", Toast.LENGTH_SHORT).show();
            } else {
                textString = this.txt_title.getText().toString();
                tf = this.txt_title.getTypeface();
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(this.edt_name.getWindowToken(), 0);
                new AsyncTaskNextActivity().execute(new String[]{BuildConfig.FLAVOR});
            }
        }
        return true;
    }

    protected void onResume() {
        super.onResume();
        loadBannerAd(true);
    }

    private void loadBannerAd(boolean isAdmobShown) {
        //Toast.makeText(this,"Show Admob Banner:"+AppUtils.showAdmobBanner,Toast.LENGTH_SHORT);
        AdView ad = (AdView) findViewById(R.id.adView);
        //Banner startBanner=(Banner)findViewById(R.id.startAppBanner);
        if (isAdmobShown) {
            ad.loadAd(new AdRequest.Builder().build());
            ad.setVisibility(View.VISIBLE);
            ad.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    loadBannerAd(false);
                }
            });
            //startBanner.setVisibility(View.GONE);
        } else {
            //startBanner.setVisibility(View.VISIBLE);
            //ad.setVisibility(View.GONE);
        }
    }

    private boolean isNetworkConnected() {
        return ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    void setApp() {
    }

    Drawable getThumb(int width, int height) {
        GradientDrawable thumb = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.parseColor("#00000000"), Color.parseColor("#00000000")});
        thumb.setShape(GradientDrawable.RECTANGLE);
        thumb.setStroke(1, Color.parseColor("#000000"));
        thumb.setBounds(0, 0, 4, 4);
        System.out.println("width==" + width);
        System.out.println("width==" + height);
        thumb.setSize(width, height);
        return thumb;
    }

    public class AsyncTaskNextActivity extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            AddText.this.progressDialog.setMessage("Please Wait");
            AddText.this.progressDialog.show();
        }

        protected String doInBackground(String... params) {
            AppUtils.chk_textSticker = true;
            MyStickerText sticker = new MyStickerText(AddText.this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sticker.setDrawable(getResources().getDrawable(R.drawable.bg_text, getTheme()));
            } else {
                sticker.setDrawable(getResources().getDrawable(R.drawable.bg_text));
            }
            sticker.setText(textString);
            sticker.setTypeface(AddText.this.tf);
            sticker.setTextColor(AddText.this.cl);
            sticker.setTextAlign(Alignment.ALIGN_CENTER);
            sticker.setMaxTextSize(20.0f);
            sticker.resizeText();
            AppUtils.textSticker = sticker;
            return null;
        }

        protected void onPostExecute(String result) {
            if (AppUtils.first_time) {
                //AddText.this.startActivity(new Intent(AddText.this, NameEditorActivity.class));
                AddText.this.finish();
                AppUtils.first_time = false;
            } else {
                AddText.this.finish();
            }
            if (AddText.this.progressDialog != null) {
                AddText.this.progressDialog.dismiss();
            }
        }
    }
}
