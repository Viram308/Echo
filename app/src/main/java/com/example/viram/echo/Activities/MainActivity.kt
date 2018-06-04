package com.example.viram.echo.Activities

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.example.viram.echo.Adaptors.navigation_adaptor
import com.example.viram.echo.R
import com.example.viram.echo.Fragments.mainf
import com.example.viram.echo.Fragments.songf
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity() {
    var listoftext: ArrayList<String> = arrayListOf()

    object Statified {
        var dl: DrawerLayout? = null
        var notificationManager: NotificationManager? = null
    }

    var trackNotificationBuilder: Notification? = null
    val listoficon: IntArray = intArrayOf(R.drawable.navigation_allsongs, R.drawable.navigation_favorites, R.drawable.navigation_settings, R.drawable.navigation_aboutus)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        MainActivity.Statified.dl = findViewById(R.id.drawer_layout)
        listoftext.add("All Songs")
        listoftext.add("Favorites")
        listoftext.add("Settings")
        listoftext.add("About us")


        var toggle = ActionBarDrawerToggle(this@MainActivity, MainActivity.Statified.dl, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        MainActivity.Statified.dl?.addDrawerListener(toggle)
        toggle.syncState()
        var mf = mainf()
        this.supportFragmentManager
                .beginTransaction()
                .add(R.id.frag, mf, "Main Screen Fragment")
                .commit()
        var nav_drawer_layout = navigation_adaptor(listoftext, listoficon, this)
        nav_drawer_layout.notifyDataSetChanged()

        var nav = findViewById<RecyclerView>(R.id.nav_recycle)
        nav.layoutManager = LinearLayoutManager(this@MainActivity)
        nav.itemAnimator = DefaultItemAnimator()
        nav.adapter = nav_drawer_layout
        nav.setHasFixedSize(true)
        val intent = Intent(this@MainActivity, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this@MainActivity, System.currentTimeMillis().toInt(), intent, 0)
        trackNotificationBuilder = Notification.Builder(this)
                .setContentTitle("A track is playing in background")
                .setSmallIcon(R.drawable.echo_logo)
                .setContentIntent(pIntent)
                .setOngoing(true)
                .setAutoCancel(true)
                .build()
        Statified.notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    }

    override fun onStart() {
        super.onStart()
        try {
            Statified.notificationManager?.cancel(1306)
        }catch (e:Exception)
        {
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            if (songf.Statified.mediaplayer?.isPlaying as Boolean)
            {
                Statified.notificationManager?.notify(1306,trackNotificationBuilder)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            Statified.notificationManager?.cancel(1306)
        }catch (e:Exception)
        {
            e.printStackTrace()
        }
    }
}