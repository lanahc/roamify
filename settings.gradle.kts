// In settings.gradle.kts

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        // --- THIS IS THE FIX ---
        gradlePluginPortal {
            content {
                // Declare that the KSP plugin is found here
                includeGroup("com.google.devtools.ksp")
            }
        }
    }
}

// ... rest of your file is likely correct
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Roamify"
include(":app")
