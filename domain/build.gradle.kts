import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	kotlin("plugin.spring") version "1.3.70"
}

group = "io.tricefal.domain"
version = "0.0.1-SNAPSHOT"


repositories {
	mavenCentral()
}
