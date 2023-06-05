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

/**
 * Service for handling user related operations.
 *
 * @property userRepository The repository for user entities.
 * @property roleRepository The repository for role entities.
 * @property userDtoMapper The mapper for user entities.
 * @property passwordEncoder The password encoder.
 * @property jwtService The JWT service.
 * @property mailService The mail service.
 *
 * @see beer.supportly.backend.database.repositories.UserRepository
 * @see beer.supportly.backend.database.repositories.RoleRepository
 * @see beer.supportly.backend.dto.mapper.UserDtoMapper
 * @see beer.supportly.backend.security.service.JwtService
 * @see beer.supportly.backend.mail.MailService
 */
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

    /**
     * Enables twofa for the user.
     *
     * @param jwt The JWT token.
     *
     * @return The DTO containing the QR code.
     */
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

    /**
     * Creates a new user.
     *
     * @param createUserDto The DTO containing the user data.
     *
     * @throws BackendException If the user already exists.
     * @throws BackendException If the role does not exist.
     */
    fun createUser(createUserDto: CreateUserDto) {
        val foundUser = userRepository.findByEmail(createUserDto.email)

        if (foundUser.isPresent) {
            throw BackendException(HttpStatus.BAD_REQUEST, "User already exists!")
        }

        val roleEntity = roleRepository.findByName("ROLE_USER").orElseThrow {
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
            roleEntity
        )

        this.saveUser(userEntity)
        this.sendEmailValidation(userEntity)
    }

    /**
     * Saves the user and encodes the password.
     *
     * @param userEntity The user entity.
     */
    fun saveUser(userEntity: UserEntity) {
        userRepository.save(userEntity.copy(userPassword = passwordEncoder.encode(userEntity.password)))
    }

    /**
     * Gets a user by its ID.
     *
     * @param userId The ID of the user.
     *
     * @return The DTO containing the user data.
     *
     * @throws BackendException If the user does not exist.
     */
    fun getUser(userId: Long): UserDto {
        return this.getOriginalUser(userId)
            .map(userDtoMapper)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "User not found!") }
    }

    /**
     * Gets the original user entity by its id.
     *
     * @param userId The ID of the user.
     *
     * @return The user entity.
     */
    fun getOriginalUser(userId: Long): Optional<UserEntity> {
        return userRepository.findById(userId)
    }

    /**
     * Gets the currently logged-in user by its token.
     *
     * @param jwt The JWT token.
     *
     * @return The DTO containing the user data.
     *
     * @throws BackendException If the user does not exist.
     */
    fun getUserFromToken(jwt: String): UserDto {
        return Optional.of(this.getOriginalUserFromToken(jwt)).map(userDtoMapper)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "User not found!") }
    }

    /**
     * Gets the original user entity by its token.
     *
     * @param jwt The JWT token.
     *
     * @return The user entity.
     *
     * @throws BackendException If the user does not exist.
     * @throws BackendException If the token signature is invalid.
     */
    fun getOriginalUserFromToken(jwt: String): UserEntity {
        val email = jwtService.extractEmail(jwt)
            ?: throw BackendException(HttpStatus.BAD_REQUEST, "Invalid token signature")

        return userRepository.findByEmail(email)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "User not found!") }
    }

    /**
     * Gets all users.
     *
     * @return The DTOs containing the user data.
     */
    fun getUserCount(): Long {
        return userRepository.count()
    }

    /**
     * Validates the email for a user.
     *
     * @param token The token.
     */
    fun validateEmail(token: String) {
        TODO("Not yet implemented")
    }

    /**
     * Sends the forgot password email for a user.
     *
     * @param forgotPasswordDto The DTO containing the email.
     *
     * @throws BackendException If the user does not exist.
     */
    fun forgotPassword(forgotPasswordDto: ForgotPasswordDto) {
        val userEntity = userRepository.findByEmail(forgotPasswordDto.email)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "User not found!") }

        this.sendForgotPassword(userEntity)
    }

    /**
     * Resets the password for a user.
     *
     * @param token The token.
     */
    fun resetPassword(token: String) {
        TODO("Not yet implemented")
    }

    /**
     * Sends the forgot password email for a user.
     *
     * @param userEntity The user entity.
     */
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

    /**
     * Sends the email validation email for a user.
     *
     * @param userEntity The user entity.
     */
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