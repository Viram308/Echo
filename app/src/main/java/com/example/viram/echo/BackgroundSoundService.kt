package com.example.viram.echo

/**
 * Created by Viram on 6/18/2018.
 */

import android.annotation.SuppressLint
import java.io.File

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.os.IBinder
import android.util.Log
import com.example.viram.echo.Fragments.songf

class BackgroundSoundService : Service() {
    var player: MediaPlayer=songf.Statified.mediaplayer as MediaPlayer

    override fun onBind(arg0: Intent): IBinder? {

        return null
    }

    override fun onCreate() {
        super.onCreate()
     }

    @SuppressLint("WrongConstant")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int{
        val l: Int=1
        Log.e("onStartCommand", "onStartCommand")
        if (songf.Statified.mediaplayer?.isPlaying as Boolean) {
            songf.Statified.mediaplayer?.pause()
            UtilClass.pause = true

            Log.e("onStartCommand pause", "onStartCommand pause")
        } else {
            UtilClass.pause = false
            songf.Statified.mediaplayer?.start()
        }

        return l
    }

    override fun onStart(intent: Intent, startId: Int) {
        // TO DO
    }

    fun onUnBind(arg0: Intent): IBinder? {
        // TO DO Auto-generated method
        return null
    }

    fun onStop() {

    }

    fun onPause() {

    }

    override fun onDestroy() {
        UtilClass.playing = false
        songf.Statified.mediaplayer?.stop()
        songf.Statified.mediaplayer?.release()
    }

    override fun onLowMemory() {

    }

    companion object {

        private val TAG: String? = null
    }

}