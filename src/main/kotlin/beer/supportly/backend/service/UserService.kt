package beer.supportly.backend.service

import beer.supportly.backend.database.entities.UserEntity
import beer.supportly.backend.database.repositories.RoleRepository
import beer.supportly.backend.database.repositories.UserRepository
import beer.supportly.backend.dto.CreateUserDto
import beer.supportly.backend.dto.UserDto
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository
) {
    fun createUser(createUserDto: CreateUserDto): UserDto {
        val foundUser = userRepository.findByEmail(createUserDto.email)

        if (foundUser.isPresent) {
            throw RuntimeException("User already exists!")
        }

        val roleEntity = roleRepository.findByName("ROLE_USER")

        if (!roleEntity.isPresent) {
            throw RuntimeException("Role does not exist!")
        }

        val userEntity = UserEntity(
            createUserDto.email,
            createUserDto.firstName,
            createUserDto.lastName,
            createUserDto.password, // HASH
            createUserDto.profilePictureUrl,
            roleEntity.get()
        )
    }
}