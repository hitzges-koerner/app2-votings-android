package appsquared.votings.app


/**
 * Created by jakobkoerner on 17.01.18.
 */

class VersionChecker(val versionCurrent: String, val versionRequired: String) {

    private var mDeprecated = false
    private var mOutdated = false

    fun init() {
        val versionCurrentArray = getVersionArray(versionCurrent)
        val versionRequiredArray = getVersionArray(versionRequired)
        val versionInstalledArray = getVersionArray(getAppVersion())
        if(versionCompare(versionInstalledArray, versionRequiredArray)) mDeprecated = true
        else if(versionCompare(versionInstalledArray, versionCurrentArray)) mOutdated = true
    }

    private fun getVersionArray(version: String) : List<Int> {
        return fillArray(version.split(".").map { it.toInt() })
    }

    private fun fillArray(versionArray: List<Int>) : List<Int>{
        val temp = versionArray.toMutableList()
        while(temp.size < 3) {
            temp.add(0)
        }
        return temp
    }

    // checks if versionOne is older than versionTwo
    private fun versionCompare(versionOne: List<Int>, versionTwo: List<Int>) : Boolean {

        var versionSmaller = false

        for((index,version) in versionOne.withIndex()){
            if(version == versionTwo[index]) versionSmaller = false
            if(version < versionTwo[index]) {
                versionSmaller = true
                break
            }
            if(version > versionTwo[index]) {
                versionSmaller = false
                break
            }
        }
        return versionSmaller
    }

    /*
    // checks if versionOne is older than versionTwo
    private fun versionCompare(versionOne: List<Int>, versionTwo: List<Int>) : Boolean {
        for((index,version) in versionOne.withIndex()){
            if(version < versionTwo[index]) return true
        }
        return false
    }
    */

    fun isDeprecated() : Boolean {
        return mDeprecated
    }

    fun isOutdated() : Boolean {
        return mOutdated
    }

    companion object {
        const val DEPRECATED = 0
        const val OUTDATED = 1
    }
}