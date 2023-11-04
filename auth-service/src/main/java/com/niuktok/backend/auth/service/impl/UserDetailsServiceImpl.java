package com.niuktok.backend.auth.service.impl;

import com.niuktok.backend.auth.entity.AuthenticationUser;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.entity.User;
import com.niuktok.backend.common.entity.UserAuth;
import com.niuktok.backend.common.exception.NiuktokException;
import com.niuktok.backend.common.mapper.UserAuthMapper;
import com.niuktok.backend.common.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component("userDetailsService")
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuth userAuth = new UserAuth();
        int barIndex = username.indexOf(':');
        byte identityTypeCode;
        try {
            identityTypeCode = Byte.parseByte(username.substring(0, barIndex));
        } catch (Exception e) {
            throw new NiuktokException(ResponseStatusType.NOT_EXISTED_USER, e);
        }

        String identifier = username.substring(barIndex + 1);
        userAuth.setIdentityType(identityTypeCode);
        userAuth.setIdentifier(identifier);
        UserAuth userAuthInDB = userAuthMapper.selectOne(userAuth);
        if (userAuthInDB == null) {
            throw new NiuktokException(ResponseStatusType.NOT_EXISTED_USER);
        }

        User user = userMapper.selectByPrimaryKey(userAuthInDB.getUserId());
        if (user == null) {
            throw new NiuktokException(ResponseStatusType.NOT_EXISTED_USER);
        }

        AuthenticationUser authenticationUser = new AuthenticationUser(userAuthInDB.getIdentifier(), userAuthInDB.getCredential(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        authenticationUser.setUser(user);
        return authenticationUser;
    }
}
