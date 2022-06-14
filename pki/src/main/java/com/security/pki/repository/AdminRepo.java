package com.security.pki.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.pki.model.User;

public interface AdminRepo extends JpaRepository<User , Integer> {

}
