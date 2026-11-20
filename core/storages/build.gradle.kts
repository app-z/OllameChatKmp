plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
}

kotlin {

    android {
        namespace = "com.kmp.ollamechat.core.storages"
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

                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)

                implementation(project(":core:domain"))
            }
            androidMain.dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(libs.koin.android)
            }
            iosMain.dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {

    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)

    androidRuntimeClasspath(libs.compose.uiTooling)
}
