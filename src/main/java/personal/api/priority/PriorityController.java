package personal.api.priority;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import personal.api.common.response.ApiResponse;
import personal.api.priority.dto.PriorityDto;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/priorities")
@RequiredArgsConstructor
public class PriorityController {

    private final PriorityService priorityService;

    @GetMapping()
    public ApiResponse<List<PriorityDto>> getPriorities(@RequestParam(required = false) String search) {
        List<PriorityDto> result = priorityService.getPriorities(search);
        return ApiResponse.ok(result);
    }

}
