package beer.supportly.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SupportlyBackendApplication

fun main(args: Array<String>) {
    runApplication<SupportlyBackendApplication>(*args)
}
