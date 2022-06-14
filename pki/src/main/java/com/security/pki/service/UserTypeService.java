package com.security.pki.service;

import com.security.pki.model.UserType;

public interface UserTypeService {
    UserType findUserTypeByName(String role_user);
}
