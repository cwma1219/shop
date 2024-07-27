package com.example.rd.controller;

import com.example.rd.dto.Result;
import com.example.rd.dto.UserDto;
import com.example.rd.service.UserService;
import com.example.rd.util.UserHolder;
import com.example.rd.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/code")
    public Result sendCode(@RequestParam("phone") String phone) {
        return userService.sendCode(phone);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginVo vo) throws IllegalAccessException {
        return userService.login(vo);
    }

    @GetMapping("/me")
    public Result me() {
        UserDto dto = UserHolder.getUser();
        return Result.ok(dto);
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id) {

        return userService.getResultById(id);
    }
}
