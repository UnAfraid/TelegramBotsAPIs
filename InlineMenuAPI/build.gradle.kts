plugins {
    id("com.vanniktech.maven.publish") version "0.33.0"
    `java-library`
    `jvm-test-suite`
    signing
}

dependencies {
    api(project(":CoreAPI"))
    api(group = "org.telegram", name = "telegrambots-meta", version = "9.2.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
        description.set("Telegram Bots InlineMenu API")
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