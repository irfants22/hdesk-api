package personal.api.status;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import personal.api.common.response.ApiResponse;
import personal.api.status.dto.StatusDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/statuses")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @GetMapping()
    public ApiResponse<List<StatusDto>> getStatuses(
            @RequestParam(required = false) String search) {
        List<StatusDto> result = statusService.getStatuses(search);
        return ApiResponse.ok(result);
    }
}
