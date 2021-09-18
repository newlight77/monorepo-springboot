import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	kotlin("plugin.spring") version "1.3.72"
	kotlin("plugin.jpa") version "1.3.72"
}

group = "io.oneprofile.infrastructure"
version = "0.0.1-SNAPSHOT"


repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://kotlin.bintray.com/kotlinx") }
}

dependencies {

	implementation(project(":domain:notification"))

	implementation("org.springframework.boot:spring-boot-starter:2.3.0.RELEASE")


	// notification
	implementation("org.springframework.boot:spring-boot-starter-mail:2.3.0.RELEASE")
	api("org.freemarker:freemarker:2.3.30")
	testImplementation("com.icegreen:greenmail:1.5.13")

	// twilio
	implementation("com.twilio.sdk:twilio:7.51.0")

}
