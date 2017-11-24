package com.example.admin.kotlindownloader

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        Log.i("linzehao", "000")


        var downManager = DownloadManager.get()
        downManager.initManager(this)

        downManager.addTask("http://cdn-l.llsapp.com/connett/7b6b5485-0d19-476c-816c-ff6523fae539")
        downManager.addTask("http://cdn-l.llsapp.com/connett/33fa9155-c99a-407f-8d2c-82e9d17f4c32")
        downManager.addTask("http://cdn.llsapp.com/forum/image/22f8389542734b05986c0b0dd8fd1735_1435230013392.jpg")
        downManager.addTask("http://cdn.llsapp.com/forum/image/2e6b8f9676aa47228aad74dd37709b0e_1446202991820.jpg")
//        downManager.addTask("http://cdn.llsapp.com/android/LLS-v4.0-595-20160908-143200.apk")
        downManager.addTask("http://cdn.llsapp.com/forum/image/f82192fa9f764af396579e51afeb9aaf_1435049606128.jpg")
        downManager.addTask("http://cdn.llsapp.com/forum/image/f74026981afa42e0b73a6983450deca1_1441780286505.jpg")

//        downManager.addTask("http://180.153.105.144/dd.myapp.com/16891/E2F3DEBB12A049ED921C6257C5E9FB11.apk")
//        var sss = DownloadManager
//        sss.get()

        "http://cdn.llsapp.com/android/LLS-v4.0-595-20160908-143200.apk"


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
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
