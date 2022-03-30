import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	kotlin("plugin.spring") version "1.6.10"
	kotlin("plugin.jpa") version "1.6.10"
}

group = "io.oneprofile.signup.infrastructure"
version = "0.0.1-SNAPSHOT"


repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://kotlin.bintray.com/kotlinx") }
}

dependencies {
	implementation(project(":domain:signup"))

	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.5")
	implementation("org.springframework.boot:spring-boot-starter-validation:2.6.5")

}

tasks.withType<Jar>() {
	baseName = "infra-login"
}