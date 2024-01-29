package com.personal.personalsafetyguard.model

import android.os.Parcel
import android.os.Parcelable

class ChildItem() : Parcelable {
    var childBg: String? = null
    var childTitle: String? = null
    var childImage: String? = null
    var childOpen: String? = null
    var childDesc1: String? = null
    var childDesc2: String? = null
    var childDesc3: String? = null
    var childClose: String? = null
    var childRef1: String? = null
    var childRef2: String? = null
    var childRef3: String? = null
    var childRef4: String? = null

    constructor(parcel: Parcel) : this() {
        childBg = parcel.readString()
        childTitle = parcel.readString()
        childImage = parcel.readString()
        childOpen = parcel.readString()
        childDesc1 = parcel.readString()
        childDesc2 = parcel.readString()
        childDesc3 = parcel.readString()
        childClose = parcel.readString()
        childRef1 = parcel.readString()
        childRef2 = parcel.readString()
        childRef3 = parcel.readString()
        childRef4 = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(childBg)
        parcel.writeString(childTitle)
        parcel.writeString(childImage)
        parcel.writeString(childOpen)
        parcel.writeString(childDesc1)
        parcel.writeString(childDesc2)
        parcel.writeString(childDesc3)
        parcel.writeString(childClose)
        parcel.writeString(childRef1)
        parcel.writeString(childRef2)
        parcel.writeString(childRef3)
        parcel.writeString(childRef4)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChildItem> {
        override fun createFromParcel(parcel: Parcel): ChildItem {
            return ChildItem(parcel)
        }

        override fun newArray(size: Int): Array<ChildItem?> {
            return arrayOfNulls(size)
        }
    }
}
