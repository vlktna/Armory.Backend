package blur.tech.armory.controller;

import blur.tech.armory.controller.models.User;
import blur.tech.armory.controller.models.update.UserUpdate;
import blur.tech.armory.dto.entity.UserEntity;
import blur.tech.armory.dto.entity.UserRole;
import blur.tech.armory.dto.service.UserService;
import blur.tech.armory.security.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@CrossOrigin(origins = {"*"}, allowedHeaders = {"*"})
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final Token tokenUtil;

    @RequestMapping(value = "/find-by-email", method = RequestMethod.GET, params = "email")
    public ResponseEntity<User> register(@RequestParam("email") String email) {
        UserEntity userEntity = userService.findByEmail(email);

        if (userEntity == null) {
            return new ResponseEntity<>((User) null, HttpStatus.BAD_REQUEST);
        }
        User user = new User(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getEmail(), userEntity.getRole());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/find-all", method = RequestMethod.GET)
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> userList = new ArrayList<>();
        List<UserEntity> userEntityList = userService.findAll();
        for (UserEntity userEntity : userEntityList) {
            userList.add(new User(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(),
                    userEntity.getEmail(), userEntity.getRole()));
        }
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<UserUpdate> updateUser(@RequestHeader Map<String, String> headers,
                                                 @RequestBody UserUpdate userUpdate) {
        String jwt = headers.get("authorization");
        UserEntity user = userService.findByEmail(tokenUtil.extractUsername(jwt));

        if (user.getRole() == UserRole.admin) {
            userService.updateUserRole(userUpdate);
        }

        userService.updateUser(userUpdate);
        UserEntity userEntity = userService.findById(userUpdate.getId());

        return new ResponseEntity<>(new UserUpdate(userEntity.getId(), userEntity.getFirstName(),
                userEntity.getLastName(), userEntity.getEmail(), userEntity.getRole()), HttpStatus.OK);
    }

}
