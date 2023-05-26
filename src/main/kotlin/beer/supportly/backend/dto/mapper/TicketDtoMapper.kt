package beer.supportly.backend.dto.mapper

import beer.supportly.backend.database.entities.TicketEntity
import beer.supportly.backend.dto.TicketDto
import org.springframework.stereotype.Component
import java.util.function.Function

@Component
class TicketDtoMapper(
    private val userDtoMapper: UserDtoMapper
) : Function<TicketEntity, TicketDto> {

    override fun apply(ticketEntity: TicketEntity): TicketDto {
        val assignee = if (ticketEntity.assignee == null) null
        else userDtoMapper.apply(ticketEntity.assignee!!)

        return TicketDto(
            ticketEntity.identifier,
            ticketEntity.title,
            ticketEntity.description,
            ticketEntity.createdAt,
            ticketEntity.closedAt,
            ticketEntity.updatedAt,
            userDtoMapper.apply(ticketEntity.creator),
            assignee,
            ticketEntity.state,
            ticketEntity.urgency
        )
    }
}