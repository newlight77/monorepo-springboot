import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
}

buildscript {
	configurations.classpath
			.get().resolutionStrategy.force("com.github.pinterest:ktlint:0.36.0")
}

group = "io.oneprofile.shared"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {

	implementation(project(":shared:util"))
	implementation(project(":infrastructure:ip-address"))

	implementation("org.springframework.boot:spring-boot-starter-security:2.6.5")
	implementation("org.keycloak:keycloak-spring-security-adapter:17.0.1")
	implementation("org.keycloak:keycloak-spring-boot-starter:17.0.1")
	implementation("com.github.ben-manes.caffeine:caffeine:3.0.6")

	testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.5") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
		exclude("junit", "junit")
	}
    implementation(kotlin("stdlib-jdk8"))

}

tasks.withType<Jar>() {
	archiveBaseName.set("sso-keycloak-adapter")
}

testlogger {
	theme = com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA_PARALLEL
	showExceptions = true
	showStackTraces = true
	showFullStackTraces = true
	showCauses = true
	showSummary = true
	showStandardStreams = false
	showPassedStandardStreams = true
	showSkippedStandardStreams = true
	showFailedStandardStreams = true
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}