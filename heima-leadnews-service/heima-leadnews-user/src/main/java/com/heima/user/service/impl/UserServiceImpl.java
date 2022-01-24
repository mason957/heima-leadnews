/**
 * @autheor: Mason
 * @date:2022/1/23 22:00
 */

package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.exception.CustomException;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.LoginDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.user.mapper.UserMapper;
import com.heima.user.service.UserService;
import com.heima.utils.common.AppJwtUtil;
import com.heima.utils.common.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, ApUser> implements UserService {
    /**
     * 登录
     * @param dto
     * @return
     */
    @Override
    public ResponseResult login(LoginDto dto) {
        //如果都不为空
        if (StringUtils.isNotBlank(dto.getPhone())&&StringUtils.isNotBlank(dto.getPassword())){
            //用户模式登录
            //跟据phone查找user对象
            LambdaQueryWrapper<ApUser> qw = Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone, dto.getPhone());
            ApUser apUser = getOne(qw);
            //如果user为空抛出异常
            if (apUser==null){
                throw new CustomException(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
            }
            //不为空，则跟据密码加salt md5加密后与查出的password比较
            String password = MD5Utils.encodeWithSalt(dto.getPassword(), apUser.getSalt());
            //如果不同则抛异常
            if (!password.equals(apUser.getPassword())){
                throw new CustomException(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }
            //如果没问题则跟据id构建token
            String token = AppJwtUtil.getToken(apUser.getId().longValue());
            //构建返回值，返回
            apUser.setName("");
            apUser.setSalt("");
            apUser.setPassword("");
            Map map = new HashMap();
            map.put("user",apUser);
            map.put("token",token);
            return ResponseResult.okResult(map);
        }else{
            //游客登录，用0构建token返回
            String token = AppJwtUtil.getToken(0L);
            Map map = new HashMap();
            map.put("token",token);
            return ResponseResult.okResult(map);
        }
    }
}
