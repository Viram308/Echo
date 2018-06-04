package com.example.viram.echo.Fragments


import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.viram.echo.Adaptors.FavoriteAdaptor
import com.example.viram.echo.Databases.EchoDatabase
import com.example.viram.echo.R
import com.example.viram.echo.Songs
import kotlinx.android.synthetic.main.fragment_favf.*

/**
 * A simple [Fragment] subclass.
 */
class favf : Fragment() {
    var myactivity: Activity? = null
    var noFavorites: TextView? = null
    var nowPlayingBottomBar: RelativeLayout? = null
    var playPauseButton: ImageButton? = null
    var songTitle: TextView? = null
    var recyclerView: RecyclerView? = null
    var trackPosition: Int = 0
    /*This variable will be used for database instance*/
    var favoriteContent: EchoDatabase? = null
    /*Variable to store favorites*/
    var refreshList: ArrayList<Songs> = arrayListOf()
    var getListfromDatabase: ArrayList<Songs>? = null

    object Statified {
        var mediaPlayer: MediaPlayer? = null
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        display_favorites_by_searching()
        bottomBarSetup()



    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_favf, container, false)
        activity.title = "Favorites"
        setHasOptionsMenu(true)
        favoriteContent = EchoDatabase(myactivity)
        noFavorites = view?.findViewById(R.id.noFavorites)
        nowPlayingBottomBar = view?.findViewById(R.id.hiddenbarfavscreen)
        songTitle = view.findViewById(R.id.songtitlefav)
        playPauseButton = view.findViewById(R.id.playpausebutton)
        recyclerView = view.findViewById<RecyclerView>(R.id.favoriteRecycler)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val item = menu?.findItem(R.id.action_sort)
        item?.isVisible = false
    }

    /*As the name suggests, this function is used to fetch the songs present in your phones
    and returns the arraylist of the same*/
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


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
/*The bottom bar setup function is used to place the bottom bar on the favorite screen
    when we come back from the song playing screen to the favorite screen*/
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
                    songf.Statified.currentSongHelper?.songArtist)
            args.putString("songTitle",
                    songf.Statified.currentSongHelper?.songTitle)
            args.putString("songPath",
                    songf.Statified.currentSongHelper?.songPath)
            args.putInt("songId",
                    songf.Statified.currentSongHelper?.songId?.toInt() as Int)
            args.putInt("songPosition",
                    songf.Statified.currentSongHelper?.currentPosition?.toInt() as Int)
            args.putParcelableArrayList("songData",
                    songf.Statified.fetchSongs)
/*Here we put the additional string in the bundle
* this tells us that the bottom bar was successfully setup*/
            args.putString("FavBottomBar", "success")
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
                    .addToBackStack("SongPlayingFragmentFavBottom")
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
                trackPosition = songf.Statified.mediaplayer?.getCurrentPosition() as Int
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
    }

    /*The below function is used to search the favorites and display*/
    fun display_favorites_by_searching() {

/*Checking if database has any entry or not*/
        if (favoriteContent?.checkSize() as Int > 0) {

/*New list for storing the favorites*/


/*Getting the list of songs from database*/
            getListfromDatabase = favoriteContent?.queryDBList()
/*Getting list of songs from phone storage*/
            val fetchListfromDevice: ArrayList<Songs>? = getsong()
/*If there are no songs in phone then there cannot be any favorites*/
            if (fetchListfromDevice != null) {
                var id: Int = 0


                /*Then we check all the songs in the phone*/
                for (i in 0..fetchListfromDevice.size - 1) {
/*We iterate through every song in database*/


/*While iterating through all the songs we check for the songs
which are in both the lists
* i.e. the favorites songs*/
                    id = fetchListfromDevice.get(i).songID.toInt()

                    if (favoriteContent?.checkifIdExists(id) as Boolean) {
                        var current_id = fetchListfromDevice[i].songID
                        var current_title = fetchListfromDevice[i].songTitle
                        var current_artist = fetchListfromDevice[i].artist
                        var current_songpath = fetchListfromDevice[i].songData
                        var current_dateadded = fetchListfromDevice[i].dateAdded

                        refreshList.add(Songs(current_id, current_title, current_artist, current_songpath, current_dateadded))
                       }

                    }

            } else {
            }
            if (refreshList == null) {
                recyclerView?.visibility = View.INVISIBLE
                noFavorites?.visibility = View.VISIBLE
            } else {
/*Else we setup our recycler view for displaying the favorite songs*/
                val favoriteAdapter = FavoriteAdaptor(refreshList as ArrayList<Songs>, myactivity as Context)
                val mLayoutManager = LinearLayoutManager(myactivity)
                recyclerView?.layoutManager = mLayoutManager
                recyclerView?.itemAnimator = DefaultItemAnimator()
                recyclerView?.adapter = favoriteAdapter
                recyclerView?.setHasFixedSize(true)
            }
/*If refresh list is null we display that there are no favorites*/

        } else {
/*If initially the checkSize() function returned 0 then also we display the no
favorites present message*/
            recyclerView?.visibility = View.INVISIBLE
            noFavorites?.visibility = View.VISIBLE
        }
    }
}