plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinSerialization)

}

kotlin {

    androidLibrary {
        namespace = "com.kmp.ollamechat.core.network"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

//    androidTarget()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {

        sourceSets {
            commonMain.dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)

                implementation(libs.koin.core)

                implementation(libs.timber)
            }
            androidMain.dependencies {
                implementation(libs.ktor.client.okhttp)
            }
            iosMain.dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}
