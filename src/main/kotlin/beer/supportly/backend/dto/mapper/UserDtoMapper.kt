package beer.supportly.backend.dto.mapper

import beer.supportly.backend.database.entities.UserEntity
import beer.supportly.backend.dto.RoleDto
import beer.supportly.backend.dto.UserDto
import org.springframework.stereotype.Component
import java.util.function.Function

@Component
class UserDtoMapper : Function<UserEntity, UserDto> {

    override fun apply(userEntity: UserEntity): UserDto {
        return UserDto(
            userEntity.id!!,
            userEntity.email,
            userEntity.firstName,
            userEntity.lastName,
            userEntity.profilePictureUrl,
            userEntity.twofaEnabled,
            RoleDto(
                userEntity.role.id!!,
                userEntity.role.name
            )
        )
    }
}