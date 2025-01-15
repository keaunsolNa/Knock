package org.zerock.knock.dto.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER", "Regular User"),
    ADMIN("ROLE_ADMIN", "Administrator");

    private final String key;
    private final String title;
}