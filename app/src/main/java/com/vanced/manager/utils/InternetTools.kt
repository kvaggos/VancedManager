package com.vanced.manager.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.BuildConfig
import com.vanced.manager.R
import java.lang.IllegalStateException

object InternetTools {

    fun openUrl(Url: String, color: Int, context: Context) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(context, color))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(Url))
    }

    fun getFileNameFromUrl(url: String) = url.substring(url.lastIndexOf('/')+1, url.length)

    fun displayJsonString(json: String, obj: String, context: Context): String {
        val installUrl = PreferenceManager.getDefaultSharedPreferences(context).getString("install_url", getLatestVancedUrl(context))
        return if (GetJson().isConnected(context)) {
            try {
                GetJson().AsJSONObject("$installUrl/$json").get(obj).asString
            } catch (e: IllegalStateException) {
                GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/$json").get(obj).asString
            }
        } else {
            context.getString(R.string.unavailable)
        }
    }

    fun displayJsonInt(json: String, obj: String, context: Context): Int {
        val installUrl = PreferenceManager.getDefaultSharedPreferences(context).getString("install_url", getLatestVancedUrl(context))
        return if (GetJson().isConnected(context)) {
            try {
                GetJson().AsJSONObject("$installUrl/$json").get(obj).asInt
            } catch (e: IllegalStateException) {
                GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/$json").get(obj).asInt
            }
        } else 0
    }

    fun isUpdateAvailable(): Boolean {
        val checkUrl = GetJson().AsJSONObject("https://vanced.app/api/v1/manager.json")
        val remoteVersion = checkUrl.get("versionCode").asInt

        return remoteVersion > BuildConfig.VERSION_CODE
    }

    fun getLatestVancedUrl(context: Context): String {
        return if (GetJson().isConnected(context)) {
            val latestVer = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/vanced.json").get("version").asString
            "https://vanced.app/api/v1/apks/v$latestVer"
        } else
            "https://vanced.app/api/v1/apks/v15.05.54"
    }

}
