import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
}

group = "io.tricefal.shared"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {

	implementation("com.github.java-json-tools:json-patch:1.13")
	implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
	implementation("com.squareup.moshi:moshi-adapters:1.11.0")

}

tasks.withType<Jar>() {
	baseName = "shared-util"
}