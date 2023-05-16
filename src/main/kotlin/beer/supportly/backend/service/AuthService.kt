package beer.supportly.backend.service

import beer.supportly.backend.database.repositories.UserRepository
import beer.supportly.backend.dto.LoginDto
import beer.supportly.backend.dto.OperationSuccessDto
import beer.supportly.backend.dto.TokenDto
import beer.supportly.backend.dto.TwofaDto
import beer.supportly.backend.exception.BackendException
import beer.supportly.backend.exception.TwofaRequiredException
import beer.supportly.backend.security.service.JwtService
import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {

    fun login(loginDto: LoginDto): TokenDto {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginDto.email, loginDto.password))

        val userEntity = userRepository.findByEmail(loginDto.email)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "User not found!") }

        if (userEntity.twofaEnabled) {
            val twoFaToken = jwtService.generateToken(
                mapOf("id" to userEntity.id!!), userEntity, Date(System.currentTimeMillis() + (1000 * 60 * 1))
            )

            throw TwofaRequiredException("twofa_required", twoFaToken)
        }

        val accessToken = jwtService.generateToken(
            mapOf("id" to userEntity.id!!), userEntity, Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 14))
        )

        return TokenDto(accessToken)
    }

    fun validate(token: String): OperationSuccessDto {
        val email = jwtService.extractEmail(token)
            ?: throw BackendException(HttpStatus.BAD_REQUEST, "Invalid token signature")

        val userEntity = userRepository.findByEmail(email)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "User not found!") }

        return OperationSuccessDto(jwtService.isTokenValid(token, userEntity), null)
    }

    fun twofa(twofaDto: TwofaDto): TokenDto {
        val userEntity = userRepository.findByEmail(twofaDto.email)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "User not found!") }

        val timeStamp = Date(System.currentTimeMillis())
        val verified = GoogleAuthenticator(userEntity.twofaCode.toByteArray()).isValid(twofaDto.token, timeStamp)

        if (!verified) {
            throw BackendException(HttpStatus.UNAUTHORIZED, "Code is wrong.")
        }

        val accessToken = jwtService.generateToken(
            mapOf("id" to userEntity.id!!), userEntity, Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 14))
        )

        return TokenDto(accessToken)
    }
}