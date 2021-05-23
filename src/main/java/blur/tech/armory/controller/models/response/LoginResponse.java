package blur.tech.armory.controller.models.response;

import blur.tech.armory.dto.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
    private String jwt;

}
