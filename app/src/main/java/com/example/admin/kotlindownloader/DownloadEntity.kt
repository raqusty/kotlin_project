package com.example.admin.kotlindownloader

/**
 * Created by linzehao
 * time: 2017/11/24.
 * info:下载模块的entity
 */
class DownloadEntity (var name:String,var url: String){
    companion object {
        val WAIT = 0
        val PAUSE = 1
        val DOWNLOADING = 2
        val FINISH = 3
    }
    //任务id，用来暂停用的
    var taskId: Int = -1
    //状态
    //0:等待下载(默认)  1：暂停  2：下载中
    var state: Int = WAIT
    //
    var downloadListten : DownloadManager2.DownloadListten?=null
}