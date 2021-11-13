package com.guava.judelaw.user.service;

import com.guava.judelaw.common.utils.EncryptUtil;
import com.guava.judelaw.user.model.UserEntity;
import com.guava.judelaw.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;

    public UserEntity getByCredentials(final String userId, final String password) {
        return userRepository.findByUserIdAndPassword(userId, EncryptUtil.encryptPassword(password));
    }
}
