import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

val major = 1   // 1 to 99 ~ for big redesigns or breaking changes
val minor = 0   // 0 to 99 ~ for new features
val hotfix = 0  // 0 to 99 ~ for bug fixes only

fun generateVersionCode(): Int {
    val versionCode = major * 100000 + minor * 1000 + hotfix
    println("VersionCode: $versionCode")
    return versionCode
}

fun generateVersionName(): String {
    return "$major.$minor.$hotfix"
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    id("com.codingfeline.buildkonfig")

}

// Load local.properties
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            //koin
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            //okhttp engine android specific
            implementation(libs.ktor.client.okhttp)

            //timber
            implementation(libs.timber)

        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            //navigation 2
            implementation(libs.navigation.compose)


            // Kotlinx Serialization (required for type-safe navigation)
            implementation(libs.kotlinx.serialization.json)

            //material icon
            implementation(compose.materialIconsExtended)

            //koin
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            api(libs.koin.core)

            //ktor
            implementation(libs.bundles.ktor)
            implementation(libs.bundles.coil)
            //implementation("io.ktor:ktor-client-mock:3.4.0")

            //datastore
            api(libs.datastore)
            api(libs.datastore.preferences)

            // back handler For Compose Multiplatform 1.9.1 and above
            implementation(libs.ui.backhandler)

            // coil
            implementation(libs.coil.svg)

            //paging 3 support with cash app
            implementation(libs.paging.common)
            implementation(libs.paging.compose.common)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "org.getscol.gscol"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.getscol.gscol"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = generateVersionCode()
        versionName = generateVersionName()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    flavorDimensions += "environment"

    productFlavors {
        create("qa") {
            dimension = "environment"
            applicationIdSuffix = ".qa"
            resValue("string", "app_name", "SCOL QA")
            versionNameSuffix = "-qa"
        }
        create("prod") {
            dimension = "environment"
            resValue("string", "app_name", "SCOL")
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file(localProperties.getProperty("store.file") ?: "")
            storePassword = localProperties.getProperty("store.password") ?: ""
            keyAlias = localProperties.getProperty("key.alias") ?: ""
            keyPassword = localProperties.getProperty("key.password") ?: ""
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

buildkonfig {
    packageName = "com.getscol.gscol"

    defaultConfigs {
        // detect which flavor is being built from task name
        val isQa = gradle.startParameter.taskNames.any {
            it.contains("qa", ignoreCase = true)
        }

        val baseURL = if (isQa) {
            localProperties.getProperty("qa.base.url", "").also {
                println("BuildKonfig: Using QA URL → $it")
            }
        } else {
            localProperties.getProperty("base.url", "").also {
                println("BuildKonfig: Using PROD URL → $it")
            }
        }

        require(baseURL.isNotEmpty()) { "Base URL is missing in local.properties!!!" }

        buildConfigField(STRING, "BASE_URL", baseURL)
        buildConfigField(
            STRING,
            "GOOGLE_MAPS_API_KEY",
            localProperties.getProperty("google.maps.api.key", "")
        )
    }
}
