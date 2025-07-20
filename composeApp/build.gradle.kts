import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    kotlin("plugin.serialization").version("2.2.0")
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            with(libs) {//kotlin
                implementation(kotlin.serialization)
            }
            with(compose) {//compose
                implementation(runtime)
                implementation(foundation)
                implementation(material3)
                implementation(ui)
                implementation(components.resources)
                implementation(components.uiToolingPreview)
                implementation(libs.navigation.compose)
            }

            with(libs) {//androidx
                implementation(androidx.lifecycle.viewmodel)
                implementation(androidx.lifecycle.runtimeCompose)
            }

            with(libs) {//koin
                implementation(project.dependencies.platform(koin.bom))
                implementation(koin.core)
                implementation(koin.compose)
                implementation(koin.compose.viewmodel)
                implementation(koin.compose.viewmodel.navigation)
            }
            with(libs) {//ktor
                implementation(project.dependencies.platform(ktor.bom))
                implementation(ktor.client.core)
                implementation(ktor.client.cio)
                implementation(ktor.client.logging)
                implementation(ktor.client.serialization)
            }
            with(libs) {//room
                implementation(androidx.room.runtime)
                implementation(libs.sqlite.bundled)
            }
            with(libs) {//utils
                implementation(composeIcons.evaIcons)
                implementation(sdp.ssp.compose.multiplatform)
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.materialKolor)
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}

compose.desktop {
    application {
        mainClass = "org.christophertwo.aella.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "aella"
            packageVersion = "1.0.0"
        }
    }
}
