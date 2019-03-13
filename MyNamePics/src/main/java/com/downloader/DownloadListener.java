package com.downloader;

/**
 * Created by caliber fashion on 9/16/2017.
 */

public interface DownloadListener {
    public void onDownloadStarted();

    public void onDownloadCompleted();

    public void onDownloadFailed();

    public void onDownloadProgress(int progress);
}
