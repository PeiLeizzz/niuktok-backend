package com.niuktok.backend.user.service;

import com.niuktok.backend.user.pojo.vo.UserInfoVO;

public interface UserService {
    UserInfoVO getUserInfo(Long userID, Boolean isMe);
}
