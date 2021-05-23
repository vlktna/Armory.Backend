package blur.tech.armory.dto.service;

import blur.tech.armory.controller.models.User;
import blur.tech.armory.controller.models.update.UserUpdate;
import blur.tech.armory.dto.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import blur.tech.armory.dto.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findAllByEmail(email);
    }

    public UserEntity findById(Integer id) {
        return userRepository.findUserEntityById(id);
    }

    public List<UserEntity> findUserListByEmail(List<String> emails) {
        List<UserEntity> users = new ArrayList<>();
        for (String email : emails) {
            users.add(userRepository.findAllByEmail(email));
        }
        return users;
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void save(UserEntity user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void updateUser(UserUpdate userUpdate) {
        if (userUpdate.getFirstName() != null) {
            userRepository.updateFirstName(userUpdate.getId(), userUpdate.getFirstName());
        }
        if (userUpdate.getLastName() != null) {
            userRepository.updateLastName(userUpdate.getId(), userUpdate.getLastName());
        }

    }

    public void updateUserRole(UserUpdate userUpdate) {
        if (userUpdate.getRole() != null) {
            userRepository.updateRole(userUpdate.getId(), userUpdate.getRole());
        }
    }
}
