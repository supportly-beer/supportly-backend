package beer.supportly.backend.database.repositories

import beer.supportly.backend.database.entities.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository for the Role table
 */
@Repository
interface RoleRepository : JpaRepository<RoleEntity, Long> {

    /**
     * Find a role by its name
     *
     * @param name the name of the role
     *
     * @return Optional<RoleEntity> contains the role with the given name
     *
     * @see beer.supportly.backend.database.entities.RoleEntity
     */
    fun findByName(name: String): Optional<RoleEntity>
}