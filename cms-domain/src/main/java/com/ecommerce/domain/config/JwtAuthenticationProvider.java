package com.ecommerce.domain.config;

import com.ecommerce.domain.common.UserType;
import com.ecommerce.domain.common.UserVo;
import com.ecommerce.domain.util.Aes256Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtAuthenticationProvider {

    private String secretKey = "secretKey";

    private long tokenValidTime = 1000L * 60 * 60 * 24;

    /**
     * 토큰 생성하기
     * @param userPk - 입력받은 EMAIL
     * @param id - 사용자 TABLE 부여받은 PK번호
     * @param userType - CUSTOMER 혹은 SELLER
     * @return 생성된 TOKEN
     */
    public String createToken(String userPk, Long id, UserType userType){
        Claims claims = Jwts.claims().setSubject(Aes256Util.encrypt(userPk))
                .setId(Aes256Util.encrypt(id.toString()));
        claims.put("roles",userType);

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+tokenValidTime))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();

    }

    /**
     * 유효한 시간 내에 토큰인지 확인
     * @param jwtToken - String
     * @return boolean
     */
    public boolean checkValidToken(String jwtToken){
        try{
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claimsJws.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 발행된 토큰으로 유저 정보 가져오기
     * @param token - String
     * @return - UserVo
     */
    public UserVo getUserVo(String token){
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return new UserVo(
                    Long.valueOf(Aes256Util.decrypt(claims.getId())), //USER'S ID
                    Aes256Util.decrypt(claims.getSubject()) //USER'S EMAIL
                    );
    }
}
