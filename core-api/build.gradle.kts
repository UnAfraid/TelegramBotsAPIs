plugins {
    `maven-publish`
    `java-library`
    signing
}

dependencies {
    api(group = "org.slf4j", name = "slf4j-api", version = "2.0.17")
    api(group = "org.telegram", name = "telegrambots-longpolling", version = "9.2.0")
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "6.0.1")
    testImplementation(group = "org.apache.logging.log4j", name = "log4j-slf4j2-impl", version = "2.25.3")
    testImplementation(group = "org.apache.logging.log4j", name = "log4j-core", "2.25.3")
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
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

