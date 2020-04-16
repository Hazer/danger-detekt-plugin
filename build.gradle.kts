import java.util.Date

plugins {
    kotlin("jvm") version "1.3.72"
    // Documentation for our code
    id("org.jetbrains.dokka") version "0.10.1"
    // Publication to bintray
    id("com.jfrog.bintray") version "1.8.5"
    // Maven publication
    `maven-publish`
}

group = "io.vithor.danger.plugins"
version = "0.0.5"

repositories {
    mavenCentral()
    jcenter()
    maven(url = "https://dl.bintray.com/hazer/maven")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("systems.danger:danger-kotlin-sdk:1.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks {
    dokka {
        outputFormat = "html"
        outputDirectory = "$buildDir/javadoc"
        configuration {
            moduleName = rootProject.name
            targets = listOf("JVM")
            platform = "jvm"
        }
    }
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(tasks.dokka)
    dependsOn(tasks.dokka)
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}


val gitRepo = "https://github.com/Hazer/danger-detekt-plugin"
val issueUrl = ".$gitRepo/issues"
val scmUrl = "$gitRepo.git"
val pomDesc = "Detekt Plugin for Danger"

val githubReadme = "https://github.com/Hazer/danger-detekt-plugin/blob/master/README.md"

val pomLicenseName = "The MIT License"
val pomLicenseUrl = "http://www.opensource.org/licenses/MIT"
val pomLicenseDist = "repo"

val pomDeveloperId = "Hazer"
val pomDeveloperName = "Vithorio Polten"

publishing {
    publications {
        create<MavenPublication>("lib") {
            groupId = project.group as String
            artifactId = rootProject.name
            // version is gotten from an external plugin
            version = project.version as String
            // This is the main artifact
            from(components["java"])
            // We are adding documentation artifact
            artifact(dokkaJar)
            // And sources
            artifact(sourcesJar)

            pom.withXml {
                asNode().apply {
                    appendNode("description", pomDesc)
                    appendNode("name", rootProject.name)
                    appendNode("url", gitRepo)
                    appendNode("licenses").appendNode("license").apply {
                        appendNode("name", pomLicenseName)
                        appendNode("url", pomLicenseUrl)
                        appendNode("distribution", pomLicenseDist)
                    }
                    appendNode("developers").appendNode("developer").apply {
                        appendNode("id", pomDeveloperId)
                        appendNode("name", pomDeveloperName)
                    }
                    appendNode("scm").apply {
                        appendNode("url", scmUrl)
//                        appendNode("connection", pomScmConnection)
                    }
                }
            }
        }
    }
}

bintray {
    // Getting bintray user and key from properties file or command line
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")

    // Automatic publication enabled
    publish = true

    // Set maven publication onto bintray plugin
    setPublications("lib")

    // Configure package
    pkg.apply {
        repo = "maven"
        name = rootProject.name
        setLicenses("MIT")
        setLabels("Danger", "Danger-Kotlin", "Detekt", "Plugin", "DangerPlugin", "Kotlin")
        vcsUrl = scmUrl
        websiteUrl = gitRepo
        issueTrackerUrl = issueUrl
        githubRepo = gitRepo
        githubReleaseNotesFile = githubReadme

        // Configure version
        version.apply {
            name = project.version as String
            desc = pomDesc
            released = Date().toString()
            vcsTag = project.version as String
        }
    }
}