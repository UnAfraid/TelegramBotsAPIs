subprojects {
    apply(plugin = "java")
    apply(plugin = "eclipse")
    apply(plugin = "maven-publish")

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    group = "com.github.unafraid.telegram-apis"
    version = "1.0.11"

    repositories {
        mavenCentral()
    }

    tasks["eclipse"].doLast {
        copy {
            from("../eclipse-settings")
            into(".settings")
        }
    }

    tasks["cleanEclipse"].doLast {
        delete(".settings")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
