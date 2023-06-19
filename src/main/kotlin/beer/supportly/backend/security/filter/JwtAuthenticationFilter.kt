package beer.supportly.backend.security.filter

import beer.supportly.backend.dto.OperationSuccessDto
import beer.supportly.backend.exception.BackendException
import beer.supportly.backend.security.service.JwtService
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
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

/**
 * This filter is used to authenticate the user based on the JWT token provided in the request header.
 * If the token is valid, the user is authenticated and the request is allowed to proceed.
 * If the token is invalid, the request is rejected with an error.
 *
 * @property jwtService The service used to validate the JWT token.
 * @property userDetailsService The service used to load the user details from the database.
 *
 * @see beer.supportly.backend.security.service.JwtService
 */
@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    /**
     * This method is called by the filter chain when a request is received.
     * It extracts the JWT token from the request header and validates it.
     *
     * If the token is valid, the user is authenticated and the request is allowed to proceed.
     * If the token is invalid, the request is rejected with an error.
     *
     * @param httpServletRequest The request received.
     * @param httpServletResponse The response to be sent.
     * @param filterChain The filter chain.
     */
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

        try {
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
        } catch (e: MalformedJwtException) {
            httpServletResponse.status = HttpStatus.UNAUTHORIZED.value()
            httpServletResponse.writer.write(convertObjectToJson(OperationSuccessDto(false, "Invalid token")))
        } catch (e: ExpiredJwtException) {
            httpServletResponse.status = HttpStatus.UNAUTHORIZED.value()
            httpServletResponse.writer.write(convertObjectToJson(OperationSuccessDto(false, "Token expired")))
        } catch (e: Exception) {
            httpServletResponse.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
            httpServletResponse.writer.write(convertObjectToJson(OperationSuccessDto(false, "General exception")))
        }
    }

    /**
     * This method converts an object to a JSON string.
     *
     * @param value The object to be converted.
     * @return The JSON string.
     */
    fun convertObjectToJson(value: Any): String {
        return ObjectMapper().writeValueAsString(value)
    }
}