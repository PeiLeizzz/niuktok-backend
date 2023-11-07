package com.niuktok.backend.auth.service;

import com.niuktok.backend.auth.pojo.dto.UserRegisterDTO;

public interface AuthService {
    void register(UserRegisterDTO userRegisterDTO);

    void resetCredential(Long userID, String identifier, String credential, Byte identityType);
}
