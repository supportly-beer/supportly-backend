package beer.supportly.backend.controller

import beer.supportly.backend.dto.CreateUserDto
import beer.supportly.backend.dto.UserDto
import beer.supportly.backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun createUser(@RequestBody createUserDto: CreateUserDto): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.createUser(createUserDto))
    }
}