package com.azry.lms.dto.response;

import com.azry.lms.entity.User;
import com.azry.lms.util.MappingUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;

    public static UserResponse ofEntity(User user) {
        return MappingUtil.map(user, UserResponse.class);
    }
}
