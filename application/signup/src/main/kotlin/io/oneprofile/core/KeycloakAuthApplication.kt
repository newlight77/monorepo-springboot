package io.oneprofile.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KeycloakAuthApplication

fun main(args: Array<String>) {
	val context = runApplication<KeycloakAuthApplication>(*args)

	context.beanDefinitionNames
		.map { context.getBean(it).javaClass.toString() }
		.filter { it.startsWith("class io.oneprofile.core") }
		.sorted()
		.map { println(it) }

}