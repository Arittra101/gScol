import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

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
            implementation("io.ktor:ktor-client-mock:3.4.0")

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
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
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
        val baseURL = localProperties.getProperty("base.url", "")
        require(baseURL.isNotEmpty()) {
            "Base URL is missing in local.properties!!!"
        }
        buildConfigField(
            STRING,
            "BASE_URL",
            baseURL
        )
    }
}

