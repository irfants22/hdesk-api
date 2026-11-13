package personal.api.user;

import personal.api.role.dto.RoleDto;
import personal.api.user.dto.UserDto;

public class UserMapper {

    public static UserDto toUserResponse(User user) {
        RoleDto roleDto = null;
        if (user.getRole() != null) {
            roleDto = RoleDto.builder()
                    .id(user.getRole().getId())
                    .name(user.getRole().getName())
                    .build();
        }
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .active(user.isActive())
                .role(roleDto)
                .build();
    }
}
