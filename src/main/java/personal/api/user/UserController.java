package personal.api.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import personal.api.common.response.ApiResponse;
import personal.api.user.dto.CreateUserRequestDto;
import personal.api.user.dto.UpdateUserStatusRequestDto;
import personal.api.user.dto.UserDto;
import personal.api.user.dto.UserFilterDto;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/users/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ApiResponse<List<UserDto>> getUsers(
            @Valid UserFilterDto filter,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserDto> result = userService.getUsers(filter, pageable);
        return ApiResponse.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ApiResponse<UserDto> createUser(@RequestBody @Valid CreateUserRequestDto request) {
        UserDto result = userService.createUser(request);
        return ApiResponse.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/status")
    public ApiResponse<UserDto> updateUserStatus(
            @PathVariable UUID userId,
            @RequestBody @Valid UpdateUserStatusRequestDto request,
            @AuthenticationPrincipal String email) {
        UserDto result = userService.updateUserStatus(userId, request, email);
        return ApiResponse.ok(result);
    }

    @GetMapping("me")
    public ApiResponse<UserDto> currentUser(@AuthenticationPrincipal String email) {
        UserDto result = userService.currentUser(email);
        return ApiResponse.ok(result);
    }
}
