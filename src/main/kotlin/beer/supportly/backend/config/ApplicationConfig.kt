package beer.supportly.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Configuration class for the application
 *
 * @property passwordEncoder the password encoder
 * @property userDetailsService the user details service
 */
@Configuration
class ApplicationConfig(
    private val passwordEncoder: PasswordEncoder,
    private val userDetailsService: UserDetailsService
) {

    /**
     * Bean for the AuthenticationProvider
     *
     * @return AuthenticationProvider contains the authentication provider logic
     */
    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()

        authenticationProvider.setUserDetailsService(userDetailsService)
        authenticationProvider.setPasswordEncoder(passwordEncoder)

        return authenticationProvider
    }

    /**
     * Bean for the AuthenticationManager
     *
     * @param authenticationConfiguration to pull the manager from
     *
     * @return AuthenticationManager contains the authentication manager logic
     */
    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }
}