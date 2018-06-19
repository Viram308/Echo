package com.example.viram.echo.Fragments


import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.cleveroad.audiovisualization.AudioVisualization
import com.cleveroad.audiovisualization.DbmHandler
import com.cleveroad.audiovisualization.GLAudioVisualizationView
import com.example.viram.echo.CurrentSongHelper
import com.example.viram.echo.Databases.EchoDatabase
import com.example.viram.echo.R
import com.example.viram.echo.Songs
import kotlinx.android.synthetic.main.raws_for_mainscreen.*
import java.util.*
import java.util.concurrent.TimeUnit
import android.util.Log


/**
 * A simple [Fragment] subclass.
 */
class songf : Fragment() {


    object Statified {

        var mSensorManager: SensorManager? = null
        var mSensorListener: SensorEventListener? = null
        var fab: ImageButton? = null
        var audioVisualization: AudioVisualization? = null
        var glView: GLAudioVisualizationView? = null
        var myactivity: Activity? = null
        var currentSongHelper: CurrentSongHelper = CurrentSongHelper()
        var startTimeText: TextView? = null
        var endTimeText: TextView? = null
        var playPauseImageButton: ImageButton? = null
        var previousImageButton: ImageButton? = null
        var nextImageButton: ImageButton? = null
        var loopImageButton: ImageButton? = null
        var shuffleImageButton: ImageButton? = null
        var seekBar: SeekBar? = null
        var songArtistView: TextView? = null
        var songTitleView: TextView? = null
        var currentPosition: Int = 0
        var fetchSongs: ArrayList<Songs> = arrayListOf()
        var mediaplayer: MediaPlayer? = null
        var favoriteContent: EchoDatabase? = null
        var trackPosition = 0
        var MY_PREFS_NAME = "ShakeFeature"
        var updateSongTime = object : Runnable {
            override fun run() {

//Statified.seekBar?.setProgress(Statified.mediaplayer?.currentPosition as Int)
                /*Retrieving the current time position of the media player*/

                /*The start time is set to the current position of the song
                * The TimeUnit class changes the units to minutes and milliseconds and applied to the string
                * The %d:%d is used for formatting the time strings as 03:45 so that it appears like time*/
                Statified.seekBar?.setProgress(Statified.mediaplayer?.currentPosition as Int)
                initlistener()
                if (TimeUnit.MILLISECONDS.toMinutes(Statified.mediaplayer?.currentPosition?.toLong() as Long) > 9 && TimeUnit.MILLISECONDS.toSeconds(Statified.mediaplayer?.currentPosition?.toLong() as Long)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Statified.mediaplayer?.currentPosition?.toLong() as Long)) > 9) {
                    Statified.startTimeText?.setText(String.format("%d:%d",
                            TimeUnit.MILLISECONDS.toMinutes(Statified.mediaplayer?.currentPosition?.toLong() as Long),
                            TimeUnit.MILLISECONDS.toSeconds(Statified.mediaplayer?.currentPosition?.toLong() as Long)
                                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Statified.mediaplayer?.currentPosition?.toLong() as Long))))

                    /*Similar to above is done for the end time text*/

                } else if (TimeUnit.MILLISECONDS.toMinutes(Statified.mediaplayer?.currentPosition?.toLong() as Long) <= 9 && TimeUnit.MILLISECONDS.toSeconds(Statified.mediaplayer?.currentPosition?.toLong() as Long)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Statified.mediaplayer?.currentPosition?.toLong() as Long)) > 9) {
                    Statified.startTimeText?.setText(String.format("0%d:%d",
                            TimeUnit.MILLISECONDS.toMinutes(Statified.mediaplayer?.currentPosition?.toLong() as Long),
                            TimeUnit.MILLISECONDS.toSeconds(Statified.mediaplayer?.currentPosition?.toLong() as Long)
                                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Statified.mediaplayer?.currentPosition?.toLong() as Long))))

                    /*Similar to above is done for the end time text*/

                } else if (TimeUnit.MILLISECONDS.toMinutes(Statified.mediaplayer?.currentPosition?.toLong() as Long) > 9 && TimeUnit.MILLISECONDS.toSeconds(Statified.mediaplayer?.currentPosition?.toLong() as Long)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Statified.mediaplayer?.currentPosition?.toLong() as Long)) <= 9) {
                    Statified.startTimeText?.setText(String.format("%d:0%d",
                            TimeUnit.MILLISECONDS.toMinutes(Statified.mediaplayer?.currentPosition?.toLong() as Long),
                            TimeUnit.MILLISECONDS.toSeconds(Statified.mediaplayer?.currentPosition?.toLong() as Long)
                                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Statified.mediaplayer?.currentPosition?.toLong() as Long))))

                    /*Similar to above is done for the end time text*/

                } else if (TimeUnit.MILLISECONDS.toMinutes(Statified.mediaplayer?.currentPosition?.toLong() as Long) <= 9 && TimeUnit.MILLISECONDS.toSeconds(Statified.mediaplayer?.currentPosition?.toLong() as Long)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Statified.mediaplayer?.currentPosition?.toLong() as Long)) <= 9) {
                    Statified.startTimeText?.setText(String.format("0%d:0%d",
                            TimeUnit.MILLISECONDS.toMinutes(Statified.mediaplayer?.currentPosition?.toLong() as Long),
                            TimeUnit.MILLISECONDS.toSeconds(Statified.mediaplayer?.currentPosition?.toLong() as Long)
                                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Statified.mediaplayer?.currentPosition?.toLong() as Long))))

                    /*Similar to above is done for the end time text*/

                }


                /*Since updating the time at each second will take a lot of processing, so we perform this task on the different thread using Handler*/
                Handler().postDelayed(this, 1000)
            }

            private fun initlistener() {
               // Log.e("gayo", "gayo")
                Statified.seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                        if (b) {
                            Statified.mediaplayer?.seekTo(i)
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                    }
                }) //To change body of created functions use File | Settings | File Templates.
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        val view = inflater!!.inflate(R.layout.fragment_songf, container, false)
        activity.title = "Now Playing"
        Statified.seekBar = view?.findViewById(R.id.seekbar)
        Statified.startTimeText = view?.findViewById(R.id.starttime)
        Statified.endTimeText = view?.findViewById(R.id.endtime)
        Statified.playPauseImageButton = view?.findViewById(R.id.playpause)
        Statified.nextImageButton = view?.findViewById(R.id.next)
        Statified.previousImageButton = view?.findViewById(R.id.previous)
        Statified.loopImageButton = view?.findViewById(R.id.loop)
        Statified.shuffleImageButton = view?.findViewById(R.id.shuffle)
        Statified.songArtistView = view?.findViewById(R.id.songartistsongf)
        Statified.songTitleView = view?.findViewById(R.id.songtitlesongf)
        Statified.glView = view?.findViewById(R.id.visualizer_view)
        Statified.fab = view?.findViewById(R.id.favouriteicon)
        Statified.fab?.alpha = 0.8f
        return view
    }

    object Staticated {
        var MY_PREFS_SHUFFLE = "Shuffle feature"
        var MY_PREFS_LOOP = "Loop feature"
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
        fun onSongComplete() {

            /*If shuffle was on then play a random next song*/
            if (Statified.currentSongHelper.isshuffle as Boolean) {
                playNext("PlayNextLikeNormalShuffle")
                Statified.currentSongHelper.isPlaying = true
            } else {

                /*If loop was ON, then play the same ong again*/
                if (Statified.currentSongHelper.isloop as Boolean) {
                    Statified.currentSongHelper.isPlaying = true
                    val nextSong = Statified.fetchSongs?.get(Statified.currentPosition)
                    Statified.currentSongHelper.currentPosition = Statified.currentPosition
                    Statified.currentSongHelper.songPath = nextSong?.songData
                    Statified.currentSongHelper.songTitle = nextSong?.songTitle
                    Statified.currentSongHelper.songArtist = nextSong?.artist
                    Statified.currentSongHelper.songId = nextSong?.songID as Long
                    updatetextview(Statified.currentSongHelper.songTitle as String, Statified.currentSongHelper.songArtist as String)
                    Statified.mediaplayer?.reset()
                    try {
                        Statified.mediaplayer?.setDataSource(Statified.myactivity, Uri.parse(Statified.currentSongHelper.songPath))
                        Statified.mediaplayer?.prepare()
                        Statified.mediaplayer?.start()
                        processInformation(Statified.mediaplayer as MediaPlayer)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {

                    /*If loop was OFF then normally play the next song*/
                    playNext("PlayNextNormal")
                    Statified.currentSongHelper.isPlaying = true
                }
            }
            if (Statified.favoriteContent?.checkifIdExists(Statified.currentSongHelper.songId?.toInt() as Int) as Boolean) {
                Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myactivity, R.drawable.favorite_on))
            } else {
                Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myactivity, R.drawable.favorite_off))
            }
        }

        fun updatetextview(songtitle: String, songartist: String) {
            var songTitleUpdated = songtitle
            var songartistUpdated = songartist
            if (songtitle.equals("<unknown>", true)) {
                songTitleUpdated = "unknown"
            }
            if (songartist.equals("<unknown>", true)) {
                songartistUpdated = "unknown"
            }
            Statified.songTitleView?.setText(songTitleUpdated)
            Statified.songArtistView?.setText(songartistUpdated)
        }

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
        fun processInformation(mediaPlayer: MediaPlayer) {
            /*Obtaining the final time*/
            val finalTime = mediaPlayer.duration

            /*Obtaining the current position*/
            val startTime = mediaPlayer.currentPosition
            Statified.seekBar?.max = finalTime
            /*Here we format the time and set it to the start time text*/
            if (TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()) > 9 && TimeUnit.MILLISECONDS.toSeconds(startTime.toLong())
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong())) > 9) {
                Statified.startTimeText?.setText(String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(startTime.toLong())
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()))))

                /*Similar to above is done for the end time text*/

            } else if (TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()) > 9 && TimeUnit.MILLISECONDS.toSeconds(startTime.toLong())
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong())) <= 9) {
                Statified.startTimeText?.setText(String.format("%d:0%d",
                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(startTime.toLong())
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()))))

                /*Similar to above is done for the end time text*/

            } else if (TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()) <= 9 && TimeUnit.MILLISECONDS.toSeconds(startTime.toLong())
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong())) > 9) {
                Statified.startTimeText?.setText(String.format("0%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(startTime.toLong())
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()))))


            } else if (TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()) <= 9 && TimeUnit.MILLISECONDS.toSeconds(startTime.toLong())
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong())) <= 9) {
                Statified.startTimeText?.setText(String.format("0%d:0%d",
                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(startTime.toLong())
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()))))

                /*Similar to above is done for the end time text*/

            }
            if (TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()) > 9 && TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong())
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong())) > 9) {
                Statified.endTimeText?.setText(String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong())
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()))))

                /*Similar to above is done for the end time text*/

            } else if (TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()) <= 9 && TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong())
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong())) > 9) {
                Statified.endTimeText?.setText(String.format("0%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong())
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()))))

                /*Similar to above is done for the end time text*/

            } else if (TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()) > 9 && TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong())
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong())) <= 9) {
                Statified.endTimeText?.setText(String.format("%d:0%d",
                        TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong())
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()))))

                /*Similar to above is done for the end time text*/

            } else if (TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()) <= 9 && TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong())
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong())) <= 9) {
                Statified.endTimeText?.setText(String.format("0%d:0%d",
                        TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong())
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()))))

                /*Similar to above is done for the end time text*/

            }
            Statified.seekBar?.setProgress(Statified.mediaplayer?.currentPosition as Int)

            /*Seekbar has been assigned this time so that it moves according to the time of song*/


            /*Now this task is synced with the update song time obhect*/
            Handler().postDelayed(Statified.updateSongTime, 1000)
        }


        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
        fun playNext(check: String) {

            /*Let this one sit for a while, We'll explain this after the next section where we will be teaching to add the next and previous functionality*/
            if (check.equals("PlayNextNormal", true)) {
                Statified.currentPosition = Statified.currentPosition + 1
            } else if (check.equals("PlayNextLikeNormalShuffle", true)) {
                var randomObject = Random()
                var randomPosition = randomObject.nextInt(Statified.fetchSongs?.size?.plus(1) as Int)
                Statified.currentPosition = randomPosition
            }
            if (Statified.currentSongHelper.isPlaying as Boolean) {
                Statified.playPauseImageButton?.setBackgroundResource(R.drawable.pause_icon)
            } else {
                Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)
            }
            if (Statified.currentPosition == Statified.fetchSongs?.size) {
                Statified.currentPosition = 0
            }
            Statified.currentSongHelper.isloop = false
            Statified.loopImageButton?.setBackgroundResource(R.drawable.loop_white_icon)
            val nextSong = Statified.fetchSongs.get(Statified.currentPosition)
            Statified.currentSongHelper.songPath = nextSong.songData
            Statified.currentSongHelper.songTitle = nextSong.songTitle
            Statified.currentSongHelper.songArtist = nextSong.artist
            Statified.currentSongHelper.songId = nextSong.songID
            updatetextview(Statified.currentSongHelper.songTitle as String, Statified.currentSongHelper.songArtist as String)
            Statified.mediaplayer?.reset()
            try {
                Statified.mediaplayer?.setDataSource(Statified.myactivity, Uri.parse(Statified.currentSongHelper.songPath))
                Statified.mediaplayer?.prepare()
                Statified.mediaplayer?.start()
                processInformation(Statified.mediaplayer as MediaPlayer)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (Statified.favoriteContent?.checkifIdExists(Statified.currentSongHelper.songId?.toInt() as Int) as Boolean) {
                Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myactivity, R.drawable.favorite_on))
            } else {
                Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myactivity, R.drawable.favorite_off))
            }
        }
    }// Required empty public constructor

    var mAccelaration: Float = 0f
    var mAccelarationCurrent: Float = 0f
    var mAccelarationLast: Float = 0f


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Statified.audioVisualization = Statified.glView as AudioVisualization
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Statified.myactivity = context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        Statified.myactivity = activity
    }

    override fun onResume() {
        super.onResume()
        Statified.audioVisualization?.onResume()
        Statified.mSensorManager?.registerListener(Statified.mSensorListener, Statified.mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)

    }

    override fun onPause() {
        super.onPause()
        Statified.audioVisualization?.onPause()
        Statified.mSensorManager?.unregisterListener(Statified.mSensorListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Statified.audioVisualization?.release()
    }

    fun initlistener() {


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Log.e("avi gayo", " avi gayo")


        Statified.mSensorManager = Statified.myactivity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelaration = 0.0f
        mAccelarationCurrent = SensorManager.GRAVITY_EARTH
        mAccelarationLast = SensorManager.GRAVITY_EARTH
        bindShakeListener()
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.song_playing_menu, menu)
        if (Statified.seekBar != null) {
            var value: Int? = null
            if (Statified.mediaplayer != null) {
                value = Statified.mediaplayer?.duration

            }
            Statified.seekBar?.max = value as Int
        }


    }


    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val item: MenuItem? = menu?.findItem(R.id.action_redirect)
        item?.isVisible = true
        val item2: MenuItem? = menu?.findItem(R.id.action_sort)
        item2?.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_redirect -> {
                Statified.myactivity?.onBackPressed()
                return false
            }

        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Statified.favoriteContent = EchoDatabase(Statified.myactivity)
        Statified.currentSongHelper = CurrentSongHelper()
        var path: String? = null
        Statified.currentSongHelper.isPlaying = true
        Statified.currentSongHelper.isloop = false
        Statified.currentSongHelper.isshuffle = false

        var songtitle: String? = null
        var songartist: String? = null
        var songid: Long = 0
        try {

            path = arguments.getString("path")
            songtitle = arguments.getString("songtitle")
            songartist = arguments.getString("songartist")
            songid = arguments.getInt("songid").toLong()
            Statified.currentPosition = arguments.getInt("songposition")
            Statified.fetchSongs = arguments.getParcelableArrayList("songdetails")

            /*Now store the song details to the current song helper object so that they can be used later*/
            Statified.currentSongHelper.songPath = path
            Statified.currentSongHelper.songTitle = songtitle
            Statified.currentSongHelper.songArtist = songartist
            Statified.currentSongHelper.songId = songid
            Statified.currentSongHelper.currentPosition = Statified.currentPosition
            Staticated.updatetextview(Statified.currentSongHelper.songTitle as String, Statified.currentSongHelper.songArtist as String)


        } catch (e: Exception) {
            e.printStackTrace()
        }
        var fromfavbottombar = arguments.get("FavBottomBar") as? String
        var frommainbottombar = arguments.get("MainBottomBar") as? String
        if (fromfavbottombar != null) {
            Statified.mediaplayer = favf.Statified.mediaPlayer
            var songTitleUpdated = arguments.getString("songTitle")
            var songartistUpdated = arguments.getString("songArtist")
            var sid = arguments.getInt("songId").toLong()
            var spath = arguments.getString("songPath")
            Statified.currentSongHelper.songId = sid
            Statified.currentSongHelper.songTitle = songTitleUpdated
            Statified.currentSongHelper.songArtist = songartistUpdated
            Statified.currentSongHelper.songPath = spath
            //Statified.songTitleView?.setText(songTitleUpdated)
            //Statified.songArtistView?.setText(songartistUpdated)
            //if(Statified.favoriteContent?.checkifIdExists(Statified.currentSongHelper.songId.toInt()) as Boolean)
            Staticated.updatetextview(Statified.currentSongHelper.songTitle as String, Statified.currentSongHelper.songArtist as String)
            Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myactivity, R.drawable.favorite_on))
            if (Statified.mediaplayer?.isPlaying as Boolean) {
                Statified.playPauseImageButton?.setBackgroundResource(R.drawable.pause_icon)
                Statified.currentSongHelper.isPlaying = true
            } else {
                Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)
                Statified.currentSongHelper.isPlaying = false
            }

        } else if (frommainbottombar != null) {
            Statified.mediaplayer = mainf.Statified.mediaPlayer
            var songTitleUpdated = arguments.getString("songTitle")
            var songartistUpdated = arguments.getString("songArtist")
            var sid = arguments.getInt("songId").toLong()
            var spath = arguments.getString("songPath")
            Statified.currentSongHelper.songTitle = songTitleUpdated
            Statified.currentSongHelper.songArtist = songartistUpdated
            Statified.currentSongHelper.songPath = spath
            Statified.currentSongHelper.songId = sid
            //Statified.songTitleView?.setText(songTitleUpdated)
            //Statified.songArtistView?.setText(songartistUpdated)
            Staticated.updatetextview(Statified.currentSongHelper.songTitle as String, Statified.currentSongHelper.songArtist as String)
            if (Statified.mediaplayer?.isPlaying as Boolean) {
                Statified.playPauseImageButton?.setBackgroundResource(R.drawable.pause_icon)
                Statified.currentSongHelper.isPlaying = true
            } else {
                Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)
                Statified.currentSongHelper.isPlaying = false
            }

        } else {
            Statified.mediaplayer = MediaPlayer()
            Statified.mediaplayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {
                Statified.mediaplayer?.setDataSource(Statified.myactivity, Uri.parse(path))
                Statified.mediaplayer?.prepare()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Statified.mediaplayer?.start()
        }
        Staticated.processInformation(Statified.mediaplayer as MediaPlayer)
        if (Statified.currentSongHelper.isPlaying as Boolean) {
            Statified.playPauseImageButton?.setBackgroundResource(R.drawable.pause_icon)
        } else {
            Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)
        }
        Statified.mediaplayer?.setOnCompletionListener {
            Staticated.onSongComplete()
        }
        clickHandler()
        var visualizationhandler = DbmHandler.Factory.newVisualizerHandler(Statified.myactivity as Context, 0)
        Statified.audioVisualization?.linkTo(visualizationhandler)
        var prefsforshuffle = Statified.myactivity?.getSharedPreferences(Staticated.MY_PREFS_SHUFFLE, Context.MODE_PRIVATE)
        var isshuffleallowed = prefsforshuffle?.getBoolean("feature", false)
        if (isshuffleallowed as Boolean) {
            Statified.currentSongHelper.isshuffle = true
            Statified.currentSongHelper.isloop = false
            Statified.shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_icon)
            Statified.loopImageButton?.setBackgroundResource(R.drawable.loop_white_icon)
        } else {
            Statified.currentSongHelper.isshuffle = false
            Statified.shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
        }
        var prefsforloop = Statified.myactivity?.getSharedPreferences(Staticated.MY_PREFS_LOOP, Context.MODE_PRIVATE)
        var isloopallowed = prefsforloop?.getBoolean("feature", false)
        if (isloopallowed as Boolean) {
            Statified.currentSongHelper.isshuffle = false
            Statified.currentSongHelper.isloop = true
            Statified.shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
            Statified.loopImageButton?.setBackgroundResource(R.drawable.loop_icon)
        } else {
            Statified.currentSongHelper.isloop = false
            Statified.loopImageButton?.setBackgroundResource(R.drawable.loop_white_icon)
        }
        if (Statified.favoriteContent?.checkifIdExists(Statified.currentSongHelper.songId?.toInt() as Int) as Boolean) {
            Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myactivity, R.drawable.favorite_on))
        } else {
            Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myactivity, R.drawable.favorite_off))
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun clickHandler() {

        Statified.fab?.setOnClickListener({
            if (Statified.favoriteContent?.checkifIdExists(Statified.currentSongHelper.songId?.toInt() as Int) as Boolean) {
                Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myactivity, R.drawable.favorite_off))
                Statified.favoriteContent?.deleteFavorite(Statified.currentSongHelper.songId?.toInt() as Int)
                Toast.makeText(Statified.myactivity, "Removed from Favorites", Toast.LENGTH_SHORT).show()
            } else {
                Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myactivity, R.drawable.favorite_on))
                if (!(Statified.favoriteContent?.checkifIdExists(Statified.currentSongHelper.songId.toInt()) as Boolean)) {
                    Statified.favoriteContent?.storeAsFavorite(Statified.currentSongHelper.songId.toInt(), Statified.currentSongHelper.songArtist.toString(), Statified.currentSongHelper.songTitle.toString(), Statified.currentSongHelper.songPath.toString())

                    Toast.makeText(Statified.myactivity, "Added to Favorites", Toast.LENGTH_SHORT).show()
                } else {

                }
            }
        })

        /*The implementation will be taught in the coming topics*/
        Statified.shuffleImageButton?.setOnClickListener({
            var editorShuffle = Statified.myactivity?.getSharedPreferences(Staticated.MY_PREFS_SHUFFLE, Context.MODE_PRIVATE)?.edit()
            var editorLoop = Statified.myactivity?.getSharedPreferences(Staticated.MY_PREFS_LOOP, Context.MODE_PRIVATE)?.edit()

            if (Statified.currentSongHelper.isshuffle as Boolean) {

                /*Making the isLoop false*/
                Statified.currentSongHelper.isshuffle = false

                /*We change the color of the icon*/
                Statified.shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
                editorShuffle?.putBoolean("feature", false)
                editorShuffle?.apply()
            } else {

                /*If loop was not enabled when tapped, we enable if and make the isLoop to true*/
                Statified.currentSongHelper.isloop = false

                /*Loop and shuffle won't work together so we put shuffle false irrespectve of the whether it was on or not*/
                Statified.currentSongHelper.isshuffle = true

                /*Loop button color changed to mark it ON*/
                Statified.loopImageButton?.setBackgroundResource(R.drawable.loop_white_icon)

                /*Changing the shuffle button to white, no matter which color it was earlier*/
                Statified.shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_icon)
                editorShuffle?.putBoolean("feature", true)
                editorShuffle?.apply()
                editorLoop?.putBoolean("feature", false)
                editorLoop?.apply()
            }
        })
        Statified.nextImageButton?.setOnClickListener({
            Statified.currentSongHelper.isPlaying = true
            if (Statified.currentSongHelper.isshuffle as Boolean) {
                Staticated.playNext("PlayNextLikeNormalShuffle")
            } else {
                Staticated.playNext("PlayNextNormal")
            }
        })
        Statified.previousImageButton?.setOnClickListener({

            /*We set the player to be playing by setting isPlaying to be true*/
            Statified.currentSongHelper.isPlaying = true
            if (Statified.currentSongHelper.isshuffle as Boolean) {
                playPrevious("PlayPreviousLikeNormalShuffle")
            } else {
                playPrevious("PlayPreviousNormal")
            }
        })
        Statified.loopImageButton?.setOnClickListener({
            var editorShuffle = Statified.myactivity?.getSharedPreferences(Staticated.MY_PREFS_SHUFFLE, Context.MODE_PRIVATE)?.edit()
            var editorLoop = Statified.myactivity?.getSharedPreferences(Staticated.MY_PREFS_LOOP, Context.MODE_PRIVATE)?.edit()

            /*if loop was enabled, we turn it off and vice versa*/
            if (Statified.currentSongHelper.isloop as Boolean) {

                /*Making the isLoop false*/
                Statified.currentSongHelper.isloop = false

                /*We change the color of the icon*/
                Statified.loopImageButton?.setBackgroundResource(R.drawable.loop_white_icon)
                editorLoop?.putBoolean("feature", false)
                editorLoop?.apply()
            } else {

                /*If loop was not enabled when tapped, we enable if and make the isLoop to true*/
                Statified.currentSongHelper.isloop = true

                /*Loop and shuffle won't work together so we put shuffle false irrespectve of the whether it was on or not*/
                Statified.currentSongHelper.isshuffle = false

                /*Loop button color changed to mark it ON*/
                Statified.loopImageButton?.setBackgroundResource(R.drawable.loop_icon)

                /*Changing the shuffle button to white, no matter which color it was earlier*/
                Statified.shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
                editorShuffle?.putBoolean("feature", false)
                editorShuffle?.apply()
                editorLoop?.putBoolean("feature", true)
                editorLoop?.apply()
            }
        })
        Statified.playPauseImageButton?.setOnClickListener({

            /*if the song is already playing and then play/pause button is tapped
            * then we pause the media player and also change the button to play button*/
            if (Statified.currentSongHelper.isPlaying as Boolean) {
                Statified.mediaplayer?.pause()
                Statified.currentSongHelper.isPlaying = false
                Statified.currentSongHelper.trackPosition = songf.Statified.mediaplayer?.currentPosition
                        as Int
                Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)

                /*If the song was not playing the, we start the music player and
                * change the image to pause icon*/
            } else {
                //Statified.mediaplayer?.seekTo(Statified.currentSongHelper.trackPosition as Int)
                Statified.mediaplayer?.start()
                Statified.currentSongHelper.isPlaying = true
                Statified.playPauseImageButton?.setBackgroundResource(R.drawable.pause_icon)
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun playPrevious(check: String) {

        if (check.equals("PlayPreviousNormal", true)) {
            Statified.currentPosition = Statified.currentPosition - 1
        } else if (check.equals("PlayPreviousLikeNormalShuffle", true)) {
            var randomObject = Random()
            var randomPosition = randomObject.nextInt(Statified.fetchSongs?.size?.plus(1) as Int)
            Statified.currentPosition = randomPosition
        }
        if (Statified.currentSongHelper.isPlaying as Boolean) {
            Statified.playPauseImageButton?.setBackgroundResource(R.drawable.pause_icon)
        } else {
            Statified.playPauseImageButton?.setBackgroundResource(R.drawable.play_icon)
        }
        if (Statified.currentPosition == -1) {
            Statified.currentPosition = 0
        }
        Statified.currentSongHelper.isloop = false
        Statified.loopImageButton?.setBackgroundResource(R.drawable.loop_white_icon)
        var nextSong = Statified.fetchSongs.get(Statified.currentPosition)
        Statified.currentSongHelper.songPath = nextSong.songData
        Statified.currentSongHelper.songTitle = nextSong.songTitle
        Statified.currentSongHelper.songArtist = nextSong.artist
        Statified.currentSongHelper.songId = nextSong.songID as Long
        Staticated.updatetextview(Statified.currentSongHelper.songTitle as String, Statified.currentSongHelper.songArtist as String)
        Statified.mediaplayer?.reset()
        try {
            Statified.mediaplayer?.setDataSource(Statified.myactivity, Uri.parse(Statified.currentSongHelper.songPath))
            Statified.mediaplayer?.prepare()
            Statified.mediaplayer?.start()
            Staticated.processInformation(Statified.mediaplayer as MediaPlayer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (Statified.favoriteContent?.checkifIdExists(Statified.currentSongHelper.songId?.toInt() as Int) as Boolean) {
            Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myactivity, R.drawable.favorite_on))
        } else {
            Statified.fab?.setImageDrawable(ContextCompat.getDrawable(Statified.myactivity, R.drawable.favorite_off))
        }

    }

    fun bindShakeListener() {
        Statified.mSensorListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
            override fun onSensorChanged(event: SensorEvent?) {
                val x = event!!.values[0]
                val y = event!!.values[1]
                val z = event!!.values[2]
                mAccelarationLast = mAccelarationCurrent
                mAccelarationCurrent = Math.sqrt(((x * x + y * y + z *
                        z).toDouble())).toFloat()
                var delta = mAccelarationCurrent - mAccelarationLast
                mAccelaration = mAccelaration * 0.9f + delta
                if (mAccelaration > 12) {
                    var prefs = Statified.myactivity?.getSharedPreferences(Statified.MY_PREFS_NAME, Context.MODE_PRIVATE)
                    var isAllowed = prefs?.getBoolean("feature", false)
                    if (isAllowed as Boolean) {
                        Staticated.playNext("PlayNextNormal")
                    }
                }
            }

        }
    }

}