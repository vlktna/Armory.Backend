package blur.tech.armory.controller.models.update;

import blur.tech.armory.dto.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdate {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
}
