import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	kotlin("plugin.spring") version "1.3.72"
	kotlin("plugin.jpa") version "1.3.72"
	idea
}

group = "io.tricefal.infrastructure"
version = "0.0.1-SNAPSHOT"


repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://kotlin.bintray.com/kotlinx") }
}

dependencies {
	implementation(project(":core:domain:authentication"))

	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.3.0.RELEASE")
	implementation("org.springframework.boot:spring-boot-starter-validation:2.3.0.RELEASE")

	implementation("commons-io:commons-io:2.7")


	implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.70")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.70")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.5")

	testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
	testImplementation("org.mockito:mockito-junit-jupiter:3.3.0")
	testImplementation("org.assertj:assertj-core:3.11.1")

}
