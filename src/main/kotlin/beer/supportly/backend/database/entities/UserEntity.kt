package beer.supportly.backend.database.entities

import jakarta.persistence.*

@Entity
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val profilePictureUrl: String,

    @ManyToOne
    val role: RoleEntity
) {
    constructor() : this(null, "", "", "", "", "", RoleEntity())
    constructor(
        email: String,
        firstName: String,
        lastName: String,
        password: String,
        profilePictureUrl: String,
        role: RoleEntity
    ) : this(null, email, firstName, lastName, password, profilePictureUrl, role)
}