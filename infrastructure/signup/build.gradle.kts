import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	kotlin("plugin.spring") version "1.3.72"
	kotlin("plugin.jpa") version "1.3.72"
}

group = "io.oneprofile.signup.infrastructure"
version = "0.0.1-SNAPSHOT"


repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://kotlin.bintray.com/kotlinx") }
}

dependencies {
	implementation(project(":domain:metafile"))
	implementation(project(":domain:notification"))
	implementation(project(":domain:signup"))
	implementation(project(":infrastructure:cgu"))
	implementation(project(":infrastructure:encryption"))
	implementation(project(":infrastructure:freelance"))
	implementation(project(":infrastructure:notification"))
	implementation(project(":infrastructure:profile"))
	implementation(project(":infrastructure:storage"))

	implementation(project(":infrastructure:keycloak-client"))

	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.3.0.RELEASE")
	implementation("org.springframework.boot:spring-boot-starter-validation:2.3.0.RELEASE")

}

tasks.withType<Jar>() {
	baseName = "infra-signup"
}