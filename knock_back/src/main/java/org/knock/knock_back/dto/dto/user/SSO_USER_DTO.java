package org.knock.knock_back.dto.dto.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.knock.knock_back.dto.Enum.Role;

public class SSO_USER_DTO {

    private String id;
    private String name;
    private String email;
    private String nickName;
    private String picture;
    @Enumerated(EnumType.STRING)
    private String loginType;
    @Enumerated(EnumType.STRING)
    private Role role;
}
