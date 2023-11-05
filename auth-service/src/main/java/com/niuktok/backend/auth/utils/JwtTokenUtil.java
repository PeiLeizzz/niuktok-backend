package com.niuktok.backend.auth.utils;

import com.niuktok.backend.auth.service.feign.RedisFeign;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.exception.NiuktokException;
import com.niuktok.backend.common.pojo.dto.redis.RedisSetDTO;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import com.niuktok.backend.common.pojo.vo.GenericResponseVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Component
public class JwtTokenUtil {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String TOKEN_REDIS_KEY = "niuktok:token:";

    private static final String SECRET = "jwtniutok";
    private static final String ISS = "echisan";
    private static final String ROLE_CLAIMS = "role";
    private static final String REMEMBER_CLAIMS = "rememberMe";

    // 过期时间是 3600 秒
    private static final int EXPIRATION = 3600;

    // 选择了记住我之后的过期时间为 7 天
    private static final int EXPIRATION_REMEMBER = 604800;

    @Autowired
    private RedisFeign redisFeign;

    // 创建 token
    public String createToken(String userID, String role, boolean rememberMe) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, role);
        map.put(REMEMBER_CLAIMS, rememberMe);
        String token = Jwts.builder()
                        .signWith(SignatureAlgorithm.HS512, SECRET)
                        .setClaims(map)
                        .setIssuer(ISS)
                        .setSubject(userID)
                        .setIssuedAt(new Date())
                        .compact();
        try {
            BaseResponseVO response = redisFeign.set(
                    new RedisSetDTO(
                            TOKEN_REDIS_KEY + userID,
                            token,
                            rememberMe ? EXPIRATION_REMEMBER : EXPIRATION
                    )
            );
            if (!response.getStatus().equals(ResponseStatusType.SUCCESS.getCode())) {
                throw new NiuktokException(ResponseStatusType.ERROR);
            }
            return token;
        } catch (Exception e) {
            throw new NiuktokException(ResponseStatusType.ERROR);
        }
    }

    // 从 token 中获取 userID
    public String getUserID(String token){
        return getTokenBody(token).getSubject();
    }

    // 获取用户角色
    public String getUserRole(String token) {
        return (String) getTokenBody(token).get(ROLE_CLAIMS);
    }

    // 是否已过期
    public boolean isExpiration(String token) {
        String userID = getUserID(token);
        try {
            GenericResponseVO<Boolean> response = redisFeign.exists(TOKEN_REDIS_KEY + userID);
            if (!response.getStatus().equals(ResponseStatusType.SUCCESS.getCode())) {
                throw new NiuktokException(ResponseStatusType.ERROR);
            }
            return !response.getData();
        } catch (Exception e) {
            throw new NiuktokException(ResponseStatusType.ERROR);
        }
    }

    // 续期
    public void renewal(String token) {
        Claims tokenBody = getTokenBody(token);
        String userID = tokenBody.getSubject();
        Boolean rememberMe = (Boolean) tokenBody.get(REMEMBER_CLAIMS);
        // 续期失败不影响
        redisFeign.expire(TOKEN_REDIS_KEY + userID, rememberMe ? EXPIRATION_REMEMBER : EXPIRATION);
    }

    private Claims getTokenBody(String token){
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}