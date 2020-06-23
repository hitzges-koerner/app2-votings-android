package appsquared.votings.app

import android.os.Parcel
import android.os.Parcelable

class VotingCustomItem() {

    var type: Int = 0
    var subType: Int = 0
    var tag: Int = 0
    var count: Int = 0
    var total: Int = 0
    var id: String = ""
    var url: String = ""
    var title: String = ""
    var file: String = ""
    var nameFirst: String = ""
    var nameLast: String = ""
    var selected: Boolean = false

    /**
     * constructor for stream files
     * @param type
     * @param subType
     * @param title
     */
    constructor(type: Int, subType: Int, title: String) : this() {
        this.type = type
        this.subType = subType
        this.title = title
    }

    /**
     * @param type
     * @param subType
     * @param title
     * @param file
     * @param url
     */
    constructor(type: Int, subType: Int, title: String, file: String, url: String) : this() {
        this.type = type
        this.subType = subType
        this.title = title
        this.file = file
        this.url = url
    }

    /**
     * @param type
     * @param subType
     * @param tag
     * @param id
     * @param nameFirst
     * @param nameLast
     */
    constructor(type: Int, subType: Int, tag: Int, id: String, nameFirst: String, nameLast: String) : this() {
        this.type = type
        this.subType = subType
        this.tag = tag
        this.id = id
        this.nameFirst = nameFirst
        this.nameLast = nameLast
    }

    /**
     * @param type
     * @param subType
     * @param tag
     * @param id
     * @param title
     */
    constructor(type: Int, subType: Int, tag: Int, id: String, title: String, selected: Boolean) : this() {
        this.type = type
        this.subType = subType
        this.tag = tag
        this.id = id
        this.title = title
        this.selected = selected
    }

    /**
     * @param type
     * @param subType
     * @param id
     * @param title
     * @param count
     * @param total
     * @param selected
     */
    constructor(type: Int, subType: Int, id: String, title: String, count: Int, total: Int, selected: Boolean) : this() {
        this.type = type
        this.subType = subType
        this.id = id
        this.title = title
        this.count = count
        this.total = total
        this.selected = selected
    }

    companion object {
        var TYPE = 0
        var TAG = 1
        var ID = 2
        var TITLE = 3
        var URL = 4
        var FILE = 5
        var NAME_FIRST = 6
        var NAME_LAST = 7

        var SECTION = 0
        var STREAM = 1
        var DOCUMENT = 2
        var CREATOR = 3
        var CHOICE = 4
        var BUTTON = 5
        var USER = 6
        var INFO = 7
    }
}