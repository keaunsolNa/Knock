package org.zerock.knock.dto.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "Not Login User"),
    USER("ROLE_USER", "Regular User"),
    ADMIN("ROLE_ADMIN", "Administrator"),
    INVALID("ROLE_INVALID", "User Who Banned");

    private final String key;
    private final String title;
}