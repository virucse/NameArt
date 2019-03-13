package com.gallery.utils;

public class PhotoItem {
    private long _id;
    private String name;
    private String path;

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return this.name != null ? this.name : "Vid" + System.currentTimeMillis();
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return this._id;
    }

    public void setId(long albumId) {
        this._id = albumId;
    }

    public String toString() {
        return new StringBuilder(String.valueOf(this._id)).toString();
    }

    public int hashCode() {
        return super.hashCode();
    }
}
