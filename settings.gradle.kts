pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        //카카오맵 SDK. 원격 저장소
        maven { url = uri ("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")}
    }
}

rootProject.name = "Broaf"
include(":app")
 