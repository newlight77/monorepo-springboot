import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
	application
	`java-library`
	id("org.springframework.boot") version "2.3.0.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm")
	kotlin("plugin.spring") version "1.3.72"
	kotlin("plugin.jpa") version "1.3.70"
}

buildscript {
	configurations.classpath
			.resolutionStrategy.force("com.github.pinterest:ktlint:0.36.0")
}

group = "io.oneprofile.signup.application"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {

	implementation(project(":shared:util"))
	implementation(project(":shared:sso-keycloak-adapter"))

	implementation(project(":domain:company"))
	implementation(project(":domain:freelance"))
	implementation(project(":domain:metafile"))
	implementation(project(":domain:mission"))
	implementation(project(":domain:notification"))
	implementation(project(":domain:pricer"))
	implementation(project(":domain:signup"))

	implementation(project(":infrastructure:company"))
	implementation(project(":infrastructure:cgu"))
	implementation(project(":infrastructure:encryption"))
	implementation(project(":infrastructure:ip-address"))
	implementation(project(":infrastructure:login"))
	implementation(project(":infrastructure:notification"))
	implementation(project(":infrastructure:freelance"))
	implementation(project(":infrastructure:mission"))
	implementation(project(":infrastructure:pricer-reference"))
	implementation(project(":infrastructure:profile"))
	implementation(project(":infrastructure:signup"))
	implementation(project(":infrastructure:storage"))
	implementation(project(":infrastructure:user"))

	implementation(project(":infrastructure:keycloak-client"))

	implementation("org.springdoc:springdoc-openapi-data-rest:1.4.3")
	implementation("org.springdoc:springdoc-openapi-ui:1.4.3")
//	implementation("org.springdoc:springdoc-openapi-webflux-ui:1.4.3")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.4.3")

	implementation("org.springframework.boot:spring-boot-devtools")

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")

	implementation("com.github.ben-manes.caffeine:caffeine:2.8.1")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
		exclude("junit", "junit")
	}

	testImplementation("com.icegreen:greenmail:1.5.13")


	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.3.0.RELEASE")
	testImplementation("com.h2database:h2")
	implementation("org.postgresql:postgresql")

	implementation("org.liquibase:liquibase-core:3.8.9")

}

application {
	mainClassName = "io.oneprofile.signup.KeycloakAuthApplicationKt"
}

tasks.withType<BootRun> {
	jvmArgs = listOf(
			"-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5080"

	)
	args = listOf(
			"--spring.profiles.active=localhost"
	)
}

tasks.withType<Jar>() {
    baseName = "app-signup-backend"
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
