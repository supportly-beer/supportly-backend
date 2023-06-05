package beer.supportly.backend.database.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

/**
 * Entity for the Role table
 *
 * @property id the id of the role
 * @property name the name of the role
 *
 * @constructor constructor with all values
 * @constructor constructor with no values where default values are set
 * @constructor constructor with only required values
 */
@Entity
data class RoleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String
) {
    constructor() : this(null, "")
    constructor(name: String) : this(null, name)
}
