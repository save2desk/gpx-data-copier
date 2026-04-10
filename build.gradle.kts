plugins {
    id("java")
}

group = "biz.gelicon"
version = "1.0-SNAPSHOT"
val log4j2version = "2.6.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("commons-cli:commons-cli:1.9.0")
    implementation("tools.jackson.dataformat:jackson-dataformat-xml:3.1.1")
    implementation("org.codehaus.woodstox:woodstox-core-asl:4.4.1")
    implementation("com.garmin:fit:21.200.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "example.save2.MainApplication"
    }

    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
