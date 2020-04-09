package framework.base.rest

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

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
            @field:Json(name = "settings") val settings: Settings,
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
    data class Settings(
            @field:Json(name = "style") val style:String = "",
            @field:Json(name = "backgroundColor") val backgroundColor:String = "",
            @field:Json(name = "backgroundImageUrl") val backgroundImageUrl:String = "",
            @field:Json(name = "logoImageUrl") val logoImageUrl: String,
            @field:Json(name = "primaryColor") val primaryColor: String,
            @field:Json(name = "secondaryColor") val secondaryColor: String,
            @field:Json(name = "dateCreated") val dateCreated: String,
            @field:Json(name = "dateLastModified") val dateLastModified: String
    )

}