package com.guava.judelaw.user.persistence;

import com.guava.judelaw.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByUserIdAndPassword(String userId, String password);
    boolean existsByUserId(String userId);

}
