package io.tricefal.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TricefalApplication

fun main(args: Array<String>) {
	runApplication<TricefalApplication>(*args)
}
