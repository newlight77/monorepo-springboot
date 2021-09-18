import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
}

group = "io.oneprofile.domain"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {

}

tasks.withType<Jar>() {
	baseName = "domain-metafile"
}