package com.formationapps.nameart.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.StateSet;
import android.util.TypedValue;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.formationapps.nameart.App;
import com.formationapps.nameart.BuildConfig;
import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.webapi.model.WebConstant;
import com.xiaopo.flying.sticker.DrawableSticker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppUtils {
    public static final String EDITORACTIVITYNAME = "editoractivityname";
    public static final String NAMEEDITORACTIVITY = "nameeditoract";
    public static final String TEMPLATEEDITORACTIVITY = "templateeditoract";
    public static final String BORDERFOLDERNAME = "border";
    public static final String BACKGROUNDDECO = "decoration";
    public static final String BACKGROUNDMATERIAL = "material";
    public static final String BACKGROUNDTEXTURE = "texture";
    public static final String BACKGROUNDPATTERN = "pattern";
    public static final String FONTPACK1 = "font1";
    public static final String FONTPACK2 = "font2";
    public static final String FONTPACK3 = "font3";
    public static final String FONTPACK4 = "font4";
    public static final String FONTPACK5 = "font5";
    public static final String FONTPACK6 = "font6";
    public static final String FONTPACK7 = "font7";
    public static final String FONTHINDI = "hindifont";
    public static final String PIP = "pip";
    public static final List<NameArtMenu.Apps> appsList = new ArrayList<>();
    public static boolean chk_sticker;
    public static boolean chk_textSticker;
    public static int color;
    public static boolean first_time;
    public static boolean set_somtthing;
    public static DrawableSticker sticker;
    public static String text;
    public static MyStickerText textSticker;
    public static int total_click;
    public static int symbolAdsShownCount = 1;
    public static int templateBtnClickCount = 1;
    public static int stickersBtnClickCount = 0;
    public static String downloadableRootUrl;
    public static Typeface defaultTypeface;
    public static int baseActivityStartCount = 0;
    public static Typeface webApiFont;
    public static int screenWidth, screenHeight, statusBarHeight;
    public static DisplayMetrics dm;
    public static Context mContext;

    static {
        total_click = 0;
        textSticker = null;
        chk_textSticker = false;
        sticker = null;
        chk_sticker = false;
        first_time = true;
        color = 0;
        text = BuildConfig.FLAVOR;
        set_somtthing = false;
    }

    public static void onCreate(Context context) {
        mContext = context;
        dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        symbolAdsShownCount = getSymbolAdsCount(context);
        templateBtnClickCount = getTemplateBtnClickCount(context);
        stickersBtnClickCount = getStickersBtnClickCount(context);
        downloadableRootUrl = getDownloadRootUrl(context);
        int result = 25;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        statusBarHeight = result;
        defaultTypeface = TypefacesUtils.get(context, "fonts/POORICH.TTF");
        webApiFont = TypefacesUtils.get(context, "fonts/arial.ttf");
    }

    public static void setImage(ImageButton viewById, int ic_clear_all) {
        setImage(viewById, ic_clear_all, getColor(viewById.getContext(), R.attr.color));
    }

    public static void setImage(ImageButton viewById, int res, int color) {
        Drawable stateButtonDrawable = ContextCompat.getDrawable(viewById.getContext(), res).mutate();
        stateButtonDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        viewById.setImageDrawable(stateButtonDrawable);
        viewById.setBackgroundResource(R.drawable.btn_selector);
    }

    public static void setSelector(ImageButton ib, int resId) {
        int color = getColor(ib.getContext(), R.attr.colorAccent);
        Drawable stateButtonDrawable = ContextCompat.getDrawable(ib.getContext(), resId).mutate();
        Drawable stateButtonDrawablePress = ContextCompat.getDrawable(ib.getContext(), resId).mutate();
        stateButtonDrawablePress.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{16842913}, stateButtonDrawablePress);
        drawable.addState(new int[]{16842919}, stateButtonDrawablePress);
        drawable.addState(StateSet.WILD_CARD, stateButtonDrawable);
        ib.setImageDrawable(drawable);
        ib.setBackgroundResource(R.drawable.btn_selector);
    }

    public static int getColor(Context context, int attrId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrId, typedValue, true);
        return typedValue.data;
    }

    public static void setSeekBarThumb(SeekBar seek) {
        if (Build.VERSION.SDK_INT >= 16) {
            Drawable d = seek.getThumb();
            if (d != null) {
                d.setColorFilter(getColor(seek.getContext(), R.attr.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                seek.setThumb(d);
            }
        }
    }

    public static int dpToPx(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static SharedPreferences getSharedPref(Context context) {
        return context.getSharedPreferences("" + App.getInstance().getContext().getPackageName(), Context.MODE_PRIVATE);
    }

    public static String getDownloadRootUrl(Context context) {
        return getSharedPref(App.getInstance().getContext()).getString("imagedownloadrooturl", "http://13.126.61.6/");
    }

    public static void setDownloadRootUrl(Context context, String url) {
        getSharedPref(App.getInstance().getContext()).edit().putString("imagedownloadrooturl", url).apply();
    }

    public static String getWebApiDomain() {
        return getSharedPref(App.getInstance().getContext()).getString("webapibase", "http://13.126.61.6/");
    }

    public static void setWebApiDomain(Context context, String url) {
        getSharedPref(App.getInstance().getContext()).edit().putString("webapibase", url).apply();
    }

    public static void setSymbolAdCount(Context context, int count) {
        getSharedPref(App.getInstance().getContext()).edit().putInt("symboladcount", count).apply();
    }

    public static int getSymbolAdsCount(Context context) {
        return getSharedPref(App.getInstance().getContext()).getInt("symboladcount", 1);
    }

    public static void setTemplateBtnClickCount(Context context, int templateBtnClickCount) {
        getSharedPref(App.getInstance().getContext()).edit().putInt("templatebtnclickcount", templateBtnClickCount).apply();
    }

    public static int getTemplateBtnClickCount(Context context) {
        return getSharedPref(App.getInstance().getContext()).getInt("templatebtnclickcount", 1);
    }

    public static void setStickersBtnClickCount(Context context, int ClickCount) {
        getSharedPref(App.getInstance().getContext()).edit().putInt("stickerbuttonclickcount", ClickCount).apply();
    }

    public static int getStickersBtnClickCount(Context context) {
        return getSharedPref(App.getInstance().getContext()).getInt("stickerbuttonclickcount", 1);
    }

    public static void setTrendingTemplateRootUrl(String url){
        getSharedPref(App.getInstance().getContext()).edit().putString("trendingtemplaterooturl",url).apply();
    }
    public static String getTrendingTemplateRootUrl(){
        return getSharedPref(App.getInstance().getContext()).getString("trendingtemplaterooturl","http://13.126.61.6/");
    }

    public static void setAppOpenCount(Context context) {
        int count = getAppOpenCount(context);
        count = count + 1;
        getSharedPref(App.getInstance().getContext()).edit().putInt("APPOPENCOUNT", count).apply();
    }

    public static int getAppOpenCount(Context context) {
        return getSharedPref(App.getInstance().getContext()).getInt("APPOPENCOUNT", 0);
    }
    public static boolean getMobFoxBannerStatus(Context context){
        return getSharedPref(App.getInstance().getContext()).getBoolean("mobfoxstatus",false);
    }
    public static void setMobFoxBannerStatus(Context context,boolean status){
        getSharedPref(App.getInstance().getContext()).edit().putBoolean("mobfoxstatus",status).apply();
    }

    public static void setAppRestrictVersionCode(Context context,int code){
        getSharedPref(App.getInstance().getContext()).edit().putInt("restrictappversioncode",code).apply();
    }
    public static int getAppRestrictVersionCode(Context context){
        return getSharedPref(App.getInstance().getContext()).getInt("restrictappversioncode",43);
    }

    public static void setStickerRemotePath(String path){
        getSharedPref(App.getInstance().getContext()).edit().putString("stickersremotepath",path).apply();
    }

    public static String getStickerRemotePath() {
        return getSharedPref(App.getInstance().getContext()).getString("stickersremotepath","defa");
    }
    public static void setStickerFilePath(String stickers_file_path) {
        getSharedPref(App.getInstance().getContext()).edit().putString("stickersfilepath",stickers_file_path).apply();
    }
    public static String getStickerFilePath(){
        return getSharedPref(App.getInstance().getContext()).getString("stickersfilepath","stickers/stickers.txt");
    }
    //this is path from firebase ie bg/bg.txt
    public static void setSymbleFilePath(String path){
        getSharedPref(App.getInstance().getContext()).edit().putString("symbolFilePath",path).apply();
    }
    public static String getSymbleFilePath(){
        return getSharedPref(App.getInstance().getContext()).getString("symbolFilePath","suits/suitlist.txt");
    }
    //this is remote base path where all bg's are stored
    public static void setSymbleRemotePath(String stickers_file_path) {
        getSharedPref(App.getInstance().getContext()).edit().putString("symbleremotepath",stickers_file_path).apply();
    }
    public static String getSymbleRemotePath(){
        return getSharedPref(App.getInstance().getContext()).getString("symbleremotepath","");
    }
    public static void setTemplatesRemotePath(String path){
        getSharedPref(App.getInstance().getContext()).edit().putString("templateremotepath",path).apply();
    }
    public static String getTemplatesRemotePath(){
        return getSharedPref(App.getInstance().getContext()).getString("templateremotepath","sdsdsds");
    }
    public static void setTemplatesFilePath(String path){
        getSharedPref(App.getInstance().getContext()).edit().putString("templatefilepath",path).apply();
    }
    public static String getTemplatesFilePath(){
        return getSharedPref(App.getInstance().getContext()).getString("templatefilepath","suits/suitlist.txt");
    }
    public static void setFontDownloadRootUrl(String url){
        getSharedPref(App.getInstance().getContext()).edit().putString("fontdowdrooturl",url).apply();
    }
    public static String getFontDownloadRootUrl(){
        return getSharedPref(App.getInstance().getContext()).getString("fontdowdrooturl","jhj");
    }

    public static void setPIPDownloadRootUrl(String url){
        getSharedPref(App.getInstance().getContext()).edit().putString("pipdowdrooturl",url).apply();
    }
    public static String getPIPDownloadRootUrl(){
        return getSharedPref(App.getInstance().getContext()).getString("pipdowdrooturl","jhj");
    }

    //i.e. border<=>frames
    public static void setBorderDownloadRootUrl(String url){
        getSharedPref(App.getInstance().getContext()).edit().putString("borderdowdrooturl",url).apply();
    }
    //i.e. border<=>frames
    public static String getBorderDownloadRootUrl(){
        return getSharedPref(App.getInstance().getContext()).getString("borderdowdrooturl","jhj");
    }
    //i.e. bg<=>backgroun resource
    public static void setBgDownloadRootUrl(String url){
        getSharedPref(App.getInstance().getContext()).edit().putString("bgbcjdownloadjhfhf",url).apply();
    }
    //i.e. bg<=>backgroun resource
    public static String getBgDownloadRootUrl(){
        return getSharedPref(App.getInstance().getContext()).getString("bgbcjdownloadjhfhf","jhj");
    }


    public static void sendTemplateCount(final String path) {
        String url = AppUtils.getTrendingTemplateRootUrl();//WebConstant.getInstance().BASEURL + "trending";
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap();
                    params.put("url", path + "");
                    return params;
                }

            };
            App.getInstance().addToRequestQueue(stringRequest, "trendingPost");
        } catch (Exception e) {
            //Toast.makeText(context,"Excepton.send:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}
