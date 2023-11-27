package appsquared.votings.app.rest

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import appsquared.votings.app.rest.Model
import io.reactivex.Observable
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers

/**
 * Created by jakobkoerner on 12.12.17.
 */

interface ApiService {

    @POST("{WORKSPACE}/login")
    @Headers("Content-type: application/json", "Accept: application/json")
    fun login(@Path("WORKSPACE") workspace : String,
              @Body data: String) : Observable<Model.LoginResponse>

    @GET("{WORKSPACE}")
    fun loadWorkspace(@Path("WORKSPACE") workspace : String,
                      @Header("Authorization") token : String) : Observable<Model.WorkspaceResponse>

    @GET("{WORKSPACE}/user")
    fun getUserList(@Header("Authorization") token : String,
                    @Path("WORKSPACE") workspace : String) : Observable<MutableList<Model.User>>

    @GET("{WORKSPACE}/push")
    fun getNotificationList(@Header("Authorization") token : String,
                    @Path("WORKSPACE") workspace : String) : Observable<MutableList<Model.Notification>>

    @GET("faq")
    fun getFaq(@Header("Authorization") token : String) : Observable<MutableList<Model.Faq>>

    @GET("{WORKSPACE}/votings")
    fun getVotingList(@Header("Authorization") token : String,
                    @Path("WORKSPACE") workspace : String) : Observable<MutableList<Model.VotingShort>>

    @GET("{WORKSPACE}/votings/{VOTING_ID}/{VOTING_REPRESENTATION_ID}")
    fun getVoting(@Header("Authorization") token : String,
                    @Path("WORKSPACE") workspace : String,
                    @Path("VOTING_ID") votingID : String,
                    @Path("VOTING_REPRESENTATION_ID") votingRepresentationId : String) : Observable<Model.Voting>

    @POST("{WORKSPACE}/votings/{VOTING_ID}")
    fun sendVoting(@Header("Authorization") token : String,
                     @Path("WORKSPACE") workspace : String,
                     @Path("VOTING_ID") votingId : String,
                     @Body data: String) : Observable<ResponseBody>

    @POST("telemetry")
    fun sendTelemetry(@Body data: String) : Observable<ResponseBody>

    @POST("{WORKSPACE}/user")
    fun inviteUser(@Header("Authorization") token : String,
                     @Path("WORKSPACE") workspace : String,
                     @Body data: String) : Observable<ResponseBody>

    @POST("register")
    fun registerAccount(@Body data: String) : Observable<ResponseBody>

    @POST("{WORKSPACE}/quickvoting")
    @Headers("Content-type: application/json", "Accept: application/json")
    fun createQuickVoting(@Header("Authorization") token : String,
                          @Path("WORKSPACE") workspace : String,
                          @Body data: String) : Observable<ResponseBody>

    @PUT("{WORKSPACE}/quickvoting/{VOTING_ID}")
    fun closeQuickVoting(@Header("Authorization") token : String,
                         @Path("WORKSPACE") workspace : String,
                         @Path("VOTING_ID") votingId : String) : Observable<ResponseBody>

    @DELETE("{WORKSPACE}/quickvoting/{VOTING_ID}")
    fun deleteQuickVoting(@Header("Authorization") token : String,
                         @Path("WORKSPACE") workspace : String,
                         @Path("VOTING_ID") votingId : String) : Observable<ResponseBody>

    @GET(".")
    fun checkWorkspaceAvailable(
        @Query("q") workspace: String) : Observable<ResponseBody>

    @GET("{WORKSPACE}/changelog/android")
    fun getChangelog(@Header("Authorization") token : String,
                    @Path("WORKSPACE") workspace : String) : Observable<MutableList<Model.Changelog>>

    @PUT("{WORKSPACE}/user")
    fun sendUserData(@Header("Authorization") token : String,
                     @Path("WORKSPACE") workspace : String,
                     @Body data: String) : Observable<Model.LoginResponse>

    @PUT("{WORKSPACE}/user/pushtoken")
    fun sendFirebaseToken(@Header("Authorization") token : String,
                     @Path("WORKSPACE") workspace : String,
                     @Body data: String) : Observable<ResponseBody>

    @DELETE("{WORKSPACE}/user/avatar")
    fun deleteAvatar(@Header("Authorization") token : String,
                     @Path("WORKSPACE") workspace : String) : Observable<ResponseBody>

    @GET("{WORKSPACE}/documents/{DOCUMENT}")
    fun getDocument(@Header("Authorization") token : String,
                     @Path("WORKSPACE") workspace : String,
                     @Path("DOCUMENT") document : String) : Observable<ResponseBody>

    @POST("{WORKSPACE}/user/avatar")
    @Headers("Content-Type: image/png",
        "Connection: Keep-Alive",
        "x-ms-blob-type: BlockBlob"
    )
    fun sendAvatar(@Header("Authorization") token : String,
                   @Path("WORKSPACE") workspace : String,
                   @Body params: RequestBody) : Observable<String>

    companion object {

        fun create(baseUrl: String): ApiService {

            val moshi = Moshi.Builder()
                    .add(NULL_TO_EMPTY_STRING_ADAPTER)
                    .build()

            val logging = HttpLoggingInterceptor()
            // set your desired log level
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            // add your other interceptors …
            // add logging as last interceptor
            httpClient.addInterceptor(logging)

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .baseUrl(baseUrl)
                    .client(httpClient.build())
                    .build()

            return retrofit.create(ApiService::class.java)
        }


    }

    object NULL_TO_EMPTY_STRING_ADAPTER {
        @FromJson
        fun fromJson(reader: JsonReader): String {
            if (reader.peek() != JsonReader.Token.NULL) {
                return reader.nextString()
            }
            reader.nextNull<Unit>()
            return ""
        }
    }

}