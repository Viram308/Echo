package com.example.viram.echo.utils

import android.app.Service
import android.bluetooth.BluetoothClass
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.example.viram.echo.Activities.MainActivity
import com.example.viram.echo.Fragments.songf
import com.example.viram.echo.R

/**
 * Created by Viram on 5/28/2018.
 */
class CaptureBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_NEW_OUTGOING_CALL) {
            try {
                MainActivity.Statified.notificationManager?.cancel(1306)
            }catch (e:Exception)
            {
                e.printStackTrace()
            }
           try {
               if (songf.Statified.mediaplayer?.isPlaying as Boolean)
               {
                   songf.Statified.mediaplayer?.pause()
                   songf.Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)

               }
           }catch (e: Exception)
           {
               e.printStackTrace()
           }
        }else
        {
            val tm: TelephonyManager=context?.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            when(tm.callState)
            {
                TelephonyManager.CALL_STATE_RINGING->{
                    try {
                        MainActivity.Statified.notificationManager?.cancel(1306)
                    }catch (e:Exception)
                    {
                        e.printStackTrace()
                    }
                    try {
                        if (songf.Statified.mediaplayer?.isPlaying as Boolean)
                        {
                            songf.Statified.mediaplayer?.pause()
                            songf.Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)

                        }
                    }catch (e: Exception)
                    {
                        e.printStackTrace()
                    }
                }
                else->
                {

                }
            }
        }
    }

}