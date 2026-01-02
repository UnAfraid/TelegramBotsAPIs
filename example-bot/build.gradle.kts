plugins {
    `java-library`
}

dependencies {
    api(project(":core-api"))
    api(project(":inline-menu-api"))
    api(group = "org.telegram", name = "telegrambots-meta", version = "9.2.0")
    api(group = "org.telegram", name = "telegrambots-client", version = "9.2.0")
    api(group = "org.apache.logging.log4j", name = "log4j-slf4j2-impl", version = "2.25.3")
    api(group = "org.apache.logging.log4j", name = "log4j-core", "2.25.3")
}
