package com.niuktok.backend.user.service.impl;

import com.niuktok.backend.common.def.IdentifierType;
import com.niuktok.backend.common.entity.User;
import com.niuktok.backend.common.entity.UserAuth;
import com.niuktok.backend.common.mapper.UserAuthMapper;
import com.niuktok.backend.common.mapper.UserMapper;
import com.niuktok.backend.user.pojo.dto.UserRegisterDTO;
import com.niuktok.backend.user.pojo.vo.UserInfoVO;
import com.niuktok.backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        int userID = userMapper.insertSelective(user);

        UserAuth userAuth = new UserAuth();
        userAuth.setUserId(userID);
        userAuth.setCredential(userRegisterDTO.getCredential());
        userAuth.setIdentifier(userRegisterDTO.getIdentifier());
        userAuth.setIdentityType(userRegisterDTO.getIdentifierType().getCode());
        userAuthMapper.insertSelective(userAuth);
    }

    @Override
    public UserInfoVO getUserInfo(Integer userID) {
        User user = userMapper.selectByPrimaryKey(userID);
        UserAuth userAuth = new UserAuth();
        userAuth.setUserId(userID);
        UserAuth userAuthInDB = userAuthMapper.selectOne(userAuth);

        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUsername(user.getUsername());
        userInfoVO.setIdentifierType(IdentifierType.getByCode(userAuthInDB.getIdentityType()));
        return userInfoVO;
    }
}
