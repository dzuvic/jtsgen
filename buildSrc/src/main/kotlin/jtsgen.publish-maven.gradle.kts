import org.gradle.kotlin.dsl.`maven-publish`

plugins {
    `maven-publish`
    signing
}

extra["isReleaseVersion"] = !version.toString().endsWith("SNAPSHOT")

val ossrhUsername: String? by project
val ossrhPassword: String? by project
val user = if (ossrhUsername == null) System.getenv("OSSRH_USER") else  ossrhUsername
val pass = if (ossrhPassword == null) System.getenv("OSSRH_PASSWORD") else  ossrhPassword


publishing {

    repositories {
        maven {
            val mavenSnapshotRepoUrl : String by project
            val mavenRepoUrl : String by project

            url = if (version.toString().endsWith("SNAPSHOT")) uri(mavenSnapshotRepoUrl) else uri(mavenRepoUrl)
            name = "mavenCentral"

            if (url.toString().contains("http")) credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
}

signing {
    setRequired({
        (project.extra["isReleaseVersion"] as Boolean) && gradle.taskGraph.hasTask("publish")
    })
}

