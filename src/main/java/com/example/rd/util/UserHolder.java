package com.example.rd.util;

import com.example.rd.dto.UserDto;
import com.example.rd.entity.User;

public class UserHolder {
    private static final ThreadLocal<UserDto> tl = new ThreadLocal<>();

    public static void saveUser(UserDto user) {
        tl.set(user);
    }

    public static UserDto getUser() {
        return tl.get();
    }

    public static void removeUser() {
        tl.remove();
    }
}
