package beer.supportly.backend.security.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

/**
 * Service for generating and validating JWT tokens.
 */
@Service
class JwtService {

    private val secret =
        "294A404E635266556A586E3272357538782F413F4428472D4B6150645367566B5970337336763979244226452948404D6251655468576D5A7134743777217A25"

    /**
     * Checks if the token is valid for the given user.
     *
     * @param jwt The token to validate.
     * @param userDetails The user to validate the token for.
     *
     * @return True if the token is valid for the given user, false otherwise.
     */
    fun isTokenValid(jwt: String, userDetails: UserDetails): Boolean {
        return (this.extractEmail(jwt) == userDetails.username) && !this.isTokenExpired(jwt)
    }

    /**
     * Checks if the token is expired.
     *
     * @param jwt The token to check.
     *
     * @return True if the token is expired, false otherwise.
     */
    private fun isTokenExpired(jwt: String): Boolean {
        return this.extractExpiration(jwt)?.before(Date(System.currentTimeMillis())) ?: true
    }

    /**
     * Generates a token for the given user.
     *
     * @param customClaims The custom claims to add to the token.
     * @param userDetails The user to generate the token for.
     * @param expiration The expiration date of the token.
     *
     * @return The generated token.
     */
    fun generateToken(customClaims: Map<String, Any>, userDetails: UserDetails, expiration: Date): String {
        return Jwts.builder()
            .setClaims(customClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(expiration)
            .signWith(this.getSigningKey(), SignatureAlgorithm.HS512)
            .compact()
    }

    /**
     * Extracts the email from the given token.
     *
     * @param jwt The token to extract the email from.
     *
     * @return The email from the token.
     */
    fun extractEmail(jwt: String): String? {
        return this.extractClaim(jwt, Claims::getSubject)
    }

    /**
     * Extracts the expiration date from the given token.
     *
     * @param jwt The token to extract the expiration date from.
     *
     * @return The expiration date from the token.
     */
    fun extractExpiration(jwt: String): Date? {
        return this.extractClaim(jwt, Claims::getExpiration)
    }

    /**
     * Extracts a claim from the given token.
     *
     * @param jwt The token to extract the claim from.
     *
     * @return The claim from the token.
     */
    fun <T> extractClaim(jwt: String, claimResolver: (Claims) -> T): T? {
        return claimResolver(this.extractClaims(jwt))
    }

    /**
     * Extracts all claims from the given token.
     *
     * @param jwt The token to extract the claims from.
     *
     * @return The claims from the token.
     */
    private fun extractClaims(jwt: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(this.getSigningKey())
            .build()
            .parseClaimsJws(jwt)
            .body
    }

    /**
     * Gets the signing key.
     *
     * @return The signing key.
     */
    private fun getSigningKey(): Key {
        val keyBuffer = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(keyBuffer)
    }
}