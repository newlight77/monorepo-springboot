import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
}

group = "io.tricefal.application"
version = "0.0.1-SNAPSHOT"


repositories {
	mavenCentral()
}

subprojects {
	dependencies {
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.3")
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
		implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
		implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.5")

		testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
		testImplementation("org.mockito:mockito-junit-jupiter:3.3.3")
		testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")

		testImplementation("io.rest-assured:spring-mock-mvc:4.3.0") {
			exclude("com.sun.xml.bind:jaxb-osgi")
		}

		testImplementation("io.cucumber:cucumber-java:5.6.0")
		testImplementation("io.cucumber:cucumber-java8:5.6.0")
		testImplementation("io.cucumber:cucumber-junit-platform-engine:5.6.0")
		testImplementation("io.cucumber:cucumber-spring:5.6.0")
		testImplementation("com.github.cukedoctor:cukedoctor-converter:1.2.1")

		testImplementation("io.rest-assured:spring-mock-mvc:4.3.0") {
			exclude("com.sun.xml.bind:jaxb-osgi")
		}

	}
}