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

@Service
class JwtService {

    private val secret =
        "294A404E635266556A586E3272357538782F413F4428472D4B6150645367566B5970337336763979244226452948404D6251655468576D5A7134743777217A25"

    fun isTokenValid(jwt: String, userDetails: UserDetails): Boolean {
        return (this.extractEmail(jwt) == userDetails.username) && !this.isTokenExpired(jwt)
    }

    private fun isTokenExpired(jwt: String): Boolean {
        return this.extractExpiration(jwt)?.before(Date(System.currentTimeMillis())) ?: true
    }

    fun generateToken(customClaims: Map<String, Any>, userDetails: UserDetails, expiration: Date): String {
        return Jwts.builder()
            .setClaims(customClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(expiration)
            .signWith(this.getSigningKey(), SignatureAlgorithm.HS512)
            .compact()
    }

    fun extractEmail(jwt: String): String? {
        return this.extractClaim(jwt, Claims::getSubject)
    }

    fun extractExpiration(jwt: String): Date? {
        return this.extractClaim(jwt, Claims::getExpiration)
    }

    fun extractId(jwt: String): Int? {
        return this.extractClaims(jwt)["id"] as Int?
    }

    fun <T> extractClaim(jwt: String, claimResolver: (Claims) -> T): T? {
        return claimResolver(this.extractClaims(jwt))
    }

    private fun extractClaims(jwt: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(this.getSigningKey())
            .build()
            .parseClaimsJws(jwt)
            .body
    }

    private fun getSigningKey(): Key {
        val keyBuffer = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(keyBuffer)
    }
}