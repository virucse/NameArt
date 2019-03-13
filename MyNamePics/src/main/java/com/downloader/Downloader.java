package com.downloader;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by caliber fashion on 9/16/2017.
 */

public class Downloader {
    //antsUnzipPath = FileUtils.getDataDir(this, "antsLoc").getAbsolutePath();
    public static void download(final Context context, String unZipPath,
                                String downloadUrl, final DownloadListener dListener) {
        //String serverFilePath = "http://www.colorado.edu/conflict/peace/download/peace_problem.ZIP";
        String serverFilePath = downloadUrl;

        String path = FileUtils.getDataDir(context).getAbsolutePath();

        String fileName = "sample_download";
        File file = new File(path, fileName);

        String localPath = file.getAbsolutePath();


        FileDownloadService.DownloadRequest downloadRequest = new FileDownloadService.DownloadRequest(serverFilePath, localPath);
        downloadRequest.setRequiresUnzip(true);
        downloadRequest.setDeleteZipAfterExtract(true);
        downloadRequest.setUnzipAtFilePath(unZipPath);

        FileDownloadService.OnDownloadStatusListener listener = new FileDownloadService.OnDownloadStatusListener() {

            @Override
            public void onDownloadStarted() {
                if (dListener != null) {
                    dListener.onDownloadStarted();
                }
            }

            @Override
            public void onDownloadCompleted() {
                if (dListener != null) {
                    dListener.onDownloadCompleted();
                }
            }

            @Override
            public void onDownloadFailed() {
                if (dListener != null) {
                    dListener.onDownloadFailed();
                }
            }

            @Override
            public void onDownloadProgress(int progress) {
                if (dListener != null) {
                    dListener.onDownloadProgress(progress);
                }
            }
        };

        FileDownloadService.FileDownloader downloader = FileDownloadService.FileDownloader.getInstance(downloadRequest, listener);
        downloader.download(context);
    }

    //this method only download files...
    public static void downloadFile(final Context context, String unZipPath,String fileName,
                                String downloadUrl, final DownloadListener dListener) {
        //String serverFilePath = "http://www.colorado.edu/conflict/peace/download/peace_problem.ZIP";
        String serverFilePath = downloadUrl;

        File file = new File(unZipPath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            Toast.makeText(context, "File is create for .model", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }

        String localPath = file.getAbsolutePath();


        FileDownloadService.DownloadRequest downloadRequest = new FileDownloadService.DownloadRequest(serverFilePath, localPath);
        //downloadRequest.setRequiresUnzip(true);
        //downloadRequest.setDeleteZipAfterExtract(true);
        //downloadRequest.setUnzipAtFilePath(unZipPath);

        FileDownloadService.OnDownloadStatusListener listener = new FileDownloadService.OnDownloadStatusListener() {

            @Override
            public void onDownloadStarted() {
                if (dListener != null) {
                    dListener.onDownloadStarted();
                }
            }

            @Override
            public void onDownloadCompleted() {
                if (dListener != null) {
                    dListener.onDownloadCompleted();
                }
            }

            @Override
            public void onDownloadFailed() {
                if (dListener != null) {
                    dListener.onDownloadFailed();
                }
            }

            @Override
            public void onDownloadProgress(int progress) {
                if (dListener != null) {
                    dListener.onDownloadProgress(progress);
                }
            }
        };

        FileDownloadService.FileDownloader downloader = FileDownloadService.FileDownloader.getInstance(downloadRequest, listener);
        downloader.download(context);
    }
}
