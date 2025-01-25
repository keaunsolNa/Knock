package org.zerock.knock.dto.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum JwtRule {

    TYPE("type"),
    RESOURCE_ACCESS("resource_access"),
    ACCOUNT("account"),
    ROLES("roles"),
    ROLE_PREFIX("ROLE_");

    private final String value;
}
