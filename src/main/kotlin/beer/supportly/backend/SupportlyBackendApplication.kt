package beer.supportly.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * The main class of the application.
 */
@SpringBootApplication
class SupportlyBackendApplication

/**
 * The main function of the application.
 *
 * @param args The command line arguments.
 */
fun main(args: Array<String>) {
    runApplication<SupportlyBackendApplication>(*args)
}
