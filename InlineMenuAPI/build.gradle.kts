plugins {
    `java-library`
    `jvm-test-suite`
    id("telegram-apis.common-publishing")
}

dependencies {
    api(project(":CoreAPI"))
    api(group = "org.telegram", name = "telegrambots-meta", version = "9.2.0")
}

mavenPublishing {
    pom {
        description.set("Telegram Bots InlineMenu API")
    }
}