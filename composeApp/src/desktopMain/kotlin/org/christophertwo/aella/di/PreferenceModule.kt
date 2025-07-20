package org.christophertwo.aella.di

import org.christophertwo.aella.data.preference.PreferenceSettings
import org.christophertwo.aella.data.preference.PreferenceSettingsInterface
import org.koin.core.module.Module
import org.koin.dsl.module

val PreferenceModule: Module
    get() = module {
        single<PreferenceSettingsInterface> { PreferenceSettings() }
    }