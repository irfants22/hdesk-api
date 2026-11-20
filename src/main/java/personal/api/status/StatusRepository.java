package personal.api.status;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, UUID> {

    List<Status> findAllByNameContainingIgnoreCase(String keyword);
}
