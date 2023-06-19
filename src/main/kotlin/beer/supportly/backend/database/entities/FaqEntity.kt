package beer.supportly.backend.database.entities

import jakarta.persistence.*

/**
 * Entity for the Faq table
 *
 * @property id the id of the faq
 * @property title the title of the faq
 * @property text the text of the faq
 * @property createdAt the creation date of the faq
 * @property creator the creator of the faq
 *
 * @constructor constructor with all values
 * @constructor constructor with no values where default values are set
 * @constructor constructor with only required values
 *
 * @see beer.supportly.backend.database.entities.UserEntity
 */
@Entity
data class FaqEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val title: String,
    val text: String,

    val createdAt: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "faq_to_creator",
        joinColumns = [JoinColumn(name = "faq_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val creator: UserEntity
) {
    constructor() : this(null, "", "", -1, UserEntity())
    constructor(
        title: String,
        text: String,
        createdAt: Long,
        creator: UserEntity
    ) : this(null, title, text, createdAt, creator)
}