package com.niuktok.backend.user.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.niuktok.backend.common.def.SexType;
import com.niuktok.backend.common.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户基本信息")
public class UserInfoVO {
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("性别")
    private SexType sex;

    @ApiModelProperty("关注数")
    private Long followedNum;

    @ApiModelProperty("粉丝数")
    private Long followerNum;

    @ApiModelProperty("登陆信息列表")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<UserAuthVO> userAuthList;

    public UserInfoVO(User user) {
        this.username = user.getUsername();
        this.avatar = user.getAvatar();
        this.sex = SexType.getByCode(user.getSex());
        this.followedNum = user.getFollowedNum();
        this.followerNum = user.getFollowerNum();
    }
}
