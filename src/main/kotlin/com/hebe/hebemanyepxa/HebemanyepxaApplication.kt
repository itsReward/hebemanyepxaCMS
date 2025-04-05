package com.hebe.hebemanyepxa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan

@SpringBootApplication
@ConfigurationPropertiesScan
class HebemanyepxaApplication

fun main(args: Array<String>) {
    runApplication<HebemanyepxaApplication>(*args)
}