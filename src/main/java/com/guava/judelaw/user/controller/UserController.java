package com.guava.judelaw.user.controller;

import com.guava.judelaw.common.constants.ErrorConstants;
import com.guava.judelaw.common.dto.ErrorDTO;
import com.guava.judelaw.user.dto.UserDTO;
import com.guava.judelaw.user.model.UserEntity;
import com.guava.judelaw.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        UserEntity user = userService.getByCredentials(
                userDTO.getUserId(),
                userDTO.getPassword()
        );

        if (user == null) {
            ErrorDTO errorDTO = ErrorDTO.builder().message(ErrorConstants.LOGIN_FAILED).build();
            return ResponseEntity.badRequest().body(errorDTO);
        }

        final UserDTO responseUserDTO = UserDTO.builder()
                .userId(user.getUserId())
                .licenseNo(user.getLicenseNo())
                .userName(user.getUserName())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .build();

        return ResponseEntity.ok().body(responseUserDTO);
    }
}
