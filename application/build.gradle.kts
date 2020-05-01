import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jmailen.gradle.kotlinter.tasks.LintTask

plugins {
	application
	idea
	`java-library`
	id("org.springframework.boot") version "2.2.5.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	id("org.jmailen.kotlinter") version "2.3.2"
	id("com.diffplug.gradle.spotless") version "3.27.2"
	kotlin("jvm")
	kotlin("plugin.spring") version "1.3.70"
	kotlin("plugin.jpa") version "1.3.70"
	checkstyle
	jacoco
}

buildscript {
	configurations.classpath
			.resolutionStrategy.force("com.github.pinterest:ktlint:0.36.0")
}

group = "io.tricefal"
version = "0.0.1-SNAPSHOT"
//java.sourceCompatibility = JavaVersion.VERSION_1_8

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
	implementation("com.github.ben-manes.caffeine:caffeine:2.8.1")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.5")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
		exclude("junit", "junit")
	}

	testImplementation("io.cucumber:cucumber-java8:5.6.0")
	testImplementation("io.cucumber:cucumber-junit-platform-engine:5.6.0")
	testImplementation("io.cucumber:cucumber-spring:5.6.0")
	testImplementation("com.github.cukedoctor:cukedoctor-converter:1.2.1")

	testImplementation("io.rest-assured:rest-assured:4.3.0") {
		exclude("com.sun.xml.bind", "jaxb-osgi")
//		exclude("org.codehaus.groovy", "groovy")
	}
	testImplementation("io.rest-assured:json-path:4.3.0")
	testImplementation("io.rest-assured:xml-path:4.3.0")
//	testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
//	testImplementation("org.mockito:mockito-junit-jupiter:3.3.3")
//	testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")



	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.2.5.RELEASE")
	testImplementation("com.h2database:h2")
	runtime("org.postgresql:postgresql")

//	compile("org.liquibase:liquibase-core:3.8.9")

}

configurations {
	implementation {
		resolutionStrategy.failOnVersionConflict()
	}
}

sourceSets {
	main {
		java.srcDir("src/main/java")
	}
}

java {
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
}


//application {
//	mainClassName = "io.github.newlight77.bootstrap.HelloWorldKt"
//}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
		exceptionFormat = TestExceptionFormat.FULL
		showExceptions = true
		showCauses = true
		showStackTraces = true
		showStandardStreams = true
	}

	val failedTests = mutableListOf<TestDescriptor>()
	val skippedTests = mutableListOf<TestDescriptor>()

	// See https://technology.lastminute.com/junit5-kotlin-and-gradle-dsl/
	// See https://github.com/gradle/kotlin-dsl/issues/836
	addTestListener(object : TestListener {
		override fun beforeSuite(suite: TestDescriptor) {}
		override fun beforeTest(testDescriptor: TestDescriptor) {}
		override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
			when (result.resultType) {
				TestResult.ResultType.FAILURE -> failedTests.add(testDescriptor)
				TestResult.ResultType.SKIPPED -> skippedTests.add(testDescriptor)
				else -> Unit
			}
		}

		override fun afterSuite(suite: TestDescriptor, result: TestResult) {
			if (suite.parent == null) { // root suite
				logger.lifecycle("----")
				logger.lifecycle("Test result: ${result.resultType}")
				logger.lifecycle(
						"Test summary: ${result.testCount} tests, " +
								"${result.successfulTestCount} succeeded, " +
								"${result.failedTestCount} failed, " +
								"${result.skippedTestCount} skipped")
				failedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tFailed Tests")
				skippedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tSkipped Tests:")
			}
		}

		private infix fun List<TestDescriptor>.prefixedSummary(subject: String) {
			logger.lifecycle(subject)
			forEach { test -> logger.lifecycle("\t\t${test.displayName()}") }
		}

		private fun TestDescriptor.displayName() = parent?.let { "${it.name} - $name" } ?: "$name"
	})
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<Jar>() {
    baseName = "core-application"
}


tasks.jacocoTestReport {
	reports {
		xml.isEnabled = false
		csv.isEnabled = false
		html.isEnabled = true
		html.destination = file("$buildDir/jacocoHtml")
	}
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				minimum = "0.5".toBigDecimal()
			}
		}

		rule {
			enabled = false
			element = "CLASS"
			includes = listOf("io.tricefal.*")

			limit {
				counter = "LINE"
				value = "TOTALCOUNT"
				maximum = "0.3".toBigDecimal()
			}
		}
	}
	classDirectories.setFrom(
			sourceSets.main.get().output.asFileTree.matching {
				// exclude main()
				exclude("io/tricefal/core/TricefalApplicationKt.class")
			}
	)
}

tasks.checkstyleMain { group = "verification" }
tasks.checkstyleTest { group = "verification" }

val testCoverage by tasks.registering {
	group = "verification"
	description = "Runs the unit tests with coverage."

	dependsOn(":test", ":jacocoTestReport", ":jacocoTestCoverageVerification")
	val jacocoTestReport = tasks.findByName("jacocoTestReport")
	jacocoTestReport?.mustRunAfter(tasks.findByName("test"))
	tasks.findByName("jacocoTestCoverageVerification")?.mustRunAfter(jacocoTestReport)
}

jacoco {
	toolVersion = "0.8.5"
	reportsDir = file("$buildDir/jacoco")
}

spotless {
	kotlin {
		ktlint()
	}
	kotlinGradle {
		target(fileTree(projectDir).apply {
			include("*.gradle.kts")
		} + fileTree("src").apply {
			include("**/*.gradle.kts")
		})
		ktlint()
	}
}

kotlinter {
	ignoreFailures = false
	indentSize = 4
	reporters = arrayOf("checkstyle", "plain")
	experimentalRules = false
	disabledRules = emptyArray<String>()
	fileBatchSize = 30
}

// custom linting
tasks {
	"lintKotlinMain"(LintTask::class) {
		exclude("**/*Generated.kt")
	}
}

//sonarqube.properties["sonar.host.url"] = "http://localhost:9000"
//sonarqube.properties["sonar.projectName"] = "tricefal-core"
//sonarqube.properties["sonar.language"] = "kotlin"
//sonarqube.properties["sonar.login"] = "admin"
//sonarqube.properties["sonar.passwrod"] = "admin"

//sonarqube {
//	properties {
//		property "detekt.sonar.kotlin.config.path", "$project.rootDir/detekt.yml"
//		property "detekt.sonar.kotlin.filters", ".*/test/.*,.*/resources/.*,.*/build/.*,.*/target/.*"
//	}
//}