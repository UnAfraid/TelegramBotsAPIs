plugins {
    id("com.vanniktech.maven.publish") version "0.33.0"
    `java-library`
    `jvm-test-suite`
    signing
}

dependencies {
    api(group = "org.slf4j", name = "slf4j-api", version = "2.0.17")
    api(group = "org.telegram", name = "telegrambots-longpolling", version = "9.2.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.21.0")
    testImplementation(group = "org.apache.logging.log4j", name = "log4j-slf4j2-impl", version = "2.25.3")
    testImplementation(group = "org.apache.logging.log4j", name = "log4j-core", "2.25.3")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }

        register<JvmTestSuite>("integrationTest") {
            dependencies {
                implementation(project())
            }

            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                    }
                }
            }
        }
    }
}

tasks.named("check") {
    dependsOn(testing.suites.named("integrationTest"))
}

signing {
    useGpgCmd()
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()
}

mavenPublishing {
    pom {
        name.set(project.name)
        description.set("Telegram Bots Core API")
        inceptionYear.set("2020")
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