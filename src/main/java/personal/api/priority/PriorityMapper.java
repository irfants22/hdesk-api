package personal.api.priority;

import personal.api.priority.dto.PriorityDto;

public class PriorityMapper {

    public static PriorityDto toPriorityResponse(Priority priority) {
        return PriorityDto.builder()
                .id(priority.getId())
                .name(priority.getName())
                .level(priority.getLevel())
                .build();
    }
}
