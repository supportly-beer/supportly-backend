package beer.supportly.backend.database.entities

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * Entity for the User table
 *
 * @param id the id of the user
 * @param email the email of the user
 * @param firstName the first name of the user
 * @param lastName the last name of the user
 * @param userPassword the password of the user
 * @param profilePictureUrl the profile picture url of the user
 * @param twofaCode the twofa code of the user
 * @param twofaEnabled the twofa enabled of the user
 * @param emailVerified the email verified of the user
 * @param role the role of the user
 *
 * @constructor constructor with all values
 * @constructor constructor with no values where default values are set
 * @constructor constructor with only required values
 *
 * @see RoleEntity
 */
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
    var emailVerified: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "roles_to_user",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    val role: RoleEntity
) : UserDetails {
    constructor() : this(null, "", "", "", "", "", "", false, false, RoleEntity())
    constructor(
        email: String,
        firstName: String,
        lastName: String,
        userPassword: String,
        profilePictureUrl: String,
        twofaCode: String,
        twofaEnabled: Boolean,
        emailVerified: Boolean,
        role: RoleEntity
    ) : this(
        null,
        email,
        firstName,
        lastName,
        userPassword,
        profilePictureUrl,
        twofaCode,
        twofaEnabled,
        emailVerified,
        role
    )

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