package beer.supportly.backend.service

import beer.supportly.backend.database.entities.UserEntity
import beer.supportly.backend.database.repositories.RoleRepository
import beer.supportly.backend.database.repositories.UserRepository
import beer.supportly.backend.dto.CreateUserDto
import beer.supportly.backend.dto.UserDto
import beer.supportly.backend.dto.mapper.UserDtoMapper
import beer.supportly.backend.exception.BackendException
import beer.supportly.backend.security.service.JwtService
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val userDtoMapper: UserDtoMapper,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    fun createUser(createUserDto: CreateUserDto) {
        val foundUser = userRepository.findByEmail(createUserDto.email)

        if (foundUser.isPresent) {
            throw BackendException(HttpStatus.BAD_REQUEST, "User already exists!")
        }

        val roleEntity = roleRepository.findByName("ROLE_USER")

        if (!roleEntity.isPresent) {
            throw BackendException(HttpStatus.NOT_FOUND, "Role does not exist!")
        }

        val userEntity = UserEntity(
            createUserDto.email,
            createUserDto.firstName,
            createUserDto.lastName,
            createUserDto.password,
            createUserDto.profilePictureUrl,
            "not_set",
            false,
            roleEntity.get()
        )

        this.saveUser(userEntity)
    }

    fun saveUser(userEntity: UserEntity) {
        userRepository.save(userEntity.copy(userPassword = passwordEncoder.encode(userEntity.password)))
    }

    fun getUser(userId: Long): UserDto {
        return userRepository.findById(userId)
            .map(userDtoMapper)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "User not found!") }
    }

    fun getUserFromToken(jwt: String): UserDto {
        val email = jwtService.extractEmail(jwt)
            ?: throw BackendException(HttpStatus.BAD_REQUEST, "Invalid token signature")

        return userRepository.findByEmail(email)
            .map(userDtoMapper)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "User not found!") }
    }

    fun getUserCount(): Long {
        return userRepository.count()
    }
}