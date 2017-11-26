package com.example.admin.kotlindownloader

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.liulishuo.filedownloader.util.FileDownloadUtils
import java.io.File
import java.util.*

/**
 * Created by linzehao
 * time: 2017/11/23.
 * info: 下载 管理
 * 需求：
 * 1.创建一个单例来做管理运行的队列，app初始化的时候传入applicationContext
 * 2.taskqueue 最多启动3个任务，多的排队  √
 * 3.支持并行，串行
 * 4.删除任务 √
 * 5.存放意外终止的url,以便下次重新开始，持久化 这个待测试
 * 6.一个可视界面 √
 * 7.统一一个回掉接口，给使用者调用 √
 * 8.通知化下载

 *
 * 考虑功能：
 * 1.在service运行，在activity 运行
 * 2.动态设置网络下载线程数
 *
 * 可能的需求：
 * 1.如果要像迅雷那样，可暂停，同时开启正在准备的任务的话，就不能用 doNum这个来标记了，需要维护多一分list，用来存放暂停的任务
 *
 */
class DownloadManager2 private constructor() {

    //多任务的队列  最多是doNum个同时进行的任务
    private val waitVector: Vector<DownloadEntity>   by lazy { Vector<DownloadEntity>(doNum) }
    //默认的路径
    private val defaultPath by lazy { FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "tmpdir1" }
    //最多同时多少个任务同时开启
    private val doNum = 2
    //当前同时多少个任务同时开启
    private var currdoNum = 0
    //任务进行的队列是否开启了循环，如果没有就开启就，如果开启了，就直接加入队列
    private var queueIsLoop = false

    //ApplicationContext
//    var context: Context? = null

    companion object {
        val Tag = "DownloadManager"
        fun get(): DownloadManager2 = Inner.downloadManger
    }

    //
    private object Inner {
        val downloadManger = DownloadManager2()
    }


    fun initManager(context: Context) {
        FileDownloader.setup(context)
//        Log.i(Tag, defaultPath)
    }

    fun addTask(entity: DownloadEntity) {
        var isHas = false
        waitVector.forEach {
            if (it.url ==entity.url &&it.name ==entity.name ){
                if(it.state == DownloadEntity.PAUSE)
                    it.state =  DownloadEntity.WAIT
                isHas = true
            }
        }
        if (!isHas)
            waitVector.add(entity)
        if (!queueIsLoop)
            startQueue()
    }

    //通过判断名字，url ，来暂停任务
    fun pauseTask(entity: DownloadEntity):Boolean{
        waitVector.forEach {
            if (it.url ==entity.url &&it.name ==entity.name ){
                if (it.taskId!=-1){
                    FileDownloader.getImpl().pause(it.taskId)
                }
                //如果目前在下载状态，就下载数-1
                if (it.state == DownloadEntity.DOWNLOADING){
                    currdoNum--
                }
                it.state =  DownloadEntity.PAUSE
                return true
            }
        }
        return false
    }


    //通过判断名字，url ，来删除任务
    fun removeTask(entity: DownloadEntity):Boolean{
        waitVector.forEach {
            if (it.url ==entity.url &&it.name ==entity.name ){
                if (it.taskId!=-1){
                    FileDownloader.getImpl().pause(it.taskId)
                }
                //如果目前在下载状态，就下载数-1
                if (it.state == DownloadEntity.DOWNLOADING){
                    currdoNum--
                }
                waitVector.remove(it)
                return true
            }
        }
        return false
    }

    //删除完成的任务
    fun removeFinishTask():Boolean{
        waitVector.forEach {
            //如果目前在下载状态，就下载数-1
            if (it.state == DownloadEntity.FINISH){
                currdoNum--
            }
            waitVector.remove(it)
            return true
        }
        return false
    }

    //获取正在排队的任务
    private fun getTaskToStart():DownloadEntity?{
        waitVector.forEach {
            if (it.state ==DownloadEntity.WAIT )
                return it
        }
        //证明没有待下载的
        return null
    }

    private fun startQueue() {
        var isLoop = true
        Thread({
            while (isLoop) {
                queueIsLoop = true
                var entity = getTaskToStart()
                if (currdoNum < doNum) {
                    if (entity!=null && !TextUtils.isEmpty(entity.url)) {
                        entity!!.state =DownloadEntity.DOWNLOADING
                        currdoNum++
                        //这里保存taskId,用来做停止操作
                        entity.taskId = startTask(entity)
                    }
                }
                if (waitVector.size == 0) {
                    isLoop = false
                }
                Thread.sleep(500)
            }
            queueIsLoop = false
        }).start()

    }

    private fun startTask(entity: DownloadEntity): Int = FileDownloader.getImpl().create(entity.url)
            .setPath(defaultPath+ File.separator +entity.name)
            .setListener(object : FileDownloadListener() {
                override fun pending(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
//                    Log.i(Tag, "pending")
                    entity.downloadListten?.pending(task,soFarBytes,totalBytes)
                }

                override fun connected(task: BaseDownloadTask?, etag: String?, isContinue: Boolean, soFarBytes: Int, totalBytes: Int) {
//                    Log.i(Tag, "connected     " + task?.filename)
                    entity.downloadListten?.connected(task,etag,isContinue,soFarBytes,totalBytes)
                }

                override fun progress(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
//                    Log.i(Tag, "progress  pro  " )
                    entity.downloadListten?.progress(task,soFarBytes,totalBytes)
                }

                override fun blockComplete(task: BaseDownloadTask?) {
//            Log.i(Tag, "blockComplete")
                    entity.downloadListten?.blockComplete(task)
                }

                override fun retry(task: BaseDownloadTask?, ex: Throwable?, retryingTimes: Int, soFarBytes: Int) {
//            Log.i(Tag, "retry")
                    entity.downloadListten?.retry(task,ex,retryingTimes,soFarBytes)
                }

                override fun completed(task: BaseDownloadTask) {
                    currdoNum--
                    entity.state = DownloadEntity.FINISH
                    removeFinishTask()
                    entity.downloadListten?.completed(task)
                }

                override fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                    entity.downloadListten?.paused(task,soFarBytes,totalBytes)
                }

                override fun error(task: BaseDownloadTask, e: Throwable) {
//            Log.i(Tag, "error")
                    entity.downloadListten?.error(task,e)
                }

                override fun warn(task: BaseDownloadTask) {
//            Log.i(Tag, "warn")
                    entity.downloadListten?.warn(task)
                }
            }).start()



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

    interface DownloadListten{
         fun pending(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int)

         fun connected(task: BaseDownloadTask?, etag: String?, isContinue: Boolean, soFarBytes: Int, totalBytes: Int)

         fun progress(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int)

         fun blockComplete(task: BaseDownloadTask?)

         fun retry(task: BaseDownloadTask?, ex: Throwable?, retryingTimes: Int, soFarBytes: Int)

         fun completed(task: BaseDownloadTask)

         fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int)

         fun error(task: BaseDownloadTask, e: Throwable)

         fun warn(task: BaseDownloadTask)
    }
}