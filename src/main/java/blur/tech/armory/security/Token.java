package blur.tech.armory.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@Service
public class Token {

    private final byte[] key = ("To replace programmers with robots, clients will have to accurately describe" +
            "what they want. We're save").getBytes(StandardCharsets.UTF_8);

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    public Integer getId(String token) {
        return extractAllClaims(token).get("userId", Integer.class);
    }

    public String generateToken(UserDetails userDetails, Integer id) {
        return createToken(userDetails.getUsername(), id);
    }

    private String createToken(String subject, Integer id) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("userId", id)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()));
    }
}