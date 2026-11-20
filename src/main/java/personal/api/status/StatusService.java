package personal.api.status;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import personal.api.status.dto.StatusDto;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusRepository statusRepository;

    public List<StatusDto> getStatuses(String search) {
        List<Status> statuses = search == null || search.isBlank()
                ? statusRepository.findAll()
                : statusRepository.findAllByNameContainingIgnoreCase(search.trim());

        return statuses.stream().map(StatusMapper::toStatusResponse).toList();
    }
}
