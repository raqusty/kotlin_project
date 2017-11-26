package com.example.admin.kotlindownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;

/**
 * Created by linzehao
 * time: 2017/11/24.
 * info:
 */

public class test  {
    private String url;
    private int taskId;

    class ss extends FileDownloadListener{

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

        }

        @Override
        protected void completed(BaseDownloadTask task) {

        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {

        }

        @Override
        protected void warn(BaseDownloadTask task) {

        }
    }
}
