import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
//	id("io.spring.dependency-management") version "1.0.11.RELEASE"
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
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.5")
	implementation("org.springframework.boot:spring-boot-starter-validation:2.6.5")

	implementation("io.jsonwebtoken:jjwt-api:0.11.2")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.2")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.2")
    implementation(kotlin("stdlib-jdk8"))

}

tasks.withType<Jar>() {
	archiveBaseName.set("infra-encryption")
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}