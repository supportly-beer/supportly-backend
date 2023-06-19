package beer.supportly.backend.database.repositories

import beer.supportly.backend.database.entities.FaqEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository for the Faq table
 */
@Repository
interface FaqRepository : JpaRepository<FaqEntity, Long>