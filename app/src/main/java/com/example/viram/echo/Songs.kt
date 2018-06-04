package com.example.viram.echo

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Viram on 5/19/2018.
 */
class Songs(var songID: Long, var songTitle: String, var artist: String, var songData: String, var dateAdded: Long) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong()) {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0 //To change body of created functions use File | Settings | File Templates.
    }

    object Statified {
        var nameComparator: Comparator<Songs> = Comparator<Songs>
        { song1, song2 ->
            val songOne=song1.songTitle.toUpperCase()
            val songTwo=song2.songTitle.toUpperCase()
            songOne.compareTo(songTwo)
        }
        var dateComparator: Comparator<Songs> = Comparator<Songs>
        { song1, song2 ->
            val songOne=song1.dateAdded.toDouble()
            val songTwo=song2.dateAdded.toDouble()
            songTwo.compareTo(songOne)
        }

    }

    companion object CREATOR : Parcelable.Creator<Songs> {
        override fun createFromParcel(parcel: Parcel): Songs {
            return Songs(parcel)
        }

        override fun newArray(size: Int): Array<Songs?> {
            return arrayOfNulls(size)
        }
    }
}