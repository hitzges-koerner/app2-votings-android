package appsquared.votings.app

class PreferenceNames {

    companion object {

        const val USER_TOKEN = "account_token"

        const val REALM = "realm"

        const val AUTH = "authorization_key"
        const val CONFIG = "config"
        const val CONFIG_ANY = "config_any"
        const val HASHTAGS = "hashtags"

        const val APP_ID = "app_id"
        const val DEVICE_ID = "device_id"

        // PAYLOAD
        const val FACEBOOK_DISABLED = "facebook_disabled"
        const val FACEBOOK_APPEND_HASHTAG = "facebook_append_hashtag"
        const val SIDEMENU_LOGIN_HIDE = "sidemenu_login_hide"
        const val GENERATED_STREAMS_NOT_LOGGED_IN_HIDE = "generated_streams_not_logged_in_hide"
        const val RATING_DISABLED = "rating_disabled"
        const val FOREIGN_MOMENTS_SHARE = "foreign_moments_share"
        const val VOTING_CHOICES_LOAD = "voting_choices_load"
        const val SIDEMENU_TOURNEMENT_HIDE = "sidemenu_tournement_hide"

        //firebase
        const val FIREBASE_TOKEN = "firebase_token";
        const val FIREBASE_TOKEN_SEND_TO_SERVER = "firebase_token_send_to_server";
        const val NOTIFICATION_SHOW = "notification_show"

        //login
        const val USERNAME = "username"
        const val USERID = "user_id"
        const val USERGROUPS = "user_group"
        const val GROUP = "group"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val FIRSTNAME = "first_name"
        const val LASTNAME = "last_name"
        const val PROPERTY_ONE = "property_one"
        const val PROPERTY_TWO = "property_two"

        //db systel
        const val QR_NOTES = "qr_note"
        const val INVALID_VOUCHERS = "voucher_invalid_ids"
        const val REMINDER = "reminder"

        //payload content, url and timestamp for last successfull download
        const val CONTENT = "_content"
        const val URL = "_url"
        const val TIMESTAMP = "_timestamp"

        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"

        const val IMAGE_RATING = "image_rating"
        const val IMAGE_COMMENT = "image_comment"
        const val IMAGE_STREAM_ID = "image_stream_id"
        const val IMAGE_SAVE_APP = "image_save_app"
        const val IMAGE_SAVE_GALLERY = "image_save_gallery"
        const val PHOTO_LIST = "photo_list"

        const val TERMS_ACCEPTED = "terms_accepted"

        const val HIDDEN_IDENTITY = "hidden_identity"
        const val APP_SPONSOR_TAP_URL = "app_sponsor_app_url"
        const val USER_FAVORITES = "user_favorites"
        const val CONTACT_ALLOWED = "contact_allowed"
        const val USER_AVATAR_URL = "user_avatar_url"
        const val LANGUAGE = "language"
    }
}