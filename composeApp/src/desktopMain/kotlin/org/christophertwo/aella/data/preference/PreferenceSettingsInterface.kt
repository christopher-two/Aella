package org.christophertwo.aella.data.preference

interface PreferenceSettingsInterface {
    fun getPreference(key: String): String?
    fun setPreference(key: String, value: String)
    fun clearPreference(key: String)
    fun clearAllPreferences()
}