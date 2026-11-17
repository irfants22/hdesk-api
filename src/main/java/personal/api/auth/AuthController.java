package personal.api.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import personal.api.auth.dto.LoginRequestDto;
import personal.api.auth.dto.RegisterRequestDto;
import personal.api.auth.dto.TokenResponseDto;
import personal.api.common.response.ApiResponse;
import personal.api.user.dto.UserDto;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<UserDto> register(@RequestBody @Valid RegisterRequestDto request) {
        UserDto result = authService.register(request);
        return ApiResponse.ok(result);
    }

    @PostMapping("/login")
    public ApiResponse<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
        TokenResponseDto result = authService.login(request);
        return ApiResponse.ok(result);
    }

    @PostMapping("/logout")
    public ApiResponse<Boolean> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
        authService.logout(header.substring(7));
        return ApiResponse.ok(true);
    }

}
