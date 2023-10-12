import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	kotlin("plugin.spring") version "1.9.10"
}

group = "io.oneprofile.signup.infrastructure"
version = "0.0.1-SNAPSHOT"


repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://kotlin.bintray.com/kotlinx") }
}

dependencies {
	implementation(project(":domain:metafile"))
	implementation(project(":domain:signup"))

	implementation("org.springframework.boot:spring-boot-starter:2.6.5")

	implementation("org.keycloak:keycloak-admin-client:17.0.1")

	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")
	implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
	implementation("org.mindrot:jbcrypt:0.4")
    implementation(kotlin("stdlib-jdk8"))

}

tasks.withType<Jar>() {
	archiveBaseName.set("infra-keycloak-client")
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}