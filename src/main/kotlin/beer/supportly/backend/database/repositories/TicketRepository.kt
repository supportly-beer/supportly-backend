package beer.supportly.backend.database.repositories

import beer.supportly.backend.database.entities.TicketEntity
import beer.supportly.backend.database.entities.TicketState
import beer.supportly.backend.database.entities.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository for the Ticket table
 */
@Repository
interface TicketRepository : JpaRepository<TicketEntity, Long> {

    /**
     * Find a ticket by its identifier
     *
     * @param identifier the identifier of the ticket
     *
     * @return Optional<TicketEntity> contains the ticket with the given identifier
     *
     * @see beer.supportly.backend.database.entities.TicketEntity
     */
    fun findByIdentifier(identifier: String): Optional<TicketEntity>

    /**
     * Find a ticket by its creator and identifier
     *
     * @param creator the creator of the ticket
     * @param identifier the identifier of the ticket
     *
     * @return Optional<TicketEntity> contains the ticket with the given creator and identifier
     *
     * @see beer.supportly.backend.database.entities.TicketEntity
     * @see beer.supportly.backend.database.entities.UserEntity
     */
    fun findByCreatorAndIdentifier(creator: UserEntity, identifier: String): Optional<TicketEntity>

    /**
     * Find all tickets by their creator
     *
     * @param creator the creator of the ticket
     *
     * @return Optional<List<TicketEntity>> contains the tickets with the given creator
     *
     * @see beer.supportly.backend.database.entities.TicketEntity
     */
    fun findAllByCreator(creator: UserEntity): Optional<List<TicketEntity>>

    /**
     * Find all tickets by their creator with pagination
     *
     * @param creator the creator of the ticket
     * @param pageable the pagination information
     *
     * @return Page<TicketEntity> contains the tickets with the given creator
     *
     * @see beer.supportly.backend.database.entities.TicketEntity
     * @see beer.supportly.backend.database.entities.UserEntity
     */
    fun findAllByCreator(creator: UserEntity, pageable: Pageable): Page<TicketEntity>

    /**
     * Find all tickets by their creator between dates
     *
     * @param creator the creator of the ticket
     * @param startDate the start date of the ticket
     * @param endDate the end date of the ticket
     *
     * @return Optional<List<TicketEntity>> contains the tickets with the given creator
     *
     * @see beer.supportly.backend.database.entities.TicketEntity
     * @see beer.supportly.backend.database.entities.UserEntity
     */
    fun findAllByCreatorAndCreatedAtBetween(
        creator: UserEntity, startDate: Long, endDate: Long
    ): Optional<List<TicketEntity>>

    /**
     * Find all tickets by their creator and state between dates
     *
     * @param creator the creator of the ticket
     * @param state the state of the ticket
     * @param startDate the start date of the ticket
     * @param endDate the end date of the ticket
     *
     * @return Optional<List<TicketEntity>> contains the tickets with the given creator and state
     *
     * @see beer.supportly.backend.database.entities.TicketEntity
     * @see beer.supportly.backend.database.entities.UserEntity
     * @see beer.supportly.backend.database.entities.TicketState
     */
    fun findAllByCreatorAndStateAndCreatedAtBetween(
        creator: UserEntity, state: TicketState, startDate: Long, endDate: Long
    ): Optional<List<TicketEntity>>

    /**
     * Find all tickets by their creator and closed at between dates
     *
     * @param creator the creator of the ticket
     * @param startDate the start date of the ticket
     * @param endDate the end date of the ticket
     *
     * @return Optional<List<TicketEntity>> contains the tickets with the given creator
     *
     * @see beer.supportly.backend.database.entities.TicketEntity
     * @see beer.supportly.backend.database.entities.UserEntity
     */
    fun findAllByCreatorAndClosedAtBetween(
        creator: UserEntity, startDate: Long, endDate: Long
    ): Optional<List<TicketEntity>>

    /**
     * Find all tickets by their creator and state
     *
     * @param creator the creator of the ticket
     * @param state the state of the ticket
     *
     * @return Optional<List<TicketEntity>> contains the tickets with the given creator and state
     *
     * @see beer.supportly.backend.database.entities.TicketEntity
     * @see beer.supportly.backend.database.entities.UserEntity
     * @see beer.supportly.backend.database.entities.TicketState
     */
    fun findAllByCreatorAndState(
        creator: UserEntity,
        state: TicketState
    ): Optional<List<TicketEntity>>

    /**
     * Find all tickets by their assignee
     *
     * @param assignee the assignee of the ticket
     * @param pageable the pagination information
     *
     * @return Optional<List<TicketEntity>> contains the tickets with the given assignee
     *
     * @see beer.supportly.backend.database.entities.TicketEntity
     * @see beer.supportly.backend.database.entities.UserEntity
     */
    fun findAllByAssignee(assignee: UserEntity, pageable: Pageable): Page<TicketEntity>

    /**
     * Find all tickets by their assignee and state between dates
     *
     * @param assignee the assignee of the ticket
     * @param state the state of the ticket
     * @param startDate the start date of the ticket
     * @param endDate the end date of the ticket
     *
     * @return Optional<List<TicketEntity>> contains the tickets with the given assignee
     *
     * @see beer.supportly.backend.database.entities.TicketEntity
     * @see beer.supportly.backend.database.entities.UserEntity
     * @see beer.supportly.backend.database.entities.TicketState
     */
    fun findAllByAssigneeAndStateAndCreatedAtBetween(
        assignee: UserEntity, state: TicketState, startDate: Long, endDate: Long
    ): Optional<List<TicketEntity>>

    /**
     * Find all tickets by their assignee and state between dates
     *
     * @param assignee the assignee of the ticket
     * @param state the state of the ticket
     * @param startDate the start date of the ticket
     * @param endDate the end date of the ticket
     *
     * @return Optional<List<TicketEntity>> contains the tickets with the given assignee and state
     *
     * @see beer.supportly.backend.database.entities.TicketEntity
     * @see beer.supportly.backend.database.entities.UserEntity
     * @see beer.supportly.backend.database.entities.TicketState
     */
    fun findAllByAssigneeAndStateAndClosedAtBetween(
        assignee: UserEntity, state: TicketState, startDate: Long, endDate: Long
    ): Optional<List<TicketEntity>>

    /**
     * Find all tickets by their assignee and state
     *
     * @param assignee the assignee of the ticket
     * @param state the state of the ticket
     *
     * @return Optional<List<TicketEntity>> contains the tickets with the given assignee and state
     *
     * @see beer.supportly.backend.database.entities.TicketEntity
     * @see beer.supportly.backend.database.entities.UserEntity
     * @see beer.supportly.backend.database.entities.TicketState
     */
    fun findAllByAssigneeAndState(
        assignee: UserEntity,
        state: TicketState
    ): Optional<List<TicketEntity>>

    /**
     * Find all tickets by their state between dates
     *
     * @param state the state of the ticket
     * @param startDate the start date of the ticket
     * @param endDate the end date of the ticket
     *
     * @return Optional<List<TicketEntity>> contains the tickets with the given state
     *
     * @see beer.supportly.backend.database.entities.TicketEntity
     * @see beer.supportly.backend.database.entities.TicketState
     */
    fun findAllByStateAndCreatedAtBetween(
        state: TicketState, startDate: Long, endDate: Long
    ): Optional<List<TicketEntity>>

    /**
     * Find all tickets by their state
     *
     * @param state the state of the ticket
     *
     * @return Optional<List<TicketEntity>> contains the tickets with the given state
     *
     * @see beer.supportly.backend.database.entities.TicketEntity
     * @see beer.supportly.backend.database.entities.TicketState
     */
    fun findAllByState(state: TicketState): Optional<List<TicketEntity>>
}