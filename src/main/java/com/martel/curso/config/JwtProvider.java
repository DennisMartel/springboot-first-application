package com.martel.curso.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
    private static final String SECRET_KEY = "258d6064b0474adda7d190a107583f0e9a0e94d17c1549dd9dd1d4e7270c7567";
    
    SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    
    public String generateToken(Authentication auth)
    {
        String tkn = Jwts.builder()
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime()+846000000))
            .claim("email", auth.getName())
            .signWith(key).compact();       
        
        return tkn;
    }
    
    public String getEmailFromToken(String tkn)
    {
        tkn = tkn.substring(7);
        
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(tkn).getBody();

        String email = String.valueOf(claims.get("email"));
        
        return email;
    }
}
