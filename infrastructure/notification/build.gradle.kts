import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	kotlin("plugin.spring") version "1.9.10"
	kotlin("plugin.jpa") version "1.9.10"
}

group = "io.oneprofile.signup.infrastructure"
version = "0.0.1-SNAPSHOT"


repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://kotlin.bintray.com/kotlinx") }
}

dependencies {

	implementation(project(":domain:notification"))

	implementation("org.springframework.boot:spring-boot-starter:2.6.5")


	// notification
	implementation("org.springframework.boot:spring-boot-starter-mail:2.6.5")
	api("org.freemarker:freemarker:2.3.31")
	testImplementation("com.icegreen:greenmail:1.6.7")

	// twilio
	implementation("com.twilio.sdk:twilio:8.28.0")
    implementation(kotlin("stdlib-jdk8"))

}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}