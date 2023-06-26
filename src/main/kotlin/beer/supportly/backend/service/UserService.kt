package beer.supportly.backend.service

import beer.supportly.backend.database.entities.UserEntity
import beer.supportly.backend.database.repositories.RoleRepository
import beer.supportly.backend.database.repositories.UserRepository
import beer.supportly.backend.dto.*
import beer.supportly.backend.dto.mapper.UserDtoMapper
import beer.supportly.backend.exception.BackendException
import beer.supportly.backend.mail.MailService
import beer.supportly.backend.security.service.JwtService
import beer.supportly.backend.utils.QrCodeUtils
import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.options.BlobParallelUploadOptions
import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.Duration
import java.util.*
import java.util.stream.Collectors

/**
 * Service for handling user related operations.
 *
 * @property userRepository The repository for user entities.
 * @property roleRepository The repository for role entities.
 * @property userDtoMapper The mapper for user entities.
 * @property passwordEncoder The password encoder.
 * @property jwtService The JWT service.
 * @property mailService The mail service.
 * @property blobServiceClient The blob service client.
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
    private val mailService: MailService,
    private val blobServiceClient: BlobServiceClient
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
        mailService.sendEmailValidation(userEntity)
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
     * Gets the original user entity by its email.
     *
     * @param email The email of the user.
     *
     * @return The user entity.
     */
    fun getOriginalUser(email: String): Optional<UserEntity> {
        return userRepository.findByEmail(email)
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
     * Uploads a profile picture for a user.
     *
     * @param token The token.
     * @param profilePicture The profile picture.
     *
     * @return The DTO containing the operation success.
     *
     * @throws BackendException If the user does not exist.
     * @throws BackendException If the profile picture could not be uploaded.
     */
    fun uploadProfilePicture(token: String, profilePicture: MultipartFile): OperationSuccessDto {
        val userEntity = this.getOriginalUserFromToken(token)
        val options = BlobParallelUploadOptions(profilePicture.inputStream)

        val imageName = "${UUID.randomUUID()}-${userEntity.id}.${
            profilePicture.originalFilename?.split(".")?.last()
        }"

        val response = blobServiceClient.getBlobContainerClient("users")
            .getBlobClient(imageName)
            .uploadWithResponse(options, Duration.ofSeconds(30), null)

        val operationSuccessful = response.value?.let {
            OperationSuccessDto(true, null)
        } ?: throw BackendException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not upload profile picture")

        userEntity.profilePictureUrl = "https://supportly.blob.core.windows.net/users/$imageName"

        return operationSuccessful
    }

    /**
     * Disables two-factor authentication for a user.
     *
     * @param token The token.
     *
     * @return The DTO containing the operation success.
     *
     * @throws BackendException If the user does not exist.
     */
    fun disableTwofa(token: String) {
        val userEntity = this.getOriginalUserFromToken(token)

        userEntity.twofaEnabled = false
        userEntity.twofaCode = "not_set"
    }

    /**
     * Update a user
     *
     * @param token The token.
     * @param updateUserDto The DTO containing the user data.
     *
     * @throws BackendException If the user does not exist.
     */
    fun updateUser(token: String, updateUserDto: UpdateUserDto) {
        val userEntity = this.getOriginalUserFromToken(token)

        if (updateUserDto.firstName != null) userEntity.firstName = updateUserDto.firstName
        if (updateUserDto.lastName != null) userEntity.lastName = updateUserDto.lastName
        if (updateUserDto.password != null) userEntity.userPassword = passwordEncoder.encode(updateUserDto.password)
    }

    /**
     * Update a user's role
     *
     * @param updateRoleDto The DTO containing the user data.
     *
     * @throws BackendException If the user does not exist.
     * @throws BackendException If the role does not exist.
     */
    fun updateRole(updateRoleDto: UpdateRoleDto) {
        val userEntity = this.getOriginalUser(updateRoleDto.userId)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "User not found!") }

        val role = roleRepository.findByName(updateRoleDto.role)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "Role not found!") }

        userEntity.role = role
    }

    /**
     * Gets all users.
     *
     * @param start The start page.
     * @param limit The limit.
     *
     * @return The DTOs containing the user data.
     */
    fun getAllUsers(start: Int, limit: Int): List<UserDto> {
        return userRepository.findAll(PageRequest.of(start, limit)).stream()
            .map(userDtoMapper)
            .collect(Collectors.toList())
    }
}