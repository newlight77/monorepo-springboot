
plugins {
//	application
	idea
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

group = "io.tricefal"
version = "0.0.1-SNAPSHOT"


repositories {
	mavenCentral()
}

dependencies {
	implementation(project(":core:domain"))
//	implementation(project(":core:infrastructure:note"))

//	implementation("org.springframework:spring-core:5.2.8.RELEASE")
//	implementation("org.springframework:spring-context:5.2.8.RELEASE")
//	implementation("org.springframework:spring-web:5.2.8.RELEASE")

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")

	implementation("org.springframework.security:spring-security-oauth2-client")
	implementation("org.springframework.security:spring-security-oauth2-jose")
	implementation("org.keycloak:keycloak-spring-security-adapter:11.0.0")
	implementation("com.okta.jwt:okta-jwt-verifier:0.4.0")
	implementation("com.okta.jwt:okta-jwt-verifier-impl:0.4.0")
	implementation("io.jsonwebtoken:jjwt:0.9.1")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.fasterxml.jackson.module:jackson-module-jaxb-annotations")
	implementation("com.github.ben-manes.caffeine:caffeine:2.8.1")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.5")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
		exclude("junit", "junit")
	}

    testImplementation("io.rest-assured:spring-mock-mvc:4.3.0") {
        exclude("com.sun.xml.bind:jaxb-osgi")
    }

	testImplementation("io.cucumber:cucumber-java:5.6.0")
	testImplementation("io.cucumber:cucumber-java8:5.6.0")
	testImplementation("io.cucumber:cucumber-junit-platform-engine:5.6.0")
	testImplementation("io.cucumber:cucumber-spring:5.6.0")
	testImplementation("com.github.cukedoctor:cukedoctor-converter:1.2.1")

	testImplementation("io.rest-assured:spring-mock-mvc:4.3.0") {
		exclude("com.sun.xml.bind:jaxb-osgi")
	}

	testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
	testImplementation("org.mockito:mockito-junit-jupiter:3.3.0")
	testImplementation("org.assertj:assertj-core:3.11.1")


	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.3.0.RELEASE")
	testImplementation("com.h2database:h2")
	implementation("org.postgresql:postgresql")

	implementation("org.liquibase:liquibase-core:3.8.9")

}

tasks.withType<Jar>() {
    baseName = "core-application-note"
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
