package com.removebg;

import android.graphics.Bitmap;

public class BitmapContainer {
    private static BitmapContainer instance;
    public static BitmapContainer getInstance(){
        if(instance==null)
            instance=new BitmapContainer();
        return instance;
    }
    private BitmapContainer(){}

    private Bitmap erasedBitmap;
    public boolean setErasedBitmap(Bitmap bitmap){
        if(bitmap!=null&&!bitmap.isRecycled()){
            if(erasedBitmap!=null&&!erasedBitmap.isRecycled()){
                erasedBitmap.recycle();
            }
            erasedBitmap=bitmap.copy(Bitmap.Config.ARGB_8888,true);
            return true;
        }else {
            return false;
        }
    }
    public Bitmap getErasedBitmap(){
        /*if(filteredBitmap!=null&&!filteredBitmap.isRecycled()){
            return getFilteredBitmap();
        }*/
        return getBitmap(erasedBitmap);
    }
    private Bitmap erasedOrgBitmap;
    public boolean setErasedOriginalBitmap(Bitmap bitmap){
        if(bitmap!=null&&!bitmap.isRecycled()){
            if(erasedOrgBitmap!=null&&!erasedOrgBitmap.isRecycled()){
                erasedOrgBitmap.recycle();
            }
            erasedOrgBitmap=bitmap.copy(Bitmap.Config.ARGB_8888,true);
            return true;
        }else {
            return false;
        }
    }
    public Bitmap getErasedOriginalBitmap(){
        return getBitmap(erasedOrgBitmap);

    }
    private Bitmap getBitmap(Bitmap bitmap){
        try {
            if(bitmap!=null&&!bitmap.isRecycled())
                return bitmap.copy(Bitmap.Config.ARGB_8888,true);
        }catch (OutOfMemoryError e){
            return bitmap;
        }
        catch (Exception e){
            return bitmap;
        }
        return bitmap;
    }
    private Bitmap smoothBitmap;
    public boolean setSmoothBitmap(Bitmap bitmap){
        if(bitmap!=null&&!bitmap.isRecycled()){
            if(smoothBitmap!=null&&!smoothBitmap.isRecycled()){
                smoothBitmap.recycle();
            }
            smoothBitmap=bitmap.copy(Bitmap.Config.ARGB_8888,true);
            return true;
        }else {
            return false;
        }
    }
    public Bitmap getSmoothBitmap(){
        if(smoothBitmap!=null&&!smoothBitmap.isRecycled()){
            return getBitmap(smoothBitmap);
        }else if(filteredBitmap!=null&&!filteredBitmap.isRecycled()){
            return getFilteredBitmap();
        }else {
            return getBitmap(erasedBitmap);
        }
    }
    private Bitmap filteredBitmap;
    public boolean setFilteredBitmap(Bitmap bitmap){
        if(bitmap!=null&&!bitmap.isRecycled()){
            if(filteredBitmap!=null&&!filteredBitmap.isRecycled()){
                filteredBitmap.recycle();
            }
            filteredBitmap=bitmap.copy(Bitmap.Config.ARGB_8888,true);
            return true;
        }else {
            return false;
        }
    }
    public Bitmap getFilteredBitmap(){
        if(filteredBitmap!=null&&!filteredBitmap.isRecycled()){
            return getBitmap(filteredBitmap);
        }else {
            return getSmoothBitmap();
        }
    }
    private Bitmap previewBitmap;
    public void setPreviewBitmap(Bitmap bitmap){
        if(bitmap!=null&&!bitmap.isRecycled()){
           destroyBitmap(previewBitmap);
           previewBitmap=bitmap.copy(Bitmap.Config.ARGB_8888,true);
        }
    }
    public Bitmap getPreviewBitmap(){
        return previewBitmap;
    }
    public void destroyPreview(){
        destroyBitmap(previewBitmap);
    }
    public void destroyBitmaps(){
        destroyBitmap(erasedBitmap);
        destroyBitmap(erasedOrgBitmap);
        destroyBitmap(smoothBitmap);
        destroyBitmap(filteredBitmap);
    }
    private void destroyBitmap(Bitmap bitmap){
        if(bitmap!=null&&!bitmap.isRecycled()){
            bitmap.recycle();
        }
    }

    private Bitmap tempBitmap;
    public boolean setBitmap(Bitmap bitmap){
        if(bitmap!=null&&!bitmap.isRecycled()){
            if(tempBitmap!=null&&!tempBitmap.isRecycled()){
                tempBitmap.recycle();tempBitmap=null;
            }
            tempBitmap=bitmap.copy(Bitmap.Config.ARGB_8888,true);
            if(tempBitmap!=null&&!tempBitmap.isRecycled()){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
    public Bitmap getBitmap(){
        Bitmap result=null;
        if(tempBitmap!=null&&!tempBitmap.isRecycled()){
            result=tempBitmap.copy(Bitmap.Config.ARGB_8888,true);
            tempBitmap.recycle();
        }
        return result;
    }
}
