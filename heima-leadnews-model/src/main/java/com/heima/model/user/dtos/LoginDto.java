/**
 * @autheor: Mason
 * @date:2022/1/23 21:54
 */

package com.heima.model.user.dtos;

import lombok.Data;

@Data
public class LoginDto {
    private String phone;
    private String password;
}
