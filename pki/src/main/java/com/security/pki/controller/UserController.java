package com.security.pki.controller;

import com.security.pki.dto.ChangePasswordDTO;
import com.security.pki.dto.LoginDTO;
import com.security.pki.dto.SignUpUserDTO;
import com.security.pki.dto.UserDTO;
import com.security.pki.enums.UserType;
import com.security.pki.mapper.UserMapper;
import com.security.pki.model.User;
import com.security.pki.repository.UserRepository;
import com.security.pki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    @RequestMapping(method = RequestMethod.GET, value = "/getById/{id}")
    public ResponseEntity getUserById(@PathVariable Integer id) {
        User user = userService.findUserById(id);
        if(user == null) {
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(new UserMapper().UserToUserDto(user));
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, value = "/addAdmin")
    public ResponseEntity<?> addAdmin(@RequestBody SignUpUserDTO dto) throws Exception {
        User user = userService.registerAdmin(dto);
        return ResponseEntity.ok(user.getId());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findAll")
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getByEmail/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findAllClients")
    public ResponseEntity<List<User>> findAllClients() {
        List<User> users = new ArrayList<>();
        for (User u: userService.findAll()) {
            if(u.getUserType().equals(UserType.USER)){
                users.add(u);
            }
        }
        return ResponseEntity.ok(users);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/changePassword",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO dto, Principal user) throws Exception {
        try {
            userService.changePassword(dto, user.getName());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
