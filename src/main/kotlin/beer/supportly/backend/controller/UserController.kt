package beer.supportly.backend.controller

import beer.supportly.backend.dto.*
import beer.supportly.backend.service.UserService
import jakarta.annotation.security.RolesAllowed
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

/**
 * Controller for the user endpoints
 *
 * @property userService contains the user service
 *
 * @see beer.supportly.backend.service.UserService
 */
@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {

    /**
     * Endpoint to get the currently authenticated user
     *
     * @param token contains the token
     *
     * @return UserDto contains the data of the user
     *
     * @see beer.supportly.backend.dto.UserDto
     */
    @GetMapping
    fun getUser(@RequestHeader("Authorization") token: String): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.getUserFromToken(token.substring("Bearer ".length)))
    }

    /**
     * Endpoint to get a user
     *
     * @param userId contains the user id
     *
     * @return UserDto contains the data of the user
     *
     * @throws beer.supportly.backend.exception.BackendException if the user does not exist
     *
     * @see beer.supportly.backend.dto.UserDto
     */
    @GetMapping("{userId}")
    @RolesAllowed("ROLE_ADMINISTRATOR")
    fun getUser(@PathVariable("userId") userId: Long): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.getUser(userId))
    }

    /**
     * Endpoint to create a user
     *
     * @param createUserDto contains the data of the user
     *
     * @return OperationSuccessDto contains the operation success
     *
     * @throws beer.supportly.backend.exception.BackendException if the user already exists
     *
     * @see beer.supportly.backend.dto.CreateUserDto
     * @see beer.supportly.backend.dto.OperationSuccessDto
     */
    @PostMapping
    fun createUser(@RequestBody createUserDto: CreateUserDto): ResponseEntity<OperationSuccessDto> {
        userService.createUser(createUserDto)
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }

    /**
     * Endpoint to enable two-factor authentication
     *
     * @param token contains the token
     *
     * @return TwofaEnabledDto contains the qr code to enable two-factor authentication
     *
     * @throws beer.supportly.backend.exception.BackendException if the user is already enabled
     *
     * @see beer.supportly.backend.dto.TwofaEnabledDto
     */
    @PostMapping("/enableTwofa")
    fun enableTwofa(@RequestHeader("Authorization") token: String): ResponseEntity<TwofaEnabledDto> {
        return ResponseEntity.ok(userService.enableTwofa(token.substring("Bearer ".length)))
    }

    /**
     * Endpoint upload a new profile picture
     *
     * @param token contains the token
     * @param profilePicture contains the profile picture
     *
     * @return OperationSuccessDto contains the operation success
     */
    @PostMapping("/upload")
    fun uploadProfilePicture(
        @RequestHeader("Authorization") token: String,
        @RequestParam("profilePicture") profilePicture: MultipartFile
    ): ResponseEntity<OperationSuccessDto> {
        return ResponseEntity.ok(userService.uploadProfilePicture(token.substring("Bearer ".length), profilePicture))
    }

    /**
     * Endpoint to disable two-factor authentication
     *
     * @param token contains the token
     *
     * @return OperationSuccessDto contains the operation success
     *
     * @throws beer.supportly.backend.exception.BackendException if the user is already disabled
     *
     * @see beer.supportly.backend.dto.OperationSuccessDto
     */
    @PostMapping("/disableTwofa")
    fun disableTwofa(@RequestHeader("Authorization") token: String): ResponseEntity<OperationSuccessDto> {
        userService.disableTwofa(token.substring("Bearer ".length))
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }

    /**
     * Endpoint to update a user
     *
     * @param token contains the token
     *
     * @return OperationSuccessDto contains the operation success
     *
     * @throws beer.supportly.backend.exception.BackendException if the user is already disabled
     *
     * @see beer.supportly.backend.dto.OperationSuccessDto
     */
    @PostMapping("/update")
    fun updateUser(
        @RequestHeader("Authorization") token: String,
        @RequestBody updateUserDto: UpdateUserDto
    ): ResponseEntity<OperationSuccessDto> {
        userService.updateUser(token.substring("Bearer ".length), updateUserDto)
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }

    /**
     * Endpoint to update the role of a user
     *
     * @param updateRoleDto contains the data of the user
     *
     * @return OperationSuccessDto contains the operation success
     *
     * @throws beer.supportly.backend.exception.BackendException if the user is not found or the role is not found
     *
     * @see beer.supportly.backend.dto.OperationSuccessDto
     */
    @PostMapping("/updateRole")
    @RolesAllowed("ROLE_ADMINISTRATOR")
    fun updateRole(
        @RequestBody updateRoleDto: UpdateRoleDto
    ): ResponseEntity<OperationSuccessDto> {
        userService.updateRole(updateRoleDto)
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }

    /**
     * Endpoint to get all users
     *
     * @return List<UserDto> contains the data of the users
     *
     * @see beer.supportly.backend.dto.UserDto
     */
    @GetMapping("/all")
    @RolesAllowed("ROLE_ADMINISTRATOR")
    fun getAllUsers(
        @RequestParam(name = "start", required = true) start: Int,
        @RequestParam(name = "limit", required = true) limit: Int
    ): ResponseEntity<List<UserDto>> {
        return ResponseEntity.ok(userService.getAllUsers(start, limit))
    }
}