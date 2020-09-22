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
        @field:Json(name = "firstname") var firstName: String = "",
        @field:Json(name = "lastname") var lastName: String = "",
        @field:Json(name = "email") var email: String = "",
        @field:Json(name = "phoneNo") var phoneNo: String = "",
        @field:Json(name = "avatarUrl") var avatarUrl: String = "",
        @field:Json(name = "userGroups") val userGroups: String = "",
        @field:Json(name = "isActive") val isActive: String = "",
        @field:Json(name = "isHidden") val isHidden: String = "",
        @field:Json(name = "isAMSUser") val isAMSUser: String = "",
        @field:Json(name = "dateCreated") val dateCreated: String = "",
        @field:Json(name = "dateConfirmed") val dateConfirmed: String = "",
        @field:Json(name = "dateLastAccess") val dateLastAccess: String = ""
    )

    @JsonClass(generateAdapter = true)
    data class WorkspaceResponse(
        @field:Json(name = "welcome") val welcome: Welcome = Welcome(),
        @field:Json(name = "main") val main: Main = Main(),
        @field:Json(name = "settings") val settings: Settings = Settings(),
        @field:Json(name = "news") val news: MutableList<News> = mutableListOf(),
        @field:Json(name = "votings") val votings: MutableList<VotingShort> = mutableListOf(),
        @field:Json(name = "legalImprint") val legalImprint: String = "",
        @field:Json(name = "legalTerms") val legalTerms: String = "",
        @field:Json(name = "legalPrivacy") val legalPrivacy: String = "",
        @field:Json(name = "version") val version: Version = Version(),
        @field:Json(name = "planName") val planName: String = "",
        @field:Json(name = "planMaxParticipants") val planMaxParticipants: String = "",
        @field:Json(name = "proVersionText") val proVersionText: String = ""

    )

    @JsonClass(generateAdapter = true)
    data class Version(
            @field:Json(name = "iOS_CurrentVersion") val iOS_CurrentVersion:String = "",
            @field:Json(name = "iOS_RequiredVersion") val iOS_RequiredVersion:String = "",
            @field:Json(name = "iOS_AppStoreLink") val iOS_AppStoreLink:String = "",
            @field:Json(name = "android_CurrentVersion") val android_CurrentVersion:String = "",
            @field:Json(name = "android_RequiredVersion") val android_RequiredVersion:String = "",
            @field:Json(name = "android_AppStoreLink") val android_AppStoreLink:String = ""
    )

    @JsonClass(generateAdapter = true)
    data class Welcome(
            @field:Json(name = "text") val text:String = "",
            @field:Json(name = "headerImageUrl") val headerImageUrl:String = "",
            @field:Json(name = "headerImageSize") val headerImageSize:String = "",
            @field:Json(name = "dateLastModified") val dateLastModified: String = ""
    )

    @JsonClass(generateAdapter = true)
    data class News(
            @field:Json(name = "id") val id:String = "",
            @field:Json(name = "title") val title:String = "",
            @field:Json(name = "subTitle") val subTitle:String = "",
            @field:Json(name = "headerImageUrl") val headerImageUrl: String = "",
            @field:Json(name = "content") val content: String = "",
            @field:Json(name = "publishFrom") val publishFrom: String = "",
            @field:Json(name = "dateLastModified") val dateLastModified: String
    ) : Serializable {

    }

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
        @field:Json(name = "logoImageUrl") val logoImageUrl: String = "",
        @field:Json(name = "dateCreated") val dateCreated: String = "",
        @field:Json(name = "dateLastModified") val dateLastModified: String = "",
        @field:Json(name = "tilesSpacing") val tilesSpacing: String = "",
        @field:Json(name = "tilesBackgroundColor") val tilesBackgroundColor: String = "",
        @field:Json(name = "tilesBorderColor") val tilesBorderColor: String = "",
        @field:Json(name = "tilesBorderWidth") val tilesBorderWidth: String = "",
        @field:Json(name = "tilesCornerRadius") val tilesCornerRadius: String = "",
        @field:Json(name = "tilesTextColor") val tilesTextColor: String = "",
        @field:Json(name = "tilesIconBackgroundColor") val tilesIconBackgroundColor: String = "",
        @field:Json(name = "tilesIconCornerRadius") val tilesIconCornerRadius: String = "",
        @field:Json(name = "tilesIconTintColor") val tilesIconTintColor: String = "",
        @field:Json(name = "contentCornerRadius") val contentCornerRadius: String = "",
        @field:Json(name = "contentBackgroundColor") val contentBackgroundColor: String = "",
        @field:Json(name = "contentTextColor") val contentTextColor: String = "",
        @field:Json(name = "contentAccentColor") val contentAccentColor: String = "",
        @field:Json(name = "contentBorderColor") val contentBorderColor: String = "",
        @field:Json(name = "contentBorderWidth") val contentBorderWidth: String = "",
        @field:Json(name = "contentAccentContrastColor") val contentAccentContrastColor: String = "",
        @field:Json(name = "contentPlaceholderColor") val contentPlaceholderColor: String = "",
        @field:Json(name = "tabActiveBackgroundColor") val tabActiveBackgroundColor: String = "",
        @field:Json(name = "tabActiveForegroundColor") val tabActiveForegroundColor: String = "",
        @field:Json(name = "tabInactiveBackgroundColor") val tabInactiveBackgroundColor: String = "",
        @field:Json(name = "tabInactiveForegroundColor") val tabInactiveForegroundColor: String = ""
    )

    @JsonClass(generateAdapter = true)
    data class User(
        @field:Json(name = "userId") val userId:String = "",
        @field:Json(name = "firstname") val firstName:String = "",
        @field:Json(name = "lastname") val lastName:String = "",
        @field:Json(name = "email") val email:String = "",
        @field:Json(name = "avatarUrl") val avatarUrl: String = "",
        @field:Json(name = "isOnline") val isOnline: String = "",
        @field:Json(name = "isConfirmed") val isConfirmed: String = "",
        @field:Json(name = "tags") val tags: MutableList<String> = mutableListOf(),
        var isSelected: Boolean = false
    )

    @JsonClass(generateAdapter = true)
    data class Notification(
        @field:Json(name = "id") val id:String = "",
        @field:Json(name = "sentDate") val sentDate:String = "",
        @field:Json(name = "message") val message:String = ""
    )

    @JsonClass(generateAdapter = true)
    data class Faq(
        @field:Json(name = "section") var section:Boolean = false,
        @field:Json(name = "topic") val topic:String = "",
        @field:Json(name = "topicPos") val topicPos:Int = 0,
        @field:Json(name = "question") val question:String = "",
        @field:Json(name = "questionPos") val questionPos:Int = 0,
        @field:Json(name = "answer") val answer:String = "",
        @field:Json(name = "affectedPlans") val affectedPlans:String = ""
    )

    @JsonClass(generateAdapter = true)
    data class VotingShort(
        @field:Json(name = "votingTitle") val votingTitle:String = "",
        @field:Json(name = "isVoted") val isVoted:String = "",
        @field:Json(name = "votingId") val votingId:String = "",
        @field:Json(name = "votingFrom") val votingFrom:String = "",
        @field:Json(name = "votingTill") val votingTill:String = "",
        @field:Json(name = "votingType") val votingType:String = "",
        @field:Json(name = "votingStatus") var votingStatus:Int = 0,
        @field:Json(name = "inRepresentationOfId") var inRepresentationOfId:String = "",
        @field:Json(name = "inRepresentationOfName") var inRepresentationOfName:String = "",
        @field:Json(name = "isQuickVoting") var isQuickVoting :String = "0", //String -> “0” or “1”
        @field:Json(name = "ownerId") var ownerId :String = ""
    )

    @JsonClass(generateAdapter = true)
    data class Voting(
        @field:Json(name = "votingId") val votingId:String = "",
        @field:Json(name = "votingTitle") val votingTitle:String = "",
        @field:Json(name = "votingDescription") val votingDescription:String = "",
        @field:Json(name = "liveStreamUrl") val liveStreamUrl:String = "",
        @field:Json(name = "votingResultsAvailableFrom") val votingResultsAvailableFrom:String = "",
        @field:Json(name = "choicesMin") val choicesMin:String = "",
        @field:Json(name = "choicesMax") val choicesMax:String = "",
        @field:Json(name = "votingType") val votingType:String = "",
        @field:Json(name = "votingFrom") val votingFrom:String = "",
        @field:Json(name = "votingTill") val votingTill:String = "",
        @field:Json(name = "inRepresentationOfId") val inRepresentationOfId:String = "",
        @field:Json(name = "inRepresentationOfName") val inRepresentationOfName:String = "",
        @field:Json(name = "documents") val documents:MutableList<Document> = mutableListOf(),
        @field:Json(name = "choices") val choices:MutableList<Choice> = mutableListOf(),
        @field:Json(name = "users") val users:MutableList<UserVoting> = mutableListOf(),
        @field:Json(name = "isQuickVoting") var isQuickVoting :String = "0", //String -> “0” or “1”
        @field:Json(name = "ownerId") var ownerId :String = ""
    )

    @JsonClass(generateAdapter = true)
    data class UserVoting(
        @field:Json(name = "userId") val userId:String = "",
        @field:Json(name = "firstname") val firstName:String = "",
        @field:Json(name = "lastname") val lastName:String = "",
        @field:Json(name = "votesCnt") val votesCnt: String = "",
        @field:Json(name = "votedAt") val votedAt: String = "",
        @field:Json(name = "votedChoiceIds") val votedChoiceIds: String = "",
        @field:Json(name = "representedByUserId") val representedByUserId: String = "",
        @field:Json(name = "representedByUserFirstname") val representedByUserFirstname: String = "",
        @field:Json(name = "representedByUserLastname") val representedByUserLastname: String = "",
        @field:Json(name = "isQuickVoting") var isQuickVoting :String = "0", //String -> “0” or “1”
        @field:Json(name = "ownerId") var ownerId :String = ""
    )

    @JsonClass(generateAdapter = true)
    data class Document(
        @field:Json(name = "title") val title:String = "",
        @field:Json(name = "fileExtension") val fileExtension:String = "",
        @field:Json(name = "url") val url:String = ""
    )

    @JsonClass(generateAdapter = true)
    data class Choice(
        @field:Json(name = "choiceId") val choiceId:String = "",
        @field:Json(name = "choiceTitle") val choiceTitle:String = "",
        @field:Json(name = "pos") val pos:String = "",
        @field:Json(name = "votesCnt") val votesCnt:String = ""
    )

    @JsonClass(generateAdapter = true)
    data class Changelog(
        @field:Json(name = "version") val version:String = "",
        @field:Json(name = "releaseDate") val releaseDate:String = "",
        @field:Json(name = "releaseNotes") val releaseNotes:String = ""
    )
}