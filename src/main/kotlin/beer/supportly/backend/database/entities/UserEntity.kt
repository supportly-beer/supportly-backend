package beer.supportly.backend.database.entities

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
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

    var twofaCode: String,
    var twofaEnabled: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "roles_to_user",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    val role: RoleEntity
) : UserDetails {
    constructor() : this(null, "", "", "", "", "", "", false, RoleEntity())
    constructor(
        email: String,
        firstName: String,
        lastName: String,
        userPassword: String,
        profilePictureUrl: String,
        twofaCode: String,
        twofaEnabled: Boolean,
        role: RoleEntity
    ) : this(null, email, firstName, lastName, userPassword, profilePictureUrl, twofaCode, twofaEnabled, role)

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(this.role.name))
    }

    override fun getPassword(): String = this.userPassword
    override fun getUsername(): String = this.email
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}