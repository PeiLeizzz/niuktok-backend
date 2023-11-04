package com.niuktok.backend.auth.service.impl;

import com.hy.corecode.idgen.WFGIdGenerator;
import com.niuktok.backend.auth.service.AuthService;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.entity.User;
import com.niuktok.backend.common.entity.UserAuth;
import com.niuktok.backend.common.exception.NiuktokException;
import com.niuktok.backend.common.mapper.UserAuthMapper;
import com.niuktok.backend.common.mapper.UserMapper;
import com.niuktok.backend.auth.pojo.dto.UserRegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private WFGIdGenerator wfgIdGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        if (userMapper.selectOne(user) != null) {
            throw new NiuktokException(ResponseStatusType.EXISTED_USER);
        }
        user.setId(wfgIdGenerator.next());
        userMapper.insertSelective(user);

        UserAuth userAuth = new UserAuth();
        userAuth.setUserId(user.getId());
        userAuth.setCredential(passwordEncoder.encode(userRegisterDTO.getCredential()));
        userAuth.setIdentifier(userRegisterDTO.getIdentifier());
        userAuth.setIdentityType(userRegisterDTO.getIdentityType().getCode());
        userAuthMapper.insertSelective(userAuth);
    }
}
