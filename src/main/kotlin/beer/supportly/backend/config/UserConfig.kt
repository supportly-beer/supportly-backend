package beer.supportly.backend.config

import beer.supportly.backend.database.repositories.UserRepository
import beer.supportly.backend.exception.BackendException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class UserConfig(
    private val userRepository: UserRepository
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            userRepository.findByEmail(username)
                .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "User not found!") }
        }
    }
}