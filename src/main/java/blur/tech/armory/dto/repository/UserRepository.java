package blur.tech.armory.dto.repository;


import blur.tech.armory.dto.entity.UserEntity;
import blur.tech.armory.dto.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findAllByEmail(String email);
    UserEntity findUserEntityById(Integer id);

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.firstName = :firstName where u.id = :id")
    void updateFirstName(Integer id, String firstName);

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.lastName = :lastName where u.id = :id")
    void updateLastName(Integer id, String lastName);

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.role = :role where u.id = :id")
    void updateRole(Integer id, UserRole role);
}