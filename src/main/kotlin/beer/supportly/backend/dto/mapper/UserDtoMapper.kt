package beer.supportly.backend.dto.mapper

import beer.supportly.backend.database.entities.UserEntity
import beer.supportly.backend.dto.RoleDto
import beer.supportly.backend.dto.UserDto
import org.springframework.stereotype.Component
import java.util.function.Function

/**
 * Maps a UserEntity to a UserDto.
 *
 * @see beer.supportly.backend.database.entities.UserEntity
 * @see beer.supportly.backend.dto.UserDto
 */
@Component
class UserDtoMapper : Function<UserEntity, UserDto> {

    /**
     * Maps a UserEntity to a UserDto.
     *
     * @param userEntity the UserEntity to map
     *
     * @return UserDto mapped from UserEntity
     *
     * @see beer.supportly.backend.database.entities.UserEntity
     * @see beer.supportly.backend.dto.UserDto
     */
    override fun apply(userEntity: UserEntity): UserDto {
        return UserDto(
            userEntity.id!!,
            userEntity.email,
            userEntity.firstName,
            userEntity.lastName,
            userEntity.profilePictureUrl,
            userEntity.twofaEnabled,
            userEntity.emailVerified,
            RoleDto(
                userEntity.role.id!!,
                userEntity.role.name
            )
        )
    }
}