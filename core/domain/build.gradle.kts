import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinSerialization)

}

kotlin {

    android {
        namespace = "com.kmp.ollamechat.core.domain"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

//    androidTarget()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {

        sourceSets {
            commonMain.dependencies {

                implementation(libs.koin.core)
                implementation(libs.timber)

                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.kotlinx.serialization.core)

                api(project(":core:common"))

            }
            androidMain.dependencies {
            }
            iosMain.dependencies {
            }
        }
    }
}
