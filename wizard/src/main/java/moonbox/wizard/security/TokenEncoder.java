package moonbox.wizard.security;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import moonbox.common.MbConf;
import moonbox.wizard.config.MbwConfig;
import moonbox.wizard.consts.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TokenEncoder {

    @Autowired
    private MbConf conf;

    public String encode(Session session) {
        return encode(session, getLoginTimeoutMs());
    }

    public String encode(Session session, Long expireMs) {
        JwtBuilder jwtBuilder = Jwts.builder();

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.TOKEN_USER_NAME, session.getUser());
        claims.put(Constants.TOKEN_USER_ID, session.getUserId());
        claims.put(Constants.TOKEN_ORG_NAME, session.getOrg());
        claims.put(Constants.TOKEN_ORG_ID, session.getOrgId());

        jwtBuilder.setHeader(getJwtHeader())
                .setClaims(claims)
                .signWith(SignatureAlgorithm.forName(getAlgorithm()), getSecret());

        if (expireMs != null) {
            Long expiration = System.currentTimeMillis() + expireMs;
            jwtBuilder.setExpiration(new Date(expiration));
        }
        return jwtBuilder.compact();
    }

    public Session decode(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getSecret())
                    .parseClaimsJws(token.substring(Constants.TOKEN_PREFIX.length()).trim())
                    .getBody();
            Session session = new Session();
            session.setUser(claims.get(Constants.TOKEN_USER_NAME).toString());
            session.setUserId(Long.valueOf(claims.get(Constants.TOKEN_USER_ID).toString()));
            session.setOrg(claims.get(Constants.TOKEN_ORG_NAME).toString());
            session.setOrgId(Long.valueOf(claims.get(Constants.TOKEN_ORG_ID).toString()));
            return session;
        } catch (JwtException e) {
//            log.error("decode token failed", e);
            throw e;
        }
    }

    public boolean isValid(String token) {
        try {
            decode(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Map<String, Object> getJwtHeader() {
        Map<String, Object> map = new HashMap<>();
        map.put(JwsHeader.TYPE, JwsHeader.JWT_TYPE);
        map.put(JwsHeader.ALGORITHM, getAlgorithm());
        return map;
    }

    private String getAlgorithm() {
        return conf.get(MbwConfig.JWT_ALGORITHM, MbwConfig.DEFAULT_JWT_ALGORITHM);
    }

    private String getSecret() {
        return conf.get(MbwConfig.JWT_SECRET, MbwConfig.DEFAULT_JWT_SECRET);
    }

    private Long getLoginTimeoutMs() {
        return conf.get(MbwConfig.LOGIN_TIMEOUT, MbwConfig.DEFAULT_LOGIN_TIMEOUT);
    }
}
