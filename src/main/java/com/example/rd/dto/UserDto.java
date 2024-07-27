package com.example.rd.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;
    private String nickName;
    private String icon;

    public UserDto() {
    }

    public UserDto(Long id, String nickName, String icon) {
        this.id = id;
        this.nickName = nickName;
        this.icon = icon;
    }
}
