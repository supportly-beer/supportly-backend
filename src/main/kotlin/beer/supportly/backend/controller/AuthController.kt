package beer.supportly.backend.controller

import beer.supportly.backend.dto.CreateUserDto
import beer.supportly.backend.dto.LoginDto
import beer.supportly.backend.dto.OperationSuccessDto
import beer.supportly.backend.dto.TokenDto
import beer.supportly.backend.service.AuthService
import beer.supportly.backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody loginDto: LoginDto): ResponseEntity<TokenDto> {
        return ResponseEntity.ok(authService.login(loginDto))
    }

    @PostMapping("/validate")
    fun validate(@RequestHeader("Authorization") token: String): ResponseEntity<OperationSuccessDto> {
        return ResponseEntity.ok(authService.validate(token.substring("Bearer ".length)))
    }

    @PostMapping("/register")
    fun register(@RequestBody createUserDto: CreateUserDto): ResponseEntity<OperationSuccessDto> {
        userService.createUser(createUserDto)
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }
}