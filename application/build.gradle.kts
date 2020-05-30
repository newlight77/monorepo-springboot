
plugins {
//	application
	idea
	`java-library`
	id("org.springframework.boot") version "2.2.5.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm")
	kotlin("plugin.spring") version "1.3.70"
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
	implementation(project(":core:infrastructure"))

	compile("org.springframework.boot:spring-boot-devtools")

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")

//	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:2.2.5.RELEASE")
//	implementation("org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure:2.2.6.RELEASE")
//	implementation("org.springframework.security.oauth:spring-security-oauth2:2.4.1.RELEASE")
//	implementation("org.springframework.security:spring-security-oauth2-jose:5.3.1.RELEASE")

//	implementation("org.springframework.security.oauth:spring-security-oauth2:2.2.5.RELEASE")
//	implementation("org.springframework.boot:spring-security-oauth2:2.2.5.RELEASE")
//	implementation("org.springframework.security:spring-security-jwt:1.1.0.RELEASE")

	implementation("com.okta.spring:okta-spring-boot-starter:1.4.0")
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

	testImplementation("io.cucumber:cucumber-java:5.6.0")
	testImplementation("io.cucumber:cucumber-java8:5.6.0")
	testImplementation("io.cucumber:cucumber-junit-platform-engine:5.6.0")
	testImplementation("io.cucumber:cucumber-spring:5.6.0")
	testImplementation("com.github.cukedoctor:cukedoctor-converter:1.2.1")

    testImplementation("io.rest-assured:spring-mock-mvc:4.3.0") {
        exclude("com.sun.xml.bind:jaxb-osgi")
    }
	// testImplementation("io.rest-assured:rest-assured:4.3.0") {
	// 	exclude("com.sun.xml.bind", "jaxb-osgi")
	// }
//	testImplementation("io.rest-assured:json-path:4.3.0")
//	testImplementation("io.rest-assured:xml-path:4.3.0")

//	testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
//	testImplementation("org.mockito:mockito-junit-jupiter:3.3.3")
//	testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")


	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.2.5.RELEASE")
	testImplementation("com.h2database:h2")
	implementation("org.postgresql:postgresql")

	implementation("org.liquibase:liquibase-core:3.8.9")

}

//application {
//	mainClassName = "io.github.newlight77.bootstrap.HelloWorldKt"
//}

tasks.withType<Jar>() {
    baseName = "core-application"
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
