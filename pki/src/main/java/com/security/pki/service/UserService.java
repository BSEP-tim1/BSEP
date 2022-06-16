package com.security.pki.service;

import com.security.pki.dto.ChangePasswordDTO;
import com.security.pki.dto.LoginDTO;
import com.security.pki.dto.SignUpUserDTO;
import com.security.pki.model.User;

import java.util.List;

public interface UserService {
    User register(SignUpUserDTO dto) throws Exception;

    User findUserById(Integer id);

    User login(String email, String password);

    User findByEmail(String subjectName);

    List<User> findAll();

    boolean checkPasswordCriteria(String password);

    boolean verifyUserAccount(String verificationToken);

    boolean verifyRecoveryAccount(String verificationToken, String newPassword) throws Exception;

    boolean recoveryPass(String email) throws Exception;

    User registerAdmin(SignUpUserDTO dto) throws Exception;

    void changePassword(ChangePasswordDTO dto, String userEmail) throws Exception;
}
