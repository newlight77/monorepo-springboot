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

	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.5")
	implementation("org.springframework.boot:spring-boot-starter-validation:2.6.5")
    implementation(kotlin("stdlib-jdk8"))

}

tasks.withType<Jar>() {
	archiveBaseName.set("infra-signup")
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}