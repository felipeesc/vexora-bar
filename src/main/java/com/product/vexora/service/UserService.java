package com.product.vexora.service;

import com.product.vexora.entity.User;

public interface UserService {

    User loadByUsername(String username);
}
