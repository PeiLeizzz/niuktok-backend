package com.niuktok.backend.user.service;

import com.niuktok.backend.user.pojo.dto.UserRegisterDTO;
import com.niuktok.backend.user.pojo.vo.UserInfoVO;

public interface UserService {
    void register(UserRegisterDTO userRegisterDTO);

    UserInfoVO getUserInfo(Integer userID);
}
