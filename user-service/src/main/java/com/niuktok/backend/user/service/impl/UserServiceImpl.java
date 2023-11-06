package com.niuktok.backend.user.service.impl;

import com.niuktok.backend.common.def.IdentityType;
import com.niuktok.backend.common.def.LogicDeleteEnum;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.entity.User;
import com.niuktok.backend.common.entity.UserAuth;
import com.niuktok.backend.common.exception.NiuktokException;
import com.niuktok.backend.common.mapper.UserAuthMapper;
import com.niuktok.backend.common.mapper.UserMapper;
import com.niuktok.backend.user.pojo.vo.UserAuthVO;
import com.niuktok.backend.user.pojo.vo.UserInfoVO;
import com.niuktok.backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoVO getUserInfo(Long userID) {
        User user = userMapper.selectByPrimaryKey(userID);
        if (user == null) {
            throw new NiuktokException(ResponseStatusType.NOT_EXISTED_USER);
        }
        UserAuth userAuth = new UserAuth();
        userAuth.setUserId(userID);
        userAuth.setIsDeleted(LogicDeleteEnum.NOT_DELETED.value());
        List<UserAuth> userAuths = userAuthMapper.select(userAuth);

        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUsername(user.getUsername());
        userInfoVO.setUserAuthList(
                userAuths.stream().map(
                        ua -> new UserAuthVO(IdentityType.getByCode(ua.getIdentityType()), ua.getIdentifier())
                ).collect(Collectors.toList())
        );
        return userInfoVO;
    }
}
