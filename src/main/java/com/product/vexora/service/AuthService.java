package com.product.vexora.service;

import com.product.vexora.dto.LoginDto;
import com.product.vexora.dto.SignupDto;

public interface AuthService {

    void signup(SignupDto dto);

    String login(LoginDto dto);
}
