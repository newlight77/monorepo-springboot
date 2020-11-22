import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	kotlin("plugin.spring") version "1.3.72"
	kotlin("plugin.jpa") version "1.3.72"
}

group = "io.tricefal.infrastructure"
version = "0.0.1-SNAPSHOT"


repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://kotlin.bintray.com/kotlinx") }
}

dependencies {
	implementation(project(":domain:authentication"))
	implementation(project(":domain:metafile"))
	implementation(project(":infrastructure:iam-client:iam-client-interface"))

	implementation("org.springframework.boot:spring-boot-starter:2.3.0.RELEASE")

	// okta service
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")
	implementation("org.mindrot:jbcrypt:0.4")


}

tasks.withType<Jar>() {
	baseName = "infra-okta-client"
}
