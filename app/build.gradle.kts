plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("com.google.gms.google-services")

    // Injeção de dependência
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.calberto_barbosa_jr.interactus"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.calberto_barbosa_jr.interactus"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    viewBinding { enable = true }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Injeção de dependência
    // https://developer.android.com/training/dependency-injection/hilt-android?hl=pt-br#kts
    implementation("com.google.dagger:hilt-android:2.56.2")
    ksp("com.google.dagger:hilt-android-compiler:2.56.2")

    implementation ("io.insert-koin:koin-core:3.5.0")
    implementation ("io.insert-koin:koin-android:3.5.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation ("com.google.android.gms:play-services-auth:21.2.0")
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.firebase:firebase-database-ktx")
    implementation ("com.google.firebase:firebase-storage-ktx")
    implementation ("com.google.firebase:firebase-firestore-ktx")

    implementation("io.coil-kt:coil-compose:2.5.0")

    //===================================================================
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    /*Dependências para abas
    *  https://github.com/ogaclejapan/SmartTabLayout
    * */
    implementation ("com.ogaclejapan.smarttablayout:library:2.0.0@aar")
    implementation ("com.ogaclejapan.smarttablayout:utils-v4:2.0.0@aar")

    /*Dependência para arredondamento de imagens*/
    implementation ("de.hdodenhof:circleimageview:2.2.0")

    implementation ("io.gitlab.alexto9090:materialsearchview:1.0.0")

    implementation ("androidx.activity:activity-ktx:1.8.2")


    // Glide
    //https://github.com/bumptech/glide
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")

    // ROOM
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    // Coroutines support for Room
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

}