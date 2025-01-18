package org.zerock.knock.dto.dto.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.zerock.knock.dto.Enum.Role;

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
