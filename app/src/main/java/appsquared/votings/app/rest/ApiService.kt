package framework.base.rest

import androidx.preference.PreferenceManager
import appsquared.votings.app.rest.BasicAuthInterceptor
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import io.reactivex.Observable
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers
import java.net.Authenticator

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

    @PUT("{WORKSPACE}/user")
    fun sendUserData(@Header("Authorization") token : String,
                     @Path("WORKSPACE") workspace : String,
                     @Body data: String) : Observable<MutableList<Model.User>>

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