import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
//	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("plugin.spring") version "1.3.70"
	kotlin("plugin.jpa") version "1.3.70"
	idea
}

group = "io.tricefal"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
	implementation(project(":core:domain"))

	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.2.5.RELEASE")
	implementation("org.springframework.boot:spring-boot-starter-data-rest:2.2.5.RELEASE")

	implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.70")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.70")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.5")

	testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
	testImplementation("org.mockito:mockito-junit-jupiter:3.3.0")
	testImplementation("org.assertj:assertj-core:3.11.1")

}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
