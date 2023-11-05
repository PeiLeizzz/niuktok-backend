package com.niuktok.backend.auth.service.impl;

import com.hy.corecode.idgen.WFGIdGenerator;
import com.niuktok.backend.auth.service.AuthService;
import com.niuktok.backend.auth.service.feign.RedisFeign;
import com.niuktok.backend.auth.utils.JwtTokenUtil;
import com.niuktok.backend.common.def.IdentityType;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.entity.User;
import com.niuktok.backend.common.entity.UserAuth;
import com.niuktok.backend.common.exception.NiuktokException;
import com.niuktok.backend.common.mapper.UserAuthMapper;
import com.niuktok.backend.common.mapper.UserMapper;
import com.niuktok.backend.auth.pojo.dto.UserRegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RedisFeign redisFeign;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserRegisterDTO userRegisterDTO) {
        // 先检查用户名是否重复
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        if (userMapper.selectCount(user) > 0) {
            throw new NiuktokException(ResponseStatusType.EXISTED_USER);
        }

        // 再检查登陆凭证是否重复
        UserAuth userAuth = new UserAuth();
        userAuth.setIdentifier(userRegisterDTO.getIdentifier());
        userAuth.setIdentityType(userRegisterDTO.getIdentityType().getCode());
        if (userMapper.selectCount(user) > 0) {
            throw new NiuktokException(ResponseStatusType.EXISTED_IDENTIFIER);
        }

        // 最后插入用户和认证信息
        user.setId(wfgIdGenerator.next());
        userMapper.insertSelective(user);

        userAuth.setUserId(user.getId());
        userAuth.setCredential(passwordEncoder.encode(userRegisterDTO.getCredential()));
        userAuthMapper.insertSelective(userAuth);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetCredential(Long userID, String identifier, String credential, IdentityType identityType) {
        if (userMapper.selectByPrimaryKey(userID) == null) {
            throw new NiuktokException(ResponseStatusType.NOT_EXISTED_USER);
        }

        UserAuth userAuth = new UserAuth();
        userAuth.setUserId(userID);
        userAuth.setIdentifier(identifier);
        userAuth.setIdentityType(identityType.getCode());
        UserAuth userAuthInDB = userAuthMapper.selectOne(userAuth);
        if (userAuthInDB == null) {
            throw new NiuktokException(ResponseStatusType.NOT_EXISTED_IDENTIFIER);
        }
        userAuth.setCredential(passwordEncoder.encode(credential));
        userAuth.setId(userAuthInDB.getId());
        userAuthMapper.updateByPrimaryKeySelective(userAuth);

        // 删除登陆信息
        redisFeign.delete(JwtTokenUtil.TOKEN_REDIS_KEY + userID);
    }
}
