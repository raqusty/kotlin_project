package com.example.admin.kotlindownloader

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.liulishuo.filedownloader.util.FileDownloadUtils
import java.io.File
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

/**
 * Created by linzehao
 * time: 2017/11/23.
 * info: 下载 管理
 * 需求：
 * 1.创建一个单例来做管理运行的队列，app初始化的时候传入applicationContext
 * 2.taskqueue 最多启动3个任务，多的排队  √
 * 3.支持并行，串行
 * 4.删除任务
 * 5.存放意外终止的url,以便下次重新开始，持久化
 * 6.一个可视界面
 * 7.统一一个回掉接口，给使用者调用
 * 8.通知化下载
 * 9.暂停 ，因为在没有开始的任务不会有id,所以，（开始按钮，等待按钮）必须跟暂停按钮是互斥的
 *
 * 考虑功能：
 * 1.在service运行，在activity 运行
 * 2.动态设置网络下载线程数
 *
 * 可能的需求：
 * 1.如果要像迅雷那样，可暂停，同时开启正在准备的任务的话，就不能用 doNum这个来标记了，需要维护多一分list，用来存放暂停的任务
 *
 */
class DownloadManager private constructor() {

    //多任务的队列  最多是doNum个同时进行的任务
    private val waitQueue: BlockingQueue<DownloadEntity>   by lazy { ArrayBlockingQueue<DownloadEntity>(doNum) }
    //默认的路径
    private val defaultPath by lazy { FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "tmpdir1" }
    //最多同时多少个任务同时开启
    private val doNum = 3
    //当前同时多少个任务同时开启
    private var currdoNum = 0
    //任务进行的队列是否开启了循环，如果没有就开启就，如果开启了，就直接加入队列
    private var queueIsLoop = false

    //ApplicationContext
//    var context: Context? = null

    companion object {
        val Tag = "DownloadManager"
        fun get(): DownloadManager = Inner.downloadManger
    }

    //
    private object Inner {
        val downloadManger = DownloadManager()
    }


    fun initManager(context: Context) {
        FileDownloader.setup(context)
//        Log.i(Tag, defaultPath)
    }

    fun addTask(entity: DownloadEntity) {
        Thread({
            waitQueue.put(entity)
            if (!queueIsLoop) startQueue()
        }).start()
    }

    private fun startQueue() {
        var isLoop = true
        Thread({
            while (isLoop) {
                queueIsLoop = true
                if (currdoNum < doNum) {
                    var entity = waitQueue.poll()
                    if (!TextUtils.isEmpty(entity.url)) {
                        currdoNum++
                        //这里保存taskId,用来做停止操作
                        entity.taskId = startTask(entity.url!!)
                    }

                }
                if (waitQueue.size == 0) {
                    isLoop = false
                }
            }
        }).start()
        queueIsLoop = false
    }


    private fun startTask(url: String) :Int  = FileDownloader.getImpl().create(url)
                .setPath(defaultPath, true)
                .setListener(queueTarget).start()

    public fun puaseTask(){

    }


    /**
     *
     * 下载回调
     *
     */
    private val queueTarget: FileDownloadListener = object : FileDownloadListener() {
        override fun pending(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
//            Log.i(Tag, "pending")
        }

        override fun connected(task: BaseDownloadTask?, etag: String?, isContinue: Boolean, soFarBytes: Int, totalBytes: Int) {
            Log.i(Tag, "connected     " + task?.filename)
        }

        override fun progress(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
            Log.i(Tag, "progress  pro  " + (soFarBytes / totalBytes) * 100)
        }

        override fun blockComplete(task: BaseDownloadTask?) {
//            Log.i(Tag, "blockComplete")
        }

        override fun retry(task: BaseDownloadTask?, ex: Throwable?, retryingTimes: Int, soFarBytes: Int) {
//            Log.i(Tag, "retry")
        }

        override fun completed(task: BaseDownloadTask) {
            Log.i(Tag, "completed 222 " + task.filename)
            Log.i(Tag, "completed      ")
            currdoNum--
        }

        override fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
//            Log.i(Tag, "paused")
        }

        override fun error(task: BaseDownloadTask, e: Throwable) {
//            Log.i(Tag, "error")
        }

        override fun warn(task: BaseDownloadTask) {
//            Log.i(Tag, "warn")
        }
    }

    /**
     * 设置默认路径
     */
    fun setDefaultPath(defaultPath: String) {
        //这个路径将会作为它的默认路径
        FileDownloadUtils.setDefaultSaveRootPath(defaultPath)
    }

    /**
     * 获取未完成的下载
     * 需要实践：
     * 可以通过这个方法获取未下载完成的文件，之后加入下载列表继续下载
     */
    fun getTempPath() {
//        FileDownloadUtils.getTempPath()
    }


    /**
     * 清空filedownloader数据库中的所有数据
     */
    public fun clearAllTaskData() {

    }
}