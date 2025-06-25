plugins {
    alias(libs.plugins.nowinandroid.android.library)
    alias(libs.plugins.nowinandroid.android.feature)
    alias(libs.plugins.nowinandroid.android.library.compose)
    alias(libs.plugins.nowinandroid.android.library.jacoco)
}

android {
    namespace = "com.karuhun.navigation"
}

dependencies {
    implementation(projects.feature.home.ui)
    implementation(projects.feature.mainMenu.ui)
}