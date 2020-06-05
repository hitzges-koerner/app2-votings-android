package appsquared.votings.app

import android.os.Parcel
import android.os.Parcelable

class VotingCustomItem() {

    var type: Int = 0
    var tag: Int = 0
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
     * @param title
     */
    constructor(type: Int, title: String) : this() {
        this.type = type
        this.title = title
    }

    /**
     * @param type
     * @param tag
     * @param title
     * @param file
     * @param url
     */
    constructor(type: Int, tag: Int, title: String, file: String, url: String) : this() {
        this.type = type
        this.tag = tag
        this.title = title
        this.file = file
        this.url = url
    }

    /**
     * @param type
     * @param tag
     * @param id
     * @param url
     * @param nameFirst
     * @param nameLast
     */
    constructor(type: Int, tag: Int, id: String, url: String, nameFirst: String, nameLast: String) : this() {
        this.type = type
        this.tag = tag
        this.id = id
        this.url = url
        this.nameFirst = nameFirst
        this.nameLast = nameLast
    }

    /**
     * @param type
     * @param tag
     * @param id
     * @param title
     */
    constructor(type: Int, tag: Int, id: String, title: String, selected: Boolean) : this() {
        this.type = type
        this.tag = tag
        this.id = id
        this.title = title
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
        var VOTER = 5
    }
}