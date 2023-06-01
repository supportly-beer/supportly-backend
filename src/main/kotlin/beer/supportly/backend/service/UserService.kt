package beer.supportly.backend.service

import beer.supportly.backend.database.entities.UserEntity
import beer.supportly.backend.database.repositories.RoleRepository
import beer.supportly.backend.database.repositories.UserRepository
import beer.supportly.backend.dto.CreateUserDto
import beer.supportly.backend.dto.ForgotPasswordDto
import beer.supportly.backend.dto.TwofaEnabledDto
import beer.supportly.backend.dto.UserDto
import beer.supportly.backend.dto.mapper.UserDtoMapper
import beer.supportly.backend.exception.BackendException
import beer.supportly.backend.mail.MailService
import beer.supportly.backend.security.service.JwtService
import beer.supportly.backend.utils.QrCodeUtils
import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val userDtoMapper: UserDtoMapper,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val mailService: MailService
) {

    fun enableTwofa(jwt: String): TwofaEnabledDto {
        val userEntity = this.getOriginalUserFromToken(jwt)

        if (userEntity.twofaEnabled) {
            throw BackendException(HttpStatus.BAD_REQUEST, "Twofa already enabled!")
        }

        val secret = String(GoogleAuthenticator.createRandomSecretAsByteArray())

        userEntity.twofaCode = secret
        userEntity.twofaEnabled = true

        val qrCode = QrCodeUtils.generateQrCode(userEntity.email, secret)

        return TwofaEnabledDto(qrCode)
    }

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
            false,
            roleEntity.get()
        )

        this.saveUser(userEntity)
        this.sendEmailValidation(userEntity)
    }

    fun saveUser(userEntity: UserEntity) {
        userRepository.save(userEntity.copy(userPassword = passwordEncoder.encode(userEntity.password)))
    }

    fun getUser(userId: Long): UserDto {
        return this.getOriginalUser(userId)
            .map(userDtoMapper)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "User not found!") }
    }

    fun getOriginalUser(userId: Long): Optional<UserEntity> {
        return userRepository.findById(userId)
    }

    fun getUserFromToken(jwt: String): UserDto {
        return Optional.of(this.getOriginalUserFromToken(jwt)).map(userDtoMapper)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "User not found!") }
    }

    fun getOriginalUserFromToken(jwt: String): UserEntity {
        val email = jwtService.extractEmail(jwt)
            ?: throw BackendException(HttpStatus.BAD_REQUEST, "Invalid token signature")

        return userRepository.findByEmail(email)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "User not found!") }
    }

    fun getUserCount(): Long {
        return userRepository.count()
    }

    fun validateEmail(token: String) {
        TODO("Not yet implemented")
    }

    fun forgotPassword(forgotPasswordDto: ForgotPasswordDto) {
        val userEntity = userRepository.findByEmail(forgotPasswordDto.email)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "User not found!") }

        this.sendForgotPassword(userEntity)
    }

    fun resetPassword(token: String) {
        TODO("Not yet implemented")
    }

    fun sendForgotPassword(userEntity: UserEntity) {
        mailService.sendMail(
            userEntity.email, "Reset your password!", mailService.getForgotPasswordTemplate(
                userEntity.firstName,
                userEntity.lastName,
                jwtService.generateToken(
                    mapOf("type" to "resetPassword"),
                    userEntity,
                    Date(System.currentTimeMillis() + (1000 * 60 * 10))
                )
            )
        )
    }

    fun sendEmailValidation(userEntity: UserEntity) {
        mailService.sendMail(
            userEntity.email, "Welcome to Supportly!", mailService.getValidateEmailTemplate(
                userEntity.firstName,
                userEntity.lastName,
                jwtService.generateToken(
                    mapOf("type" to "validateEmail"),
                    userEntity,
                    Date(System.currentTimeMillis() + (1000 * 60 * 10))
                )
            )
        )
    }
}