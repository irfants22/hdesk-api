package personal.api.auth.jwt;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RevokedTokenService {

    private final RevokedTokenRepository revokedTokenRepository;

    public void revoke(Claims claims) {
        String jti = claims.getId();

        if (jti == null || revokedTokenRepository.existsByTokenId(jti)) {
            return;
        }

        RevokedToken revokedToken = new RevokedToken();
        revokedToken.setTokenId(jti);
        revokedToken.setExpiredAt(claims.getExpiration().toInstant());
        revokedTokenRepository.save(revokedToken);
    }

    public boolean isRevoked(String jti) {
        return jti != null && revokedTokenRepository.existsByTokenId(jti);
    }
}
