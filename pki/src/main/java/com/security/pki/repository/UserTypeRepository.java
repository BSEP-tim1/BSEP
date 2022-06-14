package com.security.pki.repository;

import com.security.pki.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    UserType findByName(String roleUser);
}
