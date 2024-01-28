plugins {
    id("com.android.application")

    id("com.google.gms.google-services")
}

android {
    namespace = "com.calberto_barbosa_jr.whatsappclone"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.calberto_barbosa_jr.whatsappclone"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.firebase:firebase-database-ktx")
    implementation ("com.google.firebase:firebase-storage-ktx")
    implementation ("com.google.firebase:firebase-firestore-ktx")

    // Glide
    //https://github.com/bumptech/glide
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    /*Dependências para abas
    *  https://github.com/ogaclejapan/SmartTabLayout
    * */
    implementation ("com.ogaclejapan.smarttablayout:library:2.0.0@aar")
    implementation ("com.ogaclejapan.smarttablayout:utils-v4:2.0.0@aar")

    /*Dependência para arredondamento de imagens*/
    implementation ("de.hdodenhof:circleimageview:2.2.0")

    implementation ("io.gitlab.alexto9090:materialsearchview:1.0.0")
    //implementation ("com.miguelcatalan:materialsearchview:1.4.0")

    implementation ("androidx.activity:activity-ktx:1.8.2")
}