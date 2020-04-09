package appsquared.votings.app

import android.os.Parcel
import android.os.Parcelable

class Item() : Parcelable {

    lateinit var text: String
    lateinit var icon: String
    var iconId: Int = 0

    constructor(parcel: Parcel) : this() {
        text = parcel.readString().toString()
        icon = parcel.readString().toString()
        iconId = parcel.readInt()
    }

    constructor(text: String, iconId: Int) : this() {
        this.text = text
        this.iconId = iconId
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeString(icon)
        parcel.writeInt(iconId)
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
    }
}