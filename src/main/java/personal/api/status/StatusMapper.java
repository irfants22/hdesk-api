package personal.api.status;

import personal.api.status.dto.StatusDto;

public class StatusMapper {

    public static StatusDto toStatusResponse(Status status) {
        return StatusDto.builder()
                .id(status.getId())
                .name(status.getName())
                .is_closed(status.isClosed())
                .build();
    }
}
