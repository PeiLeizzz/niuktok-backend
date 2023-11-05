package com.niuktok.backend.common.controller.auth;

import com.niuktok.backend.common.pojo.vo.GenericResponseVO;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;

public interface AuthController {
    @RequestMapping(value = "${customize.service.auth.entry-point.prefix}")
    GenericResponseVO<String> auth(Principal principal);
}
