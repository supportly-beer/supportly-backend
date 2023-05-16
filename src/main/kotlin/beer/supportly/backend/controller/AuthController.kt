package beer.supportly.backend.controller

import beer.supportly.backend.dto.*
import beer.supportly.backend.service.AuthService
import beer.supportly.backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody loginDto: LoginDto): ResponseEntity<TokenDto> {
        return ResponseEntity.ok(authService.login(loginDto))
    }

    @PostMapping("/twofa")
    fun twofa(@RequestBody twofaDto: TwofaDto): ResponseEntity<TokenDto> {
        return ResponseEntity.ok(authService.twofa(twofaDto))
    }

    @GetMapping("/validate")
    fun validate(@RequestHeader("Authorization") token: String): ResponseEntity<OperationSuccessDto> {
        return ResponseEntity.ok(authService.validate(token.substring("Bearer ".length)))
    }

    @PostMapping("/register")
    fun register(@RequestBody createUserDto: CreateUserDto): ResponseEntity<OperationSuccessDto> {
        userService.createUser(createUserDto)
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }
}