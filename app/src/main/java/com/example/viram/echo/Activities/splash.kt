package com.example.viram.echo.Activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.example.viram.echo.R

class splash : AppCompatActivity() {
    var lop=arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.PROCESS_OUTGOING_CALLS,
            android.Manifest.permission.RECORD_AUDIO)

//    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
//        super.onSaveInstanceState(outState, outPersistentState)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
//        super.onRestoreInstanceState(savedInstanceState)
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if(!hasper(this@splash,*lop))
        {
            ActivityCompat.requestPermissions(this@splash,lop,131)

        }
        else
        {
            Handler().postDelayed({
                var s=Intent(this@splash, MainActivity::class.java)
                startActivity(s)
                this.finish()
            },1000)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            131->{
                if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED
                        && grantResults[1]==PackageManager.PERMISSION_GRANTED
                        && grantResults[2]==PackageManager.PERMISSION_GRANTED
                        && grantResults[3]==PackageManager.PERMISSION_GRANTED
                        && grantResults[4]==PackageManager.PERMISSION_GRANTED)
                {
                    Handler().postDelayed({
                        var s=Intent(this@splash, MainActivity::class.java)
                        startActivity(s)
                        this.finish()
                    },1000)
                }
                else
                {
                    Toast.makeText(this@splash,"Please Allow All The Permissions to Continue",Toast.LENGTH_SHORT).show()

                    this.finish()
                }
                return

            }
            else->
            {
                Toast.makeText(this@splash,"Something went wrong",Toast.LENGTH_SHORT).show()
                this.finish()
                return
            }

        }
    }
    fun hasper(context: Context,vararg permissions: String): Boolean
    {
        var has: Boolean=true
        for(p in permissions)
        {
            val res=context.checkCallingOrSelfPermission(p)
            if(res!=PackageManager.PERMISSION_GRANTED)
            {
                has=false
            }
        }
        return has
    }
}
