package com.security.pki.controller;

import com.security.pki.dto.LoginDTO;
import com.security.pki.dto.SignUpUserDTO;
import com.security.pki.dto.UserTokenStateDTO;
import com.security.pki.model.Permission;
import com.security.pki.model.User;
import com.security.pki.security.util.TokenUtils;
import com.security.pki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import org.apache.log4j.Logger;

import java.util.Collection;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenUtils tokenUtils;

    static Logger logger = Logger.getLogger(AuthController.class.getName());

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpUserDTO dto) throws Exception {
        try {
            User user = userService.register(dto);
            if(user == null) {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/activateAccount")
    public ResponseEntity<?> activateAccount(@RequestParam("token")String verificationToken) {
        if(userService.verifyUserAccount(verificationToken)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(value = "/recoveryEmail")
    public ResponseEntity<?> recoveryEmail(@RequestParam("email") String email) throws Exception {
        if(userService.recoveryPass(email)) {
            logger.info("Successful send recovery email. Email: "+ email);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        logger.error("Recovery mail not send. Email: "+email);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(value = "/activateNewPassword")
    public ResponseEntity<?> activatePassword(@RequestParam("token")String verificationToken, @RequestBody String newPassword) throws Exception {
        if(userService.verifyRecoveryAccount(verificationToken, newPassword)) {
            logger.info("Successful sent activation email.");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<UserTokenStateDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getEmail(), loginDTO.getPassword()));
        } catch (Exception ex) {
            if (ex.getMessage().contains("User is disabled")) {
                logger.error("Failed login. User: " + loginDTO.getEmail() + ", Account not activated.");
                return new ResponseEntity("Account is not activated", HttpStatus.BAD_REQUEST);
            } else if(userService.checkUser(loginDTO.getEmail()) == false){
                logger.error("Failed login. User: " + loginDTO.getEmail() + ", Account not activated.");
                return new ResponseEntity("User is not activated", HttpStatus.BAD_REQUEST);
            }
            logger.warn("Failed login. User: " + loginDTO.getEmail() + ", Bad credentials.");
            return new ResponseEntity("Bad credentials", HttpStatus.BAD_REQUEST);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        if (!user.getIsActive()) {
            logger.error("Failed login. User: " + loginDTO.getEmail() + ", Account not activated.");
            return new ResponseEntity("User is not activated", HttpStatus.BAD_REQUEST);
        }
        Collection<Permission> p = user.getUserType().getPermissions();
        String jwt = tokenUtils.generateToken(user.getUsername(), user.getUserType().getName(), user.getId(), p);
        int expiresIn = tokenUtils.getExpiredIn();

        logger.info("Successful login. User: " + loginDTO.getEmail());

        return ResponseEntity.ok(new UserTokenStateDTO(jwt, expiresIn));
    }

}
