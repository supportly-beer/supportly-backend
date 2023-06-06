package beer.supportly.backend.security

import beer.supportly.backend.security.filter.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

/**
 * Security configuration.
 *
 * @property jwtAuthenticationFilter The JWT authentication filter.
 * @property authenticationProvider The authentication provider.
 *
 * @see beer.supportly.backend.security.filter.JwtAuthenticationFilter
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationProvider: AuthenticationProvider
) {

    /**
     * Configures the security filter chain.
     *
     * @param httpSecurity The HTTP security.
     *
     * @return The security filter chain.
     */
    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity.csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/auth/login", "/auth/register", "/auth/forgot-password", "/auth/validate-email")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(
                CorsFilter(corsConfigurationSource("http://localhost:4200")),
                AbstractPreAuthenticatedProcessingFilter::class.java
            )

        return httpSecurity.build()
    }

    /**
     * Configures the CORS configuration source.
     *
     * @param corsOrigin The CORS origin.
     *
     * @return The CORS configuration source.
     */
    private fun corsConfigurationSource(corsOrigin: String): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration()

        corsConfiguration.allowedOrigins = listOf(corsOrigin)
        corsConfiguration.allowedMethods = listOf("GET", "POST", "HEAD", "OPTIONS", "PUT", "PATCH", "DELETE")
        corsConfiguration.maxAge = 10L
        corsConfiguration.allowCredentials = true
        corsConfiguration.allowedHeaders = listOf(
            "Accept",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers",
            "Accept-Language",
            "Authorization",
            "Content-Type",
            "Request-Name",
            "Request-Surname",
            "Origin",
            "X-Request-AppVersion",
            "X-Request-OsVersion",
            "X-Request-Device",
            "X-Requested-With"
        )

        val urlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration)

        return urlBasedCorsConfigurationSource
    }
}