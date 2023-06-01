package beer.supportly.backend.database.repositories

import beer.supportly.backend.database.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository for the User table
 */
@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {

    /**
     * Find a user by its email
     *
     * @param email the email of the user
     *
     * @return Optional<UserEntity> contains the user with the given email
     *
     * @see beer.supportly.backend.database.entities.UserEntity
     */
    fun findByEmail(email: String): Optional<UserEntity>
}