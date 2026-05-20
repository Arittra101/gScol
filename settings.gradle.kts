import java.util.Properties
import java.io.FileInputStream


rootProject.name = "SCOL"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// ✅ Use rootDir instead of file()
val localProps = Properties().apply {
    val f = File(rootDir, "local.properties")
    if (f.exists()) FileInputStream(f).use { load(it) }
}

// ✅ Quick debug — check if token loaded
println(">>> GitHub token loaded: ${localProps["github.token"] != null}")


pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}


dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Arittra101/cmp_chuker")
            credentials {
                username = localProps["github.username"] as? String ?: ""
                password = localProps["github.token"] as? String ?: ""
            }
        }
    }
}

include(":composeApp")