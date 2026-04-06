package com.springboot.project.dao.repository;

import com.springboot.project.dao.entity.LoginUserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginUserRepository extends
        JpaRepository<LoginUserEntity, UUID>,
        GenericRepository<LoginUserRepository> {

    Optional<LoginUserEntity> findByEmail(String email);
}
