package com.example.spectrplus.security

import com.example.spectrplus.entity.profile.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*


@Service
class JwtService {

    private val secret = "super_secret_key_123_super_secret_key_123"

    fun generateToken(user: User): String {

        return Jwts.builder()
            .setSubject(user.email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
            .signWith(Keys.hmacShaKeyFor(secret.toByteArray()))
            .compact()
    }

    fun extractEmail(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(secret.toByteArray())
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

}