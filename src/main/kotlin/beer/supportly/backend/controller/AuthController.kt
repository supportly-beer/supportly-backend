package beer.supportly.backend.controller

import beer.supportly.backend.dto.*
import beer.supportly.backend.service.AuthService
import beer.supportly.backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controller for the authentication.
 *
 * @property userService the service for the user
 * @property authService the service for the authentication
 *
 * @see beer.supportly.backend.service.UserService
 * @see beer.supportly.backend.service.AuthService
 */
@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
    private val authService: AuthService
) {

    /**
     * Endpoint for the login of a user.
     *
     * @param loginDto contains the email and password
     *
     * @return TokenDto contains the message and the token
     *
     * @see beer.supportly.backend.dto.LoginDto
     * @see beer.supportly.backend.dto.TokenDto
     */
    @PostMapping("/login")
    fun login(@RequestBody loginDto: LoginDto): ResponseEntity<TokenDto> {
        return ResponseEntity.ok(authService.login(loginDto))
    }

    /**
     * Endpoint for the two-factor authentication of a user.
     *
     * @param twofaDto contains the email and the two-factor authentication code
     *
     * @return TokenDto contains the message and the token
     *
     * @see beer.supportly.backend.dto.TwofaDto
     * @see beer.supportly.backend.dto.TokenDto
     */
    @PostMapping("/twofa")
    fun twofa(@RequestBody twofaDto: TwofaDto): ResponseEntity<TokenDto> {
        return ResponseEntity.ok(authService.twofa(twofaDto))
    }

    /**
     * Endpoint for the validation of a token.
     *
     * @param token contains the token
     *
     * @return OperationSuccessDto contains the message and the success status
     *
     * @see beer.supportly.backend.dto.OperationSuccessDto
     */
    @GetMapping("/validate")
    fun validate(@RequestHeader("Authorization") token: String): ResponseEntity<OperationSuccessDto> {
        return ResponseEntity.ok(authService.validate(token.substring("Bearer ".length)))
    }

    /**
     * Endpoint for the registration of a user.
     *
     * @param createUserDto contains the data to create a new user
     *
     * @return OperationSuccessDto contains the message and the success status
     *
     * @see beer.supportly.backend.dto.CreateUserDto
     * @see beer.supportly.backend.dto.OperationSuccessDto
     */
    @PostMapping("/register")
    fun register(@RequestBody createUserDto: CreateUserDto): ResponseEntity<OperationSuccessDto> {
        userService.createUser(createUserDto)
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }

    /**
     * Endpoint for the validation of an email.
     *
     * @param token contains the token
     *
     * @return OperationSuccessDto contains the message and the success status
     *
     * @see beer.supportly.backend.dto.OperationSuccessDto
     */
    @PostMapping("/validate-email")
    fun validateEmail(
        @RequestParam("token") token: String,
        @RequestParam("email") email: String
    ): ResponseEntity<OperationSuccessDto> {
        authService.validateEmail(token, email)
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }

    /**
     * Endpoint for the forgot password of a user.
     *
     * @param forgotPasswordDto contains the email
     *
     * @return OperationSuccessDto contains the message and the success status
     *
     * @see beer.supportly.backend.dto.ForgotPasswordDto
     * @see beer.supportly.backend.dto.OperationSuccessDto
     */
    @PostMapping("/forgot-password")
    fun forgotPassword(@RequestBody forgotPasswordDto: ForgotPasswordDto): ResponseEntity<OperationSuccessDto> {
        authService.forgotPassword(forgotPasswordDto)
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }

    /**
     * Endpoint for the reset password of a user.
     *
     * @param token contains the token
     *
     * @return OperationSuccessDto contains the message and the success status
     *
     * @see beer.supportly.backend.dto.OperationSuccessDto
     */
    @PostMapping("/reset-password")
    fun resetPassword(
        @RequestHeader("Authorization") token: String,
    ): ResponseEntity<OperationSuccessDto> {
        authService.resetPassword(token)
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }
}