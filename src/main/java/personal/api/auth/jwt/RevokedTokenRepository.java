package personal.api.auth.jwt;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, UUID> {

    boolean existsByTokenId(String tokenId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RevokedToken r WHERE r.expiredAt < :now")
    void deleteExpired(Instant now);
}
