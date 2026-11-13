package personal.api.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import personal.api.common.response.ApiResponse;
import personal.api.user.dto.UserDto;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/users/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("me")
    public ApiResponse<UserDto> currentUser(@AuthenticationPrincipal String email) {
        UserDto result = userService.currentUser(email);
        return ApiResponse.ok(result);
    }

}
