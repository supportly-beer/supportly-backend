package beer.supportly.backend.config

import beer.supportly.backend.database.repositories.UserRepository
import beer.supportly.backend.exception.BackendException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Configuration class for the user
 *
 * @property userRepository the user repository
 *
 * @see beer.supportly.backend.database.repositories.UserRepository
 */
@Configuration
class UserConfig(
    private val userRepository: UserRepository
) {

    /**
     * Bean for the PasswordEncoder
     *
     * @return PasswordEncoder contains the bcrypt encoding logic
     *
     * @see org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * Bean for the UserDetailsService
     *
     * @return UserDetailsService contains the user details service implementation
     *
     * @see beer.supportly.backend.database.repositories.UserRepository
     */
    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            userRepository.findByEmail(username)
                .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "User not found!") }
        }
    }
}