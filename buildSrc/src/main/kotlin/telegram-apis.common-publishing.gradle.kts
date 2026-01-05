plugins {
    `java-library`
    `jvm-test-suite`
    signing
    id("com.vanniktech.maven.publish")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.21.0")
}

tasks.named<Test>("test") {
    useJUnitPlatform()

    testLogging {
        events("passed")
    }
}

signing {
    useGpgCmd()
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    pom {
        name.set(project.name)
        description.set("Telegram Bots module")
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

