/**
 * @autheor: Mason
 * @date:2022/1/23 21:18
 */

package com.heima.user.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.LoginDto;
import com.heima.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
public class ApUserLoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login_auth")
    public ResponseResult login(LoginDto dto){
        return userService.login(dto);
    }
}
