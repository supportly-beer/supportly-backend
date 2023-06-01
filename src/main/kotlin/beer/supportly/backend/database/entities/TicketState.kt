package beer.supportly.backend.database.entities

/**
 * Enum class for the state of a ticket
 */
enum class TicketState {
    OPEN, ASSIGNED, ON_HOLD, FINISHED, TERMINATED
}