package com.danielopara.Social_Media_API.jwt;

import com.danielopara.Social_Media_API.Repository.UserRepository;
import com.danielopara.Social_Media_API.models.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${SECRET_KEY}")
    private String secretKey;

    private final UserRepository userRepository;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Key getSigningKey(){
        if(secretKey == null || secretKey.isEmpty()){
            throw new RuntimeException("secret key is not set");
        }
        byte[] key = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }

    private Claims extractAllClaims(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            throw new RuntimeException("invalid token");
        }
    }

    private <T>T extractClaims(String token, Function<Claims, T> claimsFunction){
        Claims claims = extractAllClaims(token);
        return claimsFunction.apply(claims);
    }

    private String generateToken(Map<String, Object> getDetails, String username){
        return Jwts.builder()
                .setClaims(getDetails)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (long) 86400000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Date extractExpirationDate(String token){
        return extractClaims(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return !extractExpirationDate(token).before(new Date());
    }

    public String extractUsername(String token){
        return  extractClaims(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && isTokenExpired(token);
    }

    public String generateAccessToken(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<UserModel> user = userRepository.findByEmail(userDetails.getUsername());

        if(user.isEmpty()){
            throw new RuntimeException("user not found");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", user.get().getFirstName());
        claims.put("lastName", user.get().getLastName());

        return generateToken(claims, userDetails.getUsername());
    }

    public String generateAccessTokenByUsername(String username){
        Optional<UserModel> user = userRepository.findByEmail(username);

        if(user.isEmpty()){
            throw new RuntimeException("user not found");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", user.get().getFirstName());
        claims.put("lastName", user.get().getLastName());

        return generateToken(claims, username);
    }
}
