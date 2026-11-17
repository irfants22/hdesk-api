package personal.api.user;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import personal.api.role.Role;
import personal.api.role.RoleRepository;
import personal.api.user.dto.CreateUserRequestDto;
import personal.api.user.dto.UserDto;
import personal.api.user.dto.UserFilterDto;
import personal.api.util.ValidationUtil;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ValidationUtil validationUtil;
    private final PasswordEncoder passwordEncoder;

    private static final Set<String> SORTABLE_FIELDS = Set.of("name", "email", "active", "createdAt");
    private static final Set<String> PERMITTED_ROLES = Set.of("admin", "staff", "user");

    @Transactional(readOnly = true)
    public Page<UserDto> getUsers(UserFilterDto filter, Pageable pageable) {
        validationUtil.validate(filter);

        Specification<User> specification = Specification.allOf(
                UserSpecification.search(filter.getSearch()),
                UserSpecification.hasRole(filter.getRole()),
                UserSpecification.hasActive(filter.getIsActive()));

        return userRepository.findAll(specification, sanitize(pageable)).map(UserMapper::toUserResponse);
    }

    public UserDto createUser(CreateUserRequestDto request) {
        validationUtil.validate(request);

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        if (!PERMITTED_ROLES.contains(request.getRole())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The role entered is invalid");
        }

        Role role = roleRepository.findByName(request.getRole().toUpperCase()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The role entered is invalid"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        userRepository.save(user);

        return UserMapper.toUserResponse(user);
    }

    private Pageable sanitize(Pageable pageable) {
        pageable.getSort().forEach(order -> {
            if (!SORTABLE_FIELDS.contains(order.getProperty())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sort field");
            }
        });
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().and(Sort.by("id")));
    }

    public UserDto currentUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        return UserMapper.toUserResponse(user);
    }
}
