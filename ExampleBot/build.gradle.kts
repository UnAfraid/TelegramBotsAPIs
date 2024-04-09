plugins {
    `java-library`
}

dependencies {
    api(project(":CoreAPI"))
    api(project(":InlineMenuAPI"))
    api(group = "org.telegram", name = "telegrambots-meta", version = "7.2.0")
    api(group = "org.telegram", name = "telegrambots-client", version = "7.2.0")
    api(group = "org.apache.logging.log4j", name = "log4j-slf4j2-impl", version = "2.21.0")
    api(group = "org.apache.logging.log4j", name = "log4j-core", "2.21.0")
}
