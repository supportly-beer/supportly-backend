package beer.supportly.backend.database.repositories

import beer.supportly.backend.database.entities.TicketEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : JpaRepository<TicketEntity, Long>