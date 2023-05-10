package beer.supportly.backend.database.repositories

import beer.supportly.backend.database.entities.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<RoleEntity, Long>