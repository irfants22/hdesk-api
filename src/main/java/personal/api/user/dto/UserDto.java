package personal.api.user.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import personal.api.role.dto.RoleDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private UUID id;

    private String name;

    private String email;

    private RoleDto role;

    private boolean active;
}