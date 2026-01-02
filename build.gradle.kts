plugins {
    `maven-publish`
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    group = "com.github.unafraid.telegram-apis"
    version = "2.0.3"

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    publishing {
        repositories {
            maven {
                name = "MavenCentral"
                url = uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
                credentials {
                    username = getRepositoryUsername()
                    password = getRepositoryPassword()
                }
            }
        }
    }

    configure<PublishingExtension> {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/unafraid/telegrambotsapis")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
                }
            }
        }
        publications {
            register<MavenPublication>("gpr") {
                from(components["java"])
            }
        }
    }
}


fun getRepositoryUsername(): String {
    return project.findProperty("ossrhUsername") as String?
        ?: ""
}

fun getRepositoryPassword(): String {
    return project.findProperty("ossrhPassword") as String?
        ?: ""
}