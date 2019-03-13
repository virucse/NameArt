package com.gallery.asynchronusloader;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MemoryCache {
    private static final String TAG = "MemoryCache";
    private Map<String, Bitmap> cache;
    private long limit;
    private long size;

    public MemoryCache() {
        this.cache = Collections.synchronizedMap(new LinkedHashMap(10, 1.5f, true));
        this.size = 0;
        this.limit = 1000000;
        setLimit(Runtime.getRuntime().maxMemory() / 8);
    }

    public void setLimit(long new_limit) {
        this.limit = new_limit;
    }

    public Bitmap get(String id) {
        try {
            if (this.cache.containsKey(id)) {
                return (Bitmap) this.cache.get(id);
            }
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void put(String id, Bitmap bitmap) {
        try {
            if (this.cache.containsKey(id)) {
                this.size -= getSizeInBytes((Bitmap) this.cache.get(id));
            }
            this.cache.put(id, bitmap);
            this.size += getSizeInBytes(bitmap);
            checkSize();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void checkSize() {
        if (this.size > this.limit) {
            Iterator<Entry<String, Bitmap>> iter = this.cache.entrySet().iterator();
            while (iter.hasNext()) {
                this.size -= getSizeInBytes((Bitmap) ((Entry) iter.next()).getValue());
                iter.remove();
                if (this.size <= this.limit) {
                    break;
                }
            }
            Log.i(TAG, "Clean cache. New size " + this.cache.size());
        }
    }

    public void clear() {
        this.cache.clear();
    }

    long getSizeInBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        return (long) (bitmap.getRowBytes() * bitmap.getHeight());
    }
}
