package com.example.viram.echo.Adaptors

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.viram.echo.Activities.MainActivity
import com.example.viram.echo.Fragments.aboutf
import com.example.viram.echo.Fragments.favf
import com.example.viram.echo.Fragments.mainf
import com.example.viram.echo.Fragments.setf
import com.example.viram.echo.R

/**
 * Created by Viram on 5/18/2018.
 */
class navigation_adaptor(content: ArrayList<String>,icons: IntArray,context: Context) : RecyclerView.Adapter<navigation_adaptor.NavViewHolder>()
{
    var _content: ArrayList<String>?=null
    var _images: IntArray?=null
    var _context: Context?=null
    init {
        this._content=content
        this._images=icons
        this._context=context
    }

    override fun onBindViewHolder(holder: NavViewHolder?, position: Int)
    {
        holder?.icon?.setBackgroundResource(_images?.get(position) as Int)
        holder?.text?.setText(_content?.get(position))
        holder?.layout?.setOnClickListener({
            if(position==0)
            {
                val mainf=mainf()
                (_context as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frag,mainf)
                        .commit()
            }
            else if(position==1)
            {
                val favf=favf()
                (_context as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .addToBackStack("Favorites")
                        .replace(R.id.frag,favf)
                        .commit()
            }
            else if(position==2)
            {
                val setf=setf()
                (_context as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .addToBackStack("Settings")
                        .replace(R.id.frag,setf)
                        .commit()
            }
            else if(position==3)
            {
                val aboutf=aboutf()
                (_context as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .addToBackStack("About Us")
                        .replace(R.id.frag,aboutf)
                        .commit()
            }
            MainActivity.Statified.dl?.closeDrawers()
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NavViewHolder {
      var itemview=LayoutInflater.from(parent?.context)
              .inflate(R.layout.raws_for_navigation,parent,false)
        var returnthis=NavViewHolder(itemview)
        return returnthis
    }

    override fun getItemCount(): Int {
        return (_content as ArrayList).size
       }

    class NavViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
    {
        var icon: ImageView?=null
        var text: TextView?=null
        var layout: RelativeLayout?=null
        init {
            icon= itemView?.findViewById(R.id.images)
            text=itemView?.findViewById(R.id.texts)
            layout=itemView?.findViewById(R.id.nav_layout)
        }
    }
}