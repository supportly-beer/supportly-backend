package beer.supportly.backend.database.entities

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * Entity for the User table
 *
 * @property id the id of the user
 * @property email the email of the user
 * @property firstName the first name of the user
 * @property lastName the last name of the user
 * @property userPassword the password of the user
 * @property profilePictureUrl the profile picture url of the user
 * @property twofaCode the twofa code of the user
 * @property twofaEnabled the twofa enabled of the user
 * @property emailVerified the email verified of the user
 * @property role the role of the user
 *
 * @constructor constructor with all values
 * @constructor constructor with no values where default values are set
 * @constructor constructor with only required values
 *
 * @see beer.supportly.backend.database.entities.RoleEntity
 */
@Entity
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val email: String,
    var firstName: String,
    var lastName: String,
    var userPassword: String,
    var profilePictureUrl: String,

    var twofaCode: String,
    var twofaEnabled: Boolean,
    var emailVerified: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "roles_to_user",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var role: RoleEntity
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