package personal.api.priority;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import personal.api.priority.dto.PriorityDto;

@Service
@RequiredArgsConstructor
public class PriorityService {

    private final PriorityRepository priorityRepository;

    public List<PriorityDto> getPriorities(String search) {
        List<Priority> priorities = search == null || search.isBlank()
                ? priorityRepository.findAllByOrderByLevelAsc()
                : priorityRepository.findAllByNameContainingIgnoreCaseOrderByLevelAsc(search.trim());

        return priorities.stream().map(PriorityMapper::toPriorityResponse).toList();
    }

}
