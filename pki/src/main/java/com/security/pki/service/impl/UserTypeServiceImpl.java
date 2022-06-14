package com.security.pki.service.impl;

import com.security.pki.model.UserType;
import com.security.pki.repository.UserTypeRepository;
import com.security.pki.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTypeServiceImpl implements UserTypeService {
    @Autowired
    private UserTypeRepository userTypeRepository;

    @Override
    public UserType findUserTypeByName(String roleUser) {
        return userTypeRepository.findByName(roleUser);
    }
}
