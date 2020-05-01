package framework.base.rest

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by jakobkoerner on 12.12.17.
 */

object Model {

    @JsonClass(generateAdapter = true)
    data class LoginResponse(
            @field:Json(name = "workspace") val workspace:String = "",
            @field:Json(name = "userId") val userId:String = "",
            @field:Json(name = "userToken") val userToken:String = "",
            @field:Json(name = "firstname") val firstName: String,
            @field:Json(name = "lastname") val lastName: String,
            @field:Json(name = "email") val email: String,
            @field:Json(name = "phoneNo") val phoneNo: String,
            @field:Json(name = "avatarUrl") val avatarUrl: String,
            @field:Json(name = "userGroups") val userGroups: String,
            @field:Json(name = "isActive") val isActive: String,
            @field:Json(name = "isHidden") val isHidden: String,
            @field:Json(name = "isAMSUser") val isAMSUser: String,
            @field:Json(name = "dateCreated") val dateCreated: String,
            @field:Json(name = "dateConfirmed") val dateConfirmed: String,
            @field:Json(name = "dateLastAccess") val dateLastAccess: String
    )

    @JsonClass(generateAdapter = true)
    data class WorkspaceResponse(
        @field:Json(name = "welcome") val welcome: Welcome,
        @field:Json(name = "main") val main: Main = Main(),
        @field:Json(name = "settings") val settings: Settings,
        @field:Json(name = "news") val news: MutableList<News>,
        @field:Json(name = "licence") val licence: String
    )

    @JsonClass(generateAdapter = true)
    data class Welcome(
            @field:Json(name = "text") val text:String = "",
            @field:Json(name = "headerImageUrl") val headerImageUrl:String = "",
            @field:Json(name = "headerImageSize") val headerImageSize:String = "",
            @field:Json(name = "dateLastModified") val dateLastModified: String
    )

    @JsonClass(generateAdapter = true)
    data class News(
            @field:Json(name = "id") val id:String = "",
            @field:Json(name = "title") val title:String = "",
            @field:Json(name = "subTitle") val subTitle:String = "",
            @field:Json(name = "headerImageUrl") val headerImageUrl: String,
            @field:Json(name = "content") val content: String,
            @field:Json(name = "publishFrom") val publishFrom: String,
            @field:Json(name = "dateLastModified") val dateLastModified: String
    )

    @JsonClass(generateAdapter = true)
    data class Main(
            @field:Json(name = "text") val text:String = "",
            @field:Json(name = "headerImageUrl") val headerImageUrl:String = "",
            @field:Json(name = "headerImageSize") val headerImageSize:String = "",
            @field:Json(name = "dateLastModified") val dateLastModified: String = ""
    )

    @JsonClass(generateAdapter = true)
    data class Settings(
        @field:Json(name = "style") var style:String = "",
        @field:Json(name = "backgroundColor") val backgroundColor:String = "",
        @field:Json(name = "backgroundImageUrl") val backgroundImageUrl:String = "",
        @field:Json(name = "logoImageUrl") val logoImageUrl: String,
        @field:Json(name = "dateCreated") val dateCreated: String,
        @field:Json(name = "dateLastModified") val dateLastModified: String,
        @field:Json(name = "tilesSpacing") val tilesSpacing: String,
        @field:Json(name = "tilesBackgroundColor") val tilesBackgroundColor: String,
        @field:Json(name = "tilesBorderColor") val tilesBorderColor: String,
        @field:Json(name = "tilesBorderWidth") val tilesBorderWidth: String,
        @field:Json(name = "tilesCornerRadius") val tilesCornerRadius: String,
        @field:Json(name = "tilesTextColor") val tilesTextColor: String,
        @field:Json(name = "tilesIconBackgroundColor") val tilesIconBackgroundColor: String,
        @field:Json(name = "tilesIconCornerRadius") val tilesIconCornerRadius: String,
        @field:Json(name = "tilesIconTintColor") val tilesIconTintColor: String,
        @field:Json(name = "contentCornerRadius") val contentCornerRadius: String,
        @field:Json(name = "contentBackgroundColor") val contentBackgroundColor: String,
        @field:Json(name = "contentTextColor") val contentTextColor: String,
        @field:Json(name = "contentAccentColor") val contentAccentColor: String,
        @field:Json(name = "contentBorderColor") val contentBorderColor: String,
        @field:Json(name = "contentBorderWidth") val contentBorderWidth: String,
        @field:Json(name = "contentAccentContrastColor") val contentAccentContrastColor: String,
        @field:Json(name = "contentPlaceholderColor") val contentPlaceholderColor: String
    )

    @JsonClass(generateAdapter = true)
    data class User(
        @field:Json(name = "userId") val userId:String = "",
        @field:Json(name = "firstname") val firstName:String = "",
        @field:Json(name = "lastname") val lastName:String = "",
        @field:Json(name = "avatarUrl") val avatarUrl: String = "",
        @field:Json(name = "isOnline") val isOnline: String = ""
    )

}