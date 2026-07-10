package com.springboot.project.loginuser.repository;

import com.springboot.project.loginuser.entity.LoginUserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginUserRepository extends JpaRepository<LoginUserEntity, UUID> {

    Optional<LoginUserEntity> findByEmail(String email);
}
