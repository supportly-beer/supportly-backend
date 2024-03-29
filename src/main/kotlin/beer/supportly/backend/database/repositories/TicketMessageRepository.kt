package beer.supportly.backend.database.repositories

import beer.supportly.backend.database.entities.TicketMessageEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository for the TicketMessage table
 */
@Repository
interface TicketMessageRepository : JpaRepository<TicketMessageEntity, Long>