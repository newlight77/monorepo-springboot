import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
//import org.jmailen.gradle.kotlinter.tasks.LintTask

plugins {
	kotlin("jvm") version "1.4.10"
	id("com.adarshr.test-logger") version ("2.0.0")
//	id("org.jmailen.kotlinter") version "2.3.2"
//	id("com.diffplug.gradle.spotless") version "3.27.2"
	checkstyle
	jacoco
}

group = "io.tricefal.core"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

subprojects {

	apply { plugin("java") }
	apply { plugin("jacoco") }
	apply { plugin("checkstyle") }
	apply { plugin("com.adarshr.test-logger") }
//	apply { plugin("org.jmailen.kotlinter") }
//	apply { plugin("com.diffplug.gradle.spotless") }


	java {
		sourceCompatibility = JavaVersion.VERSION_13
		targetCompatibility = JavaVersion.VERSION_13
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

		// See https://technology.lastminute.com/junit5-kotlin-and-gradle-dsl/
		// See https://github.com/gradle/kotlin-dsl/issues/836
		addTestListener(object : TestListener {
			val failedTests = mutableListOf<TestDescriptor>()
			val skippedTests = mutableListOf<TestDescriptor>()

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

	tasks.checkstyleMain { group = "verification" }
	tasks.checkstyleTest { group = "verification" }

	tasks.jacocoTestReport {
		reports {
			csv.isEnabled = false
			html.isEnabled = true
			html.destination = file("$buildDir/jacoco/html")
			xml.isEnabled = true
			xml.destination = file("$buildDir/jacoco/jacocoTestReport.xml")
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
					exclude("io/tricefal/core/AuthenticationApplicationKt.class")
				}
		)
	}

	jacoco {
		toolVersion = "0.8.5"
		reportsDir = file("$buildDir/jacoco")
	}

	val testCoverage by tasks.registering {
		group = "verification"
		description = "Runs the unit tests with coverage."

		dependsOn(":test", ":jacocoTestReport", ":jacocoTestCoverageVerification")
		val jacocoTestReport = tasks.findByName("jacocoTestReport")
		jacocoTestReport?.mustRunAfter(tasks.findByName("test"))
		tasks.findByName("jacocoTestCoverageVerification")?.mustRunAfter(jacocoTestReport)
	}

//	spotless {
//		kotlin {
//			ktlint()
//		}
//		kotlinGradle {
//			target(fileTree(projectDir).apply {
//				include("*.gradle.kts")
//			} + fileTree("src").apply {
//				include("**/*.gradle.kts")
//			})
//			ktlint()
//		}
//	}

//	kotlinter {
//		ignoreFailures = false
//		indentSize = 4
//		reporters = arrayOf("checkstyle", "plain")
//		experimentalRules = false
//		disabledRules = emptyArray<String>()
//		fileBatchSize = 30
//	}

}







