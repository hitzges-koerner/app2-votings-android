package appsquared.votings.app

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson


class AppData {

    fun saveObjectToSharedPreference(
        context: Context,
        serializedObjectKey: String?,
        any: Any?
    ) {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        val sharedPreferencesEditor = sharedPreferences.edit()
        val gson = Gson()
        val serializedObject = gson.toJson(any)
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject)
        sharedPreferencesEditor.apply()
    }

    fun <GenericClass> getSavedObjectFromPreference(
        context: Context,
        preferenceKey: String?,
        classType: Class<GenericClass>?
    ): GenericClass? {
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        if (sharedPreferences.contains(preferenceKey)) {
            val gson = Gson()
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType)
        }
        return null
    }

    fun deleteSavedObjectFromPreference(
        context: Context,
        preferenceKey: String?
    ) {
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        if (sharedPreferences.contains(preferenceKey)) {
            sharedPreferences.edit().remove(preferenceKey).apply()
        }
    }

    fun isSavedObjectFromPreferenceAvailable(
        context: Context,
        preferenceKey: String?
    ) : Boolean {
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.contains(preferenceKey)
    }
}