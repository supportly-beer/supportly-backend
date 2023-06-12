package beer.supportly.backend.database.entities

import jakarta.persistence.*

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