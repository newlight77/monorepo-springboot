import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
}

group = "io.oneprofile.signup.domain"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {

	implementation(project(":domain:company"))
	implementation(project(":domain:notification"))
	implementation(project(":shared:util"))

}

tasks.withType<Jar>() {
	baseName = "domain-freelance"
}