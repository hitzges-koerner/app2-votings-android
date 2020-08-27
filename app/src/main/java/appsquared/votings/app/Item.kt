package appsquared.votings.app

import android.os.Parcel
import android.os.Parcelable

class Item() : Parcelable {

    lateinit var text: String
    lateinit var icon: String
    var type: Int = 0
    var iconId: Int = 0

    constructor(parcel: Parcel) : this() {
        text = parcel.readString().toString()
        icon = parcel.readString().toString()
        iconId = parcel.readInt()
        type = parcel.readInt()
    }

    constructor(text: String, iconId: Int, type: Int) : this() {
        this.text = text
        this.iconId = iconId
        this.type = type
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeString(icon)
        parcel.writeInt(iconId)
        parcel.writeInt(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }


        var PROFIL = 0
        var ATTENDEES = 1
        var VOTING_ACTIV = 2
        var VOTING_PAST = 3
        var VOTING_FUTURE = 4
        var NOTIFICATION = 5
        var FAQ = 6
        var PRO = 7
        var SETIINGS = 8
        var NEWS = 9
        var WELCOME = 10
    }
}