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

	api("com.github.java-json-tools:json-patch:1.13")
	implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
	implementation("com.squareup.moshi:moshi-adapters:1.11.0")

//	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.3")
	implementation("org.slf4j:slf4j-api:1.7.30")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.5")

	testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
	testImplementation("org.mockito:mockito-junit-jupiter:3.3.3")
	testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
}

tasks.withType<Jar>() {
	baseName = "shared-util"
}