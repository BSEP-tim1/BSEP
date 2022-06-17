package com.security.pki.service.impl;

import com.security.pki.controller.AuthController;
import com.security.pki.dto.ChangePasswordDTO;
import com.security.pki.dto.LoginDTO;
import com.security.pki.dto.SignUpUserDTO;
import com.security.pki.mapper.UserMapper;
import com.security.pki.model.User;
import com.security.pki.model.UserType;
import com.security.pki.model.VerificationToken;
import com.security.pki.repository.UserRepository;
import com.security.pki.repository.VerificationTokenRepository;
import com.security.pki.service.EmailService;
import com.security.pki.service.UserService;
import com.security.pki.service.UserTypeService;
import com.security.pki.service.VerificationTokenService;
import org.apache.log4j.Logger;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final int TOKEN_EXPIRES_MINUTES = 15;
    private final int MIN_PASSWORD_LENGTH = 8;

    static Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public User register(SignUpUserDTO dto) throws Exception {
        for(User user: userRepository.findAll()){
            if(user.getEmail().equals(dto.email)){
                logger.error("Registration failed. Email user: " + user.getEmail() + ". Not unique email.");
                throw new Exception("Email is not unique");
            }
        }
        if (!checkPasswordCriteria(dto.password)) {
            String pswdError = "Password must contain minimum 8 characters, at least one uppercase " +
                    "letter, one lowercase letter, one number and one special character and " +
                    "must not contain white spaces";
            System.out.println(pswdError);
            logger.error("Registration failed. Email user: " + dto.getEmail() + ". Password does not match criteria.");
            throw new Exception(pswdError);
        }
        User newUser = new UserMapper().SignUpUserDtoToUser(dto);
        newUser.setIsActive(false);
        newUser.setLastPasswordResetDate(Timestamp.from(Instant.now()));
        UserType role = userTypeService.findUserTypeByName("ROLE_USER");
        if (role == null) {
            logger.error("Registration failed. Email user: " + dto.getEmail() + ", Role user:" + dto.getUserType() +". Role does not exist.");
            throw new Exception("Role does not exist");
        }
        newUser.setUserType(role);
        newUser.setPassword(passwordEncoder.encode(dto.password));

        VerificationToken verificationToken = new VerificationToken(newUser);
        if (!emailService.sendAccountActivationMail(verificationToken.getToken(), newUser.getEmail())) {
            logger.error("Registration failed. Email user: " + dto.getEmail() + ". Email does not send.");
            throw new Exception("Email for account verification not sent, try again");
        }
        userRepository.save(newUser);
        User registeredUser = userRepository.findByEmail(newUser.getEmail());
        verificationTokenService.saveVerificationToken(verificationToken);
        logger.info("Successful register. User: " + newUser.getEmail());
        return registeredUser;
    }

    @Override
    public boolean recoveryPass(String email) throws Exception{
        User is = userRepository.findByEmail(email);
        if(is == null){
            throw new Exception("We don't have any user with that email");
        }
        VerificationToken verificationToken = new VerificationToken(is);
        if (!emailService.sendAccountRecoveryMail(verificationToken.getToken(), is.getEmail())) {
            throw new Exception("Email for account verification not sent, try again");
        }
        verificationTokenService.saveVerificationToken(verificationToken);
        return true;
    }

    @Override
    public User findUserById(Integer id) {
        for(User user: userRepository.findAll()){
            if(user.getId() == id){
                return user;
            }
        }
        return null;
    }

    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user != null) {
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findByEmail(String subjectName) {
        return userRepository.findByEmail(subjectName);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean checkPasswordCriteria(String password) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 100),
                new UppercaseCharacterRule(1),
                new LowercaseCharacterRule(1),
                new DigitCharacterRule(1),
                new SpecialCharacterRule(1),
                new WhitespaceRule()));

        RuleResult result = validator.validate(new PasswordData(password));
        return result.isValid();
    }

    @Override
    public boolean verifyUserAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findVerificationTokenByToken(token);
        long difference_In_Time = (new Date()).getTime() - verificationToken.getCreatedDateTime().getTime();
        User user = userRepository.findByEmail(verificationToken.getUser().getEmail());
        ;
        long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
        if (difference_In_Minutes <= TOKEN_EXPIRES_MINUTES) {
            user.setIsActive(true);
            userRepository.save(user);
            verificationTokenRepository.delete(verificationToken);
            return true;
        } else {
            userRepository.delete(user);
            verificationTokenRepository.delete(verificationToken);
            return false;
        }
    }

    @Override
    public boolean verifyRecoveryAccount(String token, String newPassword) throws Exception {
        VerificationToken verificationToken = verificationTokenRepository.findVerificationTokenByToken(token);
        long difference_In_Time = (new Date()).getTime() - verificationToken.getCreatedDateTime().getTime();
        User user = userRepository.findByEmail(verificationToken.getUser().getEmail());
        ;
        long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;

        if (!checkPasswordCriteria(newPassword)) {
            String pswdError = "Password must contain minimum 8 characters, at least one uppercase " +
                    "letter, one lowercase letter, one number and one special character and " +
                    "must not contain white spaces";
            System.out.println(pswdError);
            throw new Exception(pswdError);
        }

        if (difference_In_Minutes <= TOKEN_EXPIRES_MINUTES) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            verificationTokenRepository.delete(verificationToken);
            return true;
        } else {
            verificationTokenRepository.delete(verificationToken);
            return false;
        }
    }

    @Override
    public User registerAdmin(SignUpUserDTO dto) throws Exception {
        for(User user: userRepository.findAll()){
            if(user.getEmail().equals(dto.email)){
                throw new Exception("Email is not unique");
            }
        }
        if (!checkPasswordCriteria(dto.password)) {
            String pswdError = "Password must contain minimum eight characters, at least one uppercase " +
                    "letter, one lowercase letter, one number and one special character and " +
                    "must not contain white spaces";
            System.out.println(pswdError);
            throw new Exception(pswdError);
        }
        User newUser = new UserMapper().SignUpUserDtoToUser(dto);
        newUser.setIsActive(true);
        newUser.setLastPasswordResetDate(Timestamp.from(Instant.now()));
        UserType role = userTypeService.findUserTypeByName("ROLE_ADMIN");
        if (role == null) {
            throw new Exception("Role does not exist");
        }
        newUser.setUserType(role);
        newUser.setPassword(passwordEncoder.encode(dto.password));
        userRepository.save(newUser);
        VerificationToken verificationToken = new VerificationToken(newUser);
        verificationTokenService.saveVerificationToken(verificationToken);
        return userRepository.findByEmail(newUser.getEmail());
    }

    @Override
    public void changePassword(ChangePasswordDTO dto, String userEmail) throws Exception {
        String pswdError = "Password must contain minimum eight characters, at least one uppercase " +
                "letter, one lowercase letter, one number and one special character and " +
                "must not contain white spaces";
        if (!checkPasswordCriteria(dto.getNewPassword())) {
            throw new Exception(pswdError);
        }
        if (!checkPasswordCriteria(dto.getReenteredPassword())) {
            throw new Exception(pswdError);
        }

        User user = userRepository.findByEmail(userEmail);
        if(!user.getIsActive()){
            throw new Exception("Account is not activated");
        }
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new Exception("Old password does not match the current password");
        }
        if (!dto.getNewPassword().equals(dto.getReenteredPassword())) {
            throw new Exception("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setLastPasswordResetDate(Timestamp.from(Instant.now()));
        userRepository.save(user);
    }
}
