import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	id("com.adarshr.test-logger") version ("2.0.0")
}

group = "io.tricefal"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

subprojects {

	apply { plugin("java") }
	apply { plugin("com.adarshr.test-logger") }

	java {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
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

	tasks.withType<Test> {
		useJUnitPlatform()
		testLogging {
			events = mutableSetOf(org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED, org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED, org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED)
			exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
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
			jvmTarget = "11"
		}
	}

	testlogger {
		theme = com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA_PARALLEL
	}
}
