package com.example.spectrplus

import com.example.spectrplus.config.StorageProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties::class)
class SpectrPlusApplication

fun main(args: Array<String>) {
    runApplication<SpectrPlusApplication>(*args)
}


@RestController
class MessageController {
    @GetMapping("/")
    fun index(@RequestParam("name") name: String) = "Hello, $name!"
}