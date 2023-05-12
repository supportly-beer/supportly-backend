package beer.supportly.backend.database.entities

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val email: String,
    val firstName: String,
    val lastName: String,
    val userPassword: String,
    val profilePictureUrl: String,

    @ManyToOne
    @JoinTable(
        name = "roles_to_user",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    val role: RoleEntity
) : UserDetails {
    constructor() : this(null, "", "", "", "", "", RoleEntity())
    constructor(
        email: String,
        firstName: String,
        lastName: String,
        userPassword: String,
        profilePictureUrl: String,
        role: RoleEntity
    ) : this(null, email, firstName, lastName, userPassword, profilePictureUrl, role)

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()
    override fun getPassword(): String = this.userPassword
    override fun getUsername(): String = this.email
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}