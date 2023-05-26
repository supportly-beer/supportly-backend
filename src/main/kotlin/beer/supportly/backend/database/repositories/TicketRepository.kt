package beer.supportly.backend.database.repositories

import beer.supportly.backend.database.entities.TicketEntity
import beer.supportly.backend.database.entities.TicketState
import beer.supportly.backend.database.entities.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TicketRepository : JpaRepository<TicketEntity, Long> {
    fun findByIdentifier(identifier: String): Optional<TicketEntity>
    fun findByCreatorAndIdentifier(creator: UserEntity, identifier: String): Optional<TicketEntity>
    fun findAllByCreator(creator: UserEntity): Optional<List<TicketEntity>>
    fun findAllByCreator(creator: UserEntity, pageable: Pageable): Page<TicketEntity>
    fun findAllByCreatorAndCreatedAtBetween(
        creator: UserEntity, startDate: Long, endDate: Long
    ): Optional<List<TicketEntity>>

    fun findAllByCreatorAndStateAndCreatedAtBetween(
        creator: UserEntity, state: TicketState, startDate: Long, endDate: Long
    ): Optional<List<TicketEntity>>

    fun findAllByCreatorAndClosedAtBetween(
        creator: UserEntity, startDate: Long, endDate: Long
    ): Optional<List<TicketEntity>>

    fun findAllByCreatorAndStateAndClosedAtBetween(
        creator: UserEntity, state: TicketState, startDate: Long, endDate: Long
    ): Optional<List<TicketEntity>>

    fun findAllByCreatorAndCreatedAtBetweenAndClosedAtBetween(
        creator: UserEntity,
        createdAtStartDate: Long, createdAtEndDate: Long,
        closedAtStartDate: Long, closedAtEndDate: Long
    ): Optional<List<TicketEntity>>

    fun findAllByCreatorAndState(
        creator: UserEntity,
        state: TicketState
    ): Optional<List<TicketEntity>>

    fun findAllByAssignee(assignee: UserEntity, pageable: Pageable): Page<TicketEntity>
    fun findAllByAssigneeAndCreatedAtBetween(
        assignee: UserEntity,
        startDate: Long,
        endDate: Long
    ): Optional<List<TicketEntity>>

    fun findAllByAssigneeAndStateAndCreatedAtBetween(
        assignee: UserEntity, state: TicketState, startDate: Long, endDate: Long
    ): Optional<List<TicketEntity>>

    fun findAllByAssigneeAndClosedAtBetween(
        assignee: UserEntity,
        startDate: Long,
        endDate: Long
    ): Optional<List<TicketEntity>>

    fun findAllByAssigneeAndStateAndClosedAtBetween(
        assignee: UserEntity, state: TicketState, startDate: Long, endDate: Long
    ): Optional<List<TicketEntity>>

    fun findAllByAssigneeAndCreatedAtBetweenAndClosedAtBetween(
        assignee: UserEntity,
        createdAtStartDate: Long, createdAtEndDate: Long,
        closedAtStartDate: Long, closedAtEndDate: Long
    ): Optional<List<TicketEntity>>

    fun findAllByAssigneeAndStateAndCreatedAtBetweenAndClosedAtBetween(
        assignee: UserEntity,
        state: TicketState,
        createdAtStartDate: Long, createdAtEndDate: Long,
        closedAtStartDate: Long, closedAtEndDate: Long
    ): Optional<List<TicketEntity>>

    fun findAllByAssigneeAndState(
        assignee: UserEntity,
        state: TicketState
    ): Optional<List<TicketEntity>>

    fun findAllByCreatedAtBetween(startDate: Long, endDate: Long): Optional<List<TicketEntity>>

    fun findAllByStateAndCreatedAtBetween(
        state: TicketState, startDate: Long, endDate: Long
    ): Optional<List<TicketEntity>>

    fun findAllByClosedAtBetween(startDate: Long, endDate: Long): Optional<List<TicketEntity>>
    fun findAllByStateAndClosedAtBetween(
        state: TicketState, startDate: Long, endDate: Long
    ): Optional<List<TicketEntity>>

    fun findAllByCreatedAtBetweenAndClosedAtBetween(
        createdAtStartDate: Long, createdAtEndDate: Long,
        closedAtStartDate: Long, closedAtEndDate: Long
    ): Optional<List<TicketEntity>>

    fun findAllByState(state: TicketState): Optional<List<TicketEntity>>
    fun findAllByStateAndCreatedAtBetweenAndClosedAtBetween(
        state: TicketState,
        createdAtStartDate: Long, createdAtEndDate: Long,
        closedAtStartDate: Long, closedAtEndDate: Long
    )
}