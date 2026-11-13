package personal.api.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import personal.api.auth.dto.LoginRequestDto;
import personal.api.auth.dto.RegisterRequestDto;
import personal.api.auth.dto.TokenResponseDto;
import personal.api.auth.jwt.JwtService;
import personal.api.auth.jwt.RevokedTokenService;
import personal.api.role.Role;
import personal.api.role.RoleRepository;
import personal.api.user.User;
import personal.api.user.UserMapper;
import personal.api.user.UserRepository;
import personal.api.user.dto.UserDto;
import personal.api.util.ValidationUtil;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ValidationUtil validationUtil;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RevokedTokenService revokedTokenService;

    public UserDto register(RegisterRequestDto request) {
        validationUtil.validate(request);

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        Role role = roleRepository.findByName("USER").orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No valid role available for the user"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        userRepository.save(user);

        return UserMapper.toUserResponse(user);
    }

    public TokenResponseDto login(LoginRequestDto request) {
        validationUtil.validate(request);

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().getName());

        return new TokenResponseDto(token);
    }

    public void logout(String token) {
        Claims claims = jwtService.parseToken(token);
        revokedTokenService.revoke(claims);
    }

}
