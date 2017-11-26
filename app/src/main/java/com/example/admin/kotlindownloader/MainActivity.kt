package com.example.admin.kotlindownloader

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private val downManage: DownloadManager2 by lazy { DownloadManager2.get() }
    var downEntity :DownloadEntity ? = null
    var downEntity2 :DownloadEntity ? = null
    var downEntity3 :DownloadEntity ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        downManage.initManager(this)
        downEntity = DownloadEntity("111.apk","http://cdn.llsapp.com/android/LLS-v4.0-595-20160908-143200.apk")
        downEntity2 = DownloadEntity("222.apk","http://cdn.llsapp.com/android/LLS-v4.0-595-20160908-143200.apk")
        downEntity3 = DownloadEntity("333.apk","http://cdn.llsapp.com/android/LLS-v4.0-595-20160908-143200.apk")

        start_btn_1.run {
            start_btn_1.setOnClickListener {
                downManage.addTask(downEntity!!)
                downManage.addTask(downEntity2!!)
                downManage.addTask(downEntity3!!)
            }

        }

        pause_btn_1.run {
            pause_btn_1.setOnClickListener {
                downManage.pauseTask(downEntity!!)
            }
        }

        delete_btn_1.run {
//            downManage.removeTask(downEntity!!)
        }

        progressBar_1.run {
            downEntity?.downloadListten =object : DownloadManager2.DownloadListten {
                override fun pending(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                }

                override fun connected(task: BaseDownloadTask?, etag: String?, isContinue: Boolean, soFarBytes: Int, totalBytes: Int) {
                }

                override fun progress(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                    if (totalBytes == -1) {
                        // chunked transfer encoding data
                        progressBar_1.isIndeterminate = true
                    } else {
                        progressBar_1.max = totalBytes
                        progressBar_1.progress = soFarBytes
                    }
                }

                override fun blockComplete(task: BaseDownloadTask?) {
                }

                override fun retry(task: BaseDownloadTask?, ex: Throwable?, retryingTimes: Int, soFarBytes: Int) {
                }

                override fun completed(task: BaseDownloadTask) {
                    Log.i("linzehao","completed")
                }

                override fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                    Log.i("linzehao","paused")
                }

                override fun error(task: BaseDownloadTask, e: Throwable) {
                }

                override fun warn(task: BaseDownloadTask) {
                }
            }
        }


        progressBar_2.run {
            downEntity2?.downloadListten =object : DownloadManager2.DownloadListten {
                override fun pending(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                }

                override fun connected(task: BaseDownloadTask?, etag: String?, isContinue: Boolean, soFarBytes: Int, totalBytes: Int) {
                }

                override fun progress(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                    if (totalBytes == -1) {
                        // chunked transfer encoding data
                        progressBar_2.isIndeterminate = true
                    } else {
                        progressBar_2.max = totalBytes
                        progressBar_2.progress = soFarBytes
                    }
                }

                override fun blockComplete(task: BaseDownloadTask?) {
                }

                override fun retry(task: BaseDownloadTask?, ex: Throwable?, retryingTimes: Int, soFarBytes: Int) {
                }

                override fun completed(task: BaseDownloadTask) {
                    Log.i("linzehao","completed")
                }

                override fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                    Log.i("linzehao","paused")
                }

                override fun error(task: BaseDownloadTask, e: Throwable) {
                }

                override fun warn(task: BaseDownloadTask) {
                }
            }
        }


        progressBar_3.run {
            downEntity3?.downloadListten =object : DownloadManager2.DownloadListten {
                override fun pending(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                }

                override fun connected(task: BaseDownloadTask?, etag: String?, isContinue: Boolean, soFarBytes: Int, totalBytes: Int) {
                }

                override fun progress(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                    if (totalBytes == -1) {
                        // chunked transfer encoding data
                        progressBar_3.isIndeterminate = true
                    } else {
                        progressBar_3.max = totalBytes
                        progressBar_3.progress = soFarBytes
                    }
                }

                override fun blockComplete(task: BaseDownloadTask?) {
                }

                override fun retry(task: BaseDownloadTask?, ex: Throwable?, retryingTimes: Int, soFarBytes: Int) {
                }

                override fun completed(task: BaseDownloadTask) {
                    Log.i("linzehao","completed")
                }

                override fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                    Log.i("linzehao","paused")
                }

                override fun error(task: BaseDownloadTask, e: Throwable) {
                }

                override fun warn(task: BaseDownloadTask) {
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
