plugins {
    `maven-publish`
    `java-library`
    signing
}

dependencies {
    api(project(":CoreAPI"))
    api(group = "org.telegram", name = "telegrambots-meta", version = "7.7.0")
    testImplementation(group = "junit", name ="junit", version = "4.13.2")
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    repositories {
        maven {
            name = "MavenCentral"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials {
                username = getRepositoryUsername()
                password = getRepositoryPassword()
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set(project.name)
                description.set("Telegram Bots InlineMenu API")
                url.set("https://github.com/UnAfraid/TelegramBotsAPIs")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set("UnAfraid")
                        name.set("Rumen Nikiforov")
                        email.set("unafraid89@gmail.com")
                        organization.set("L2JUnity")
                        organizationUrl.set("https://l2junity.org")
                    }
                }

                scm {
                    connection.set("scm:git:git@github.com:UnAfraid/TelegramBotsAPIs.git")
                    developerConnection.set("scm:git:git@github.com:UnAfraid/TelegramBotsAPIs.git")
                    url.set("git@github.com:UnAfraid/TelegramBotsAPIs.git")
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["maven"])
}

fun getRepositoryUsername(): String {
    return project.findProperty("ossrhUsername") as String?
            ?: ""
}

fun getRepositoryPassword(): String {
    return project.findProperty("ossrhPassword") as String?
            ?: ""
}
