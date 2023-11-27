package appsquared.votings.app.tag.enums

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

enum class Style(val value: String) {
    @SerializedName("rich")
    @field:Json(name = "rich")
    RICH("rich"),

    @SerializedName("minimal")
    @field:Json(name = "minimal")
    MINIMAL("minimal"),

    @SerializedName("clean")
    @field:Json(name = "clean")
    CLEAN("clean"),

    @SerializedName("unknown")
    @field:Json(name = "unknown")
    UNKNOWN("unknown");

    companion object {
        fun fromString(value: String) : Style {
            return Style.values().firstOrNull { it.value == value } ?: run {
                UNKNOWN
            }
        }
    }
}