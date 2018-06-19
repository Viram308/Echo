package com.example.viram.echo.Fragments


import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.viram.echo.Adaptors.mainscreen_adaptor
import com.example.viram.echo.R
import com.example.viram.echo.Songs
import kotlinx.android.synthetic.*
import java.util.*


class mainf : Fragment() {
    var nowPlayingBottomBar: RelativeLayout? = null
    var trackPosition = 0
    var visiblelayout: RelativeLayout? = null
    var mainrecycle: RecyclerView? = null
    var playPauseButton: ImageButton? = null
    var songTitle: TextView? = null
    var nosongs: RelativeLayout? = null
    var getsonglist: ArrayList<Songs>? = null
    var myactivity: Activity? = null
    var mainscreenf: mainscreen_adaptor? = null

    object Statified {
        var mediaPlayer: MediaPlayer? = null
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getsonglist = getsong()
        /*Declaring the preferences to save the sorting order which we select*/
        val prefs = myactivity?.getSharedPreferences("action_sort", Context.MODE_PRIVATE)
        val action_sort_ascending = prefs?.getString("action_sort_ascending", "true")
        val action_sort_recent = prefs?.getString("action_sort_recent", "false")
/*If there are no songs we do not display the list instead we display no songs
message*/
        if (getsonglist == null) {
            mainrecycle?.visibility = View.INVISIBLE
            nosongs?.visibility = View.VISIBLE
        } else {
            mainscreenf = mainscreen_adaptor(getsonglist as ArrayList<Songs>, myactivity as Context)
            val mLayoutManager = LinearLayoutManager(myactivity)

            /*Here we put assign our layout manager to the recycler view's layout manager*/
            mainrecycle?.layoutManager = mLayoutManager

            /*It is similar to the item animator we used in the navigation drawer*/
            mainrecycle?.itemAnimator = DefaultItemAnimator()

            /*Finally we set the adapter to the recycler view*/
            mainrecycle?.adapter = mainscreenf
            mainrecycle?.setHasFixedSize(true)

        }
        if (getsonglist != null) {
            if (action_sort_ascending!!.equals("true", ignoreCase = true)) {
                Collections.sort(getsonglist, Songs.Statified.nameComparator)
                mainscreenf?.notifyDataSetChanged()
            } else if (action_sort_recent!!.equals("true", ignoreCase = true)) {
                Collections.sort(getsonglist, Songs.Statified.dateComparator)
                mainscreenf?.notifyDataSetChanged()
            }
        }
        bottomBarSetup()
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        activity.title = "All Songs"
        setHasOptionsMenu(true)
        var view = inflater!!.inflate(R.layout.fragment_mainf, container, false)
        nowPlayingBottomBar = view?.findViewById<RelativeLayout>(R.id.hiddenbar)
        visiblelayout = view?.findViewById<RelativeLayout>(R.id.visiblelayout)
        mainrecycle = view?.findViewById<RecyclerView>(R.id.mainrecycle)
        playPauseButton = view?.findViewById<ImageButton>(R.id.playpausebutton)
        songTitle = view?.findViewById<TextView>(R.id.songtitle)
        nosongs = view?.findViewById<RelativeLayout>(R.id.nosongs)

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        myactivity = context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myactivity = activity
    }

    fun getsong(): ArrayList<Songs> {
        var arraylist = arrayListOf<Songs>()
        val contentresolver = myactivity?.contentResolver
        val songuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val songcursor = contentresolver?.query(songuri, null, null, null, null)
        if (songcursor != null && songcursor.moveToFirst()) {
            val songid = songcursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val songtitle = songcursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val songartist = songcursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songdata = songcursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val songdate = songcursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
            do {
                var currentid = songcursor.getLong(songid)
                var currenttitle = songcursor.getString(songtitle)
                var currentartist = songcursor.getString(songartist)
                var currentdata = songcursor.getString(songdata)
                var currentdate = songcursor.getLong(songdate)
                arraylist.add(Songs(currentid, currenttitle, currentartist, currentdata, currentdate))
            } while (songcursor.moveToNext())
        }
        return arraylist
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main, menu)

    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val item2: MenuItem? = menu?.findItem(R.id.action_sort)
        item2?.isVisible = true
        val item: MenuItem? = menu?.findItem(R.id.action_redirect)
        item?.isVisible = false

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val switcher = item?.itemId
        if (switcher == R.id.action_sort_ascending) {
            val editor = myactivity?.getSharedPreferences("action_sort",
                    Context.MODE_PRIVATE)?.edit()
            editor?.putString("action_sort_ascending", "true")
            editor?.putString("action_sort_recent", "false")
            editor?.apply()
            if (getsonglist != null) {
                Collections.sort(getsonglist, Songs.Statified.nameComparator)
            }
            mainscreenf?.notifyDataSetChanged()
            return false
        } else if (switcher == R.id.action_sort_recent) {
            val editortwo = myactivity?.getSharedPreferences("action_sort",
                    Context.MODE_PRIVATE)?.edit()
            editortwo?.putString("action_sort_recent", "true")
            editortwo?.putString("action_sort_ascending", "false")
            editortwo?.apply()
            if (getsonglist != null) {
                Collections.sort(getsonglist, Songs.Statified.dateComparator)
            }
            mainscreenf?.notifyDataSetChanged()
            return false
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun bottomBarSetup() {
        try {
/*Calling the click handler function will help us handle the click events of
the bottom bar*/

/*We fetch the song title with the help of the current song helper and set it
to the song title in our bottom bar*/

/*While coming back from the song playing screen
* if the song was playing then only the bottom bar is placed, else not
placed*/
            if (songf.Statified.mediaplayer?.isPlaying as Boolean) {
                nowPlayingBottomBar?.visibility = View.VISIBLE

            } else {
                nowPlayingBottomBar?.visibility = View.INVISIBLE
            }
            bottomBarClickHandler()
            songTitle?.setText(songf.Statified.currentSongHelper?.songTitle)
/*If we are the on the favorite screen and not on the song playing screen when
the song finishes
* we want the changes in the song to reflect on the favorite screen hence we
call the onSongComplete() function which help us in maintaining consistency*/
            songf.Statified.mediaplayer?.setOnCompletionListener({

                songf.Staticated.onSongComplete()
                songTitle?.setText(songf.Statified.currentSongHelper?.songTitle)
            })
/*Since we are dealing with the media player object which can be null, hence
we handle all such exceptions using the try-catch block*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /*The bottomBarClickHandler() function is used to handle the click events on the bottom
    bar*/
    fun bottomBarClickHandler() {

/*We place a click listener on the bottom bar*/
        nowPlayingBottomBar?.setOnClickListener({
            /*Using the same media player object*/
            Statified.mediaPlayer = songf.Statified.mediaplayer
            val songPlayingFragment = songf()
            var args = Bundle()
/*Here when we click on the bottom bar, we navigate to the song playing
fragment
* Since we want the details of the same song which is playing to be displayed
in the song playing fragment
* we pass the details of the current song being played to the song playing
fragment using Bundle*/
            args.putString("songArtist",
                    songf.Statified.currentSongHelper.songArtist)
            args.putString("songTitle",
                    songf.Statified.currentSongHelper.songTitle)
            args.putString("songPath",
                    songf.Statified.currentSongHelper.songPath)
            args.putInt("songId",
                    songf.Statified.currentSongHelper.songId?.toInt() as Int)
            args.putInt("songPosition",
                    songf.Statified.currentSongHelper.currentPosition?.toInt() as Int)
            args.putParcelableArrayList("songData",
                    songf.Statified.fetchSongs)
/*Here we put the additional string in the bundle
* this tells us that the bottom bar was successfully setup*/
            args.putString("MainBottomBar", "success")
/*Here we pass the bundle object to the song playing fragment*/
            songPlayingFragment.arguments = args
/*The below lines are now familiar
* These are used to open a fragment*/
            fragmentManager.beginTransaction()
                    .replace(R.id.frag, songPlayingFragment)
/*The below piece of code is used to handle the back navigation
* This means that when you click the bottom bar and move on to the
next screen
* on pressing back button you navigate to the screen you came from*/
                    .addToBackStack("SongPlayingFragmentMainBottom")
                    .commit()
        })
/*Apart from the click on the bottom bar we have a play/pause button in our bottom
bar
* This button is used to play or pause the media player*/
        playPauseButton?.setOnClickListener({
            if (songf.Statified.mediaplayer?.isPlaying as Boolean) {
/*If the song was already playing, we then pause it and save the it's
position
* and then change the button to play button*/
                songf.Statified.mediaplayer?.pause()
                trackPosition = songf.Statified.mediaplayer?.currentPosition
                        as Int
                playPauseButton?.setBackgroundResource(R.drawable.play_icon)
            } else {
/*If the music was already paused and we then click on the button
* it plays the song from the same position where it was paused
* and change the button to pause button*/
                songf.Statified.mediaplayer?.seekTo(trackPosition)
                songf.Statified.mediaplayer?.start()
                playPauseButton?.setBackgroundResource(R.drawable.pause_icon)
            }
        })
    }// Required empty public constructor
}
