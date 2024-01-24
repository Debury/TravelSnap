pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
    repositories {
        // Depending on AndroidX Snapshot Builds to get the latest CameraX libs.
        maven { setUrl("https://androidx.dev/snapshots/builds/6787662/artifacts/repository/")}
        maven { url = uri("https://jitpack.io") }

    }
}

rootProject.name = "TravelSnap"
include(":app")
 