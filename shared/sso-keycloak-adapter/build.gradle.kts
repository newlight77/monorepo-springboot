
plugins {
	kotlin("jvm")
}

buildscript {
	configurations.classpath
			.resolutionStrategy.force("com.github.pinterest:ktlint:0.36.0")
}

group = "io.tricefal.shared"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {

	implementation(project(":shared:util"))
	implementation(project(":infrastructure:ip-address"))

	implementation("org.springframework.boot:spring-boot-starter-security:2.3.0.RELEASE")
	implementation("org.keycloak:keycloak-spring-security-adapter:12.0.3")
	implementation("org.keycloak:keycloak-spring-boot-starter:12.0.3")
	implementation("com.github.ben-manes.caffeine:caffeine:2.8.1")

	testImplementation("org.springframework.boot:spring-boot-starter-test:2.3.0.RELEASE") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
		exclude("junit", "junit")
	}

}

tasks.withType<Jar>() {
	baseName = "sso-keycloak-adapter"
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
