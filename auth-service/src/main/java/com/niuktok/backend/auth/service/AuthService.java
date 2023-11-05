package com.niuktok.backend.auth.service;

import com.niuktok.backend.auth.pojo.dto.UserRegisterDTO;
import com.niuktok.backend.common.def.IdentityType;

public interface AuthService {
    void register(UserRegisterDTO userRegisterDTO);

    void resetCredential(Long userID, String identifier, String credential, IdentityType identityType);
}
