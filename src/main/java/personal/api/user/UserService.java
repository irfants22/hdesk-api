package personal.api.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import personal.api.user.dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto currentUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        return UserMapper.toUserResponse(user);
    }

}
