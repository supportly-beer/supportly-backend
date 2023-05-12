package beer.supportly.backend.security.filter

import beer.supportly.backend.exception.BackendException
import beer.supportly.backend.security.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse)
            return
        }

        val jwt = authHeader.substring("Bearer ".length)

        if (jwt.isEmpty()) {
            throw BackendException(HttpStatus.BAD_REQUEST, "No token provided for auth required route")
        }

        val email = jwtService.extractEmail(jwt)

        if (email != null && SecurityContextHolder.getContext().authentication == null) {
            val userEntity = userDetailsService.loadUserByUsername(email)

            if (jwtService.isTokenValid(jwt, userEntity)) {
                val usernamePasswordAuthenticationToken =
                    UsernamePasswordAuthenticationToken(userEntity, null, userEntity.authorities)

                usernamePasswordAuthenticationToken.details =
                    WebAuthenticationDetailsSource().buildDetails(httpServletRequest)

                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse)
    }
}