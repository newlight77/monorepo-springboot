import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	idea
}

group = "io.tricefal.infrastructure"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://kotlin.bintray.com/kotlinx") }
}

subprojects {

	apply { plugin("java") }
	apply { plugin("idea") }

	java {
		sourceCompatibility = JavaVersion.VERSION_13
		targetCompatibility = JavaVersion.VERSION_13
	}

	dependencies {
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.3")
		implementation("org.slf4j:slf4j-api:1.7.30")
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
		implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
		implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.5")

		testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
		testImplementation("org.mockito:mockito-junit-jupiter:3.6.0")
		testImplementation("org.mockito:mockito-inline:3.6.0")
		testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
		testImplementation("org.assertj:assertj-core:3.11.1")
	}
}
