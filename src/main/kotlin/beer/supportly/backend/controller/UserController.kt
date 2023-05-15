package beer.supportly.backend.controller

import beer.supportly.backend.dto.CreateUserDto
import beer.supportly.backend.dto.OperationSuccessDto
import beer.supportly.backend.dto.UserDto
import beer.supportly.backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun getUser(@RequestHeader("Authorization") token: String): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.getUserFromToken(token.substring("Bearer ".length)))
    }

    @GetMapping("{userId}")
    fun getUser(@PathVariable("userId") userId: Long): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.getUser(userId))
    }

    @PostMapping
    fun createUser(@RequestBody createUserDto: CreateUserDto): ResponseEntity<OperationSuccessDto> {
        userService.createUser(createUserDto)
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }
}