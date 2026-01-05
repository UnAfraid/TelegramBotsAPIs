plugins {
    `java-library`
    `jvm-test-suite`
    id("telegram-apis.common-publishing")
}

dependencies {
    api(group = "org.slf4j", name = "slf4j-api", version = "2.0.17")
    api(group = "org.telegram", name = "telegrambots-longpolling", version = "9.2.0")
    testImplementation(group = "org.apache.logging.log4j", name = "log4j-slf4j2-impl", version = "2.25.3")
    testImplementation(group = "org.apache.logging.log4j", name = "log4j-core", "2.25.3")
}

mavenPublishing {
    pom {
        description.set("Telegram Bots Core API")
    }
}