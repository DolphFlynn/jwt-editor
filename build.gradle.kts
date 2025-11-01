plugins {
    id("java")
    id("checkstyle")
}

group = "com.blackberry"
version = "2.6"
description = "jwt-editor"

repositories {
    mavenCentral()
    maven { url = uri("https://www.jetbrains.com/intellij-repository/releases") }
    maven { url = uri("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies/") }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

checkstyle {
    toolVersion = "10.12.4"
}

configurations {
    create("guiGenerationTask")
}

dependencies {
    "guiGenerationTask"("com.jetbrains.intellij.java:java-compiler-ant-tasks:252.26199.158")

    compileOnly("org.bouncycastle:bcprov-jdk18on:1.82")
    compileOnly("org.bouncycastle:bcpkix-jdk18on:1.82")
    compileOnly("net.portswigger.burp.extensions:montoya-api:2024.7")

    implementation("com.jetbrains.intellij.java:java-gui-forms-rt:253.18871")
    implementation("com.nimbusds:nimbus-jose-jwt:9.21")
    implementation("org.exbin.deltahex:deltahex-swing:0.1.2")
    implementation("com.fifesoft:rsyntaxtextarea:3.6.0")
    implementation("org.json:json:20250517")

    testImplementation("org.bouncycastle:bcprov-jdk18on:1.82")
    testImplementation("org.bouncycastle:bcpkix-jdk18on:1.82")
    testImplementation("net.portswigger.burp.extensions:montoya-api:2024.7")
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.1")
    testImplementation("org.assertj:assertj-core:3.27.6")
    testImplementation("org.mockito:mockito-core:5.19.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Checkstyle> {
    reports {
        xml.required = false
        html.required = true
    }
}

tasks.withType<JavaCompile> {
    val mainSourceSets = project.sourceSets["main"]

    doLast {
        mainSourceSets.output.classesDirs.forEach { mkdir(it) }

        ant.withGroovyBuilder {
            "taskdef"(
                "name" to "javac2",
                "classname" to "com.intellij.ant.Javac2",
                "classpath" to configurations["guiGenerationTask"].asPath
            )
        }

        ant.withGroovyBuilder {
            "javac2"(
                "srcdir" to mainSourceSets.java.srcDirs.joinToString(":"),
                "classpath" to mainSourceSets.compileClasspath.asPath,
                "destdir" to mainSourceSets.output.classesDirs.singleFile,
                "release" to targetCompatibility.toString(),
                "includeAntRuntime" to false
            )
        }
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()

    systemProperty("user.timezone", "UTC")
}

tasks.named<Jar>("jar") {
    dependsOn("test")
    archiveBaseName.set(project.name)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(
        configurations.runtimeClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
                .matching {
                    exclude(
                        "org/exbin/deltahex/swing/DefaultCodeAreaCommandHandler.class",
                        "org/exbin/deltahex/swing/DefaultCodeAreaCommandHandler\$1.class",
                        "org/exbin/deltahex/swing/DefaultCodeAreaCommandHandler\$2.class",
                        "org/exbin/deltahex/swing/DefaultCodeAreaCommandHandler\$BinaryDataClipboardData.class",
                        "org/exbin/deltahex/swing/DefaultCodeAreaCommandHandler\$ClipboardData.class",
                        "org/exbin/deltahex/swing/DefaultCodeAreaCommandHandler\$CodeDataClipboardData.class",
                        "META-INF/LICENSE"
                    )
                }
        }
    )

    from(rootDir) {
        include("LICENSE.md")
        into("META-INF")
    }
}
