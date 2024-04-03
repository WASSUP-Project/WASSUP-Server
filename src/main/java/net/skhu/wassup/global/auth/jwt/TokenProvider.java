package net.skhu.wassup.global.auth.jwt;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.util.Collections.emptyList;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {

    private final Key key;
    private final long tokenValidityTime;

    public TokenProvider(@Value("${jwt.secret}") String key,
                         @Value("${jwt.token-validity-in-milliseconds}") long tokenValidityTime) {
        this.tokenValidityTime = tokenValidityTime;
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Long id) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + this.tokenValidityTime);

        return Jwts.builder()
                .setSubject(id.toString())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, HS512)
                .compact();
    }

    public String parseToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims;
        try {
            claims = parseClaims(token);
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        String id = claims.getSubject();
        List<SimpleGrantedAuthority> authorities = emptyList();
        UserDetails principal = new User(id, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

}

