package personal.api.priority;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PriorityRepository extends JpaRepository<Priority, UUID> {

    List<Priority> findAllByOrderByLevelAsc();

    List<Priority> findAllByNameContainingIgnoreCaseOrderByLevelAsc(String keyword);

}
