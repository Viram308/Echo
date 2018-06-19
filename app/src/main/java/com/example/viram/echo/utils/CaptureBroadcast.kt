package com.example.viram.echo.utils

import android.app.Activity
import android.app.Service
import android.bluetooth.BluetoothClass
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.widget.Toast
import com.example.viram.echo.Activities.MainActivity
import com.example.viram.echo.Fragments.songf
import com.example.viram.echo.R
import android.support.v4.app.NotificationCompat.getExtras
import android.os.Bundle
import android.util.Log
import com.example.viram.echo.BackgroundSoundService
import com.example.viram.echo.UtilClass




/**
 * Created by Viram on 5/28/2018.
 */
class CaptureBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)
        var number: String? = ""
        val bundle = intent?.getExtras()


        if (state == TelephonyManager.EXTRA_STATE_RINGING) {
            // Phone is ringing
            number = bundle?.getString("incoming_number")
            Log.e("incoming_number", "incoming_number")
            songf.Statified.mediaplayer?.pause()
            songf.Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)
            songf.Statified.currentSongHelper.isPlaying=false

        } else if (state == TelephonyManager.EXTRA_STATE_OFFHOOK) {
            // Call received
            songf.Statified.mediaplayer?.pause()
            songf.Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)
            songf.Statified.currentSongHelper.isPlaying=false

        } else if (state == TelephonyManager.EXTRA_STATE_IDLE) {
            // Call Dropped or rejected


        }
        else if(intent?.action == Intent.ACTION_NEW_OUTGOING_CALL) {
            songf.Statified.mediaplayer?.pause()
            songf.Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)
            songf.Statified.currentSongHelper.isPlaying=false
        }
    }

//    private fun _stopServices(con: Context) {
//
//        if (songf.Statified.mediaplayer?.isPlaying == true) {
//            Log.e("start services", "start services")
//            val i = Intent(con, BackgroundSoundService::class.java)
//            con.startService(i)
//            songf.Statified.mediaplayer?.pause()
//        } else {
//            songf.Statified.mediaplayer?.pause()
//            Log.e("start not services", "start not services")
//        }
//    }
}
//   }     if (intent?.action == Intent.ACTION_NEW_OUTGOING_CALL) {
//            try {
//                MainActivity.Statified.notificationManager?.cancel(1306)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            try {
//                if (songf.Statified.mediaplayer?.isPlaying as Boolean) {
//                    songf.Statified.mediaplayer?.pause()
//                    songf.Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)
//
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        } else {
//            val tm: TelephonyManager = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//            when (tm?.callState) {
//                TelephonyManager.CALL_STATE_RINGING -> {
//                    try {
//                        MainActivity.Statified.notificationManager?.cancel(1306)
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                    try {
//                        if (songf.Statified.currentSongHelper.isPlaying) {
//                            Toast.makeText(context, "incoming", Toast.LENGTH_SHORT).show()
//                            songf.Statified.mediaplayer?.pause()
//                            songf.Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)
//
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//                else -> {
//
//                }
//            }
//        }
//    }

