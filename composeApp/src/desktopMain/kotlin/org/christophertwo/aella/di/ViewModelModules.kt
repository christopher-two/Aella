package org.christophertwo.aella.di

import org.christophertwo.aella.ui.screen.home.HomeViewModel
import org.christophertwo.aella.ui.screen.works.WorksViewModel
import org.christophertwo.aella.ui.screen.workspace.WorkspaceViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ViewModelModules: Module
    get() = module {
        viewModelOf(::HomeViewModel)
        viewModelOf(::WorkspaceViewModel)
        viewModelOf(::WorksViewModel)
    }