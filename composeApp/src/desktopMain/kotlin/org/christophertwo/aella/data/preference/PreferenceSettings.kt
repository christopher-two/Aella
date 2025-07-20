package org.christophertwo.aella.data.preference

import java.util.prefs.Preferences

class PreferenceSettings : PreferenceSettingsInterface {
    override fun getPreference(key: String): String? {
        val prefs = Preferences.userRoot().node(javaClass.name)
        return prefs.get(key, null)
    }

    override fun setPreference(
        key: String,
        value: String
    ) {
        val prefs = Preferences.userRoot().node(javaClass.name)
        prefs.put(key, value)
    }

    override fun clearPreference(key: String) {
        val prefs = Preferences.userRoot().node(javaClass.name)
        prefs.remove(key)
    }

    override fun clearAllPreferences() {
        val prefs = Preferences.userRoot().node(javaClass.name)
        prefs.clear()
    }
}