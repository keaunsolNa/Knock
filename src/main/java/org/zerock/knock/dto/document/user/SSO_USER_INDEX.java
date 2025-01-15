package org.zerock.knock.dto.document.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.zerock.knock.dto.Enum.Role;

@Getter
@NoArgsConstructor
@Document(indexName = "sso-user-index")
public class SSO_USER_INDEX {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING) // Enum 값을 어떤 형태로 정할지 결정, default = int
    @Column(nullable = false)
    private Role role;

    @Builder
    public SSO_USER_INDEX(String name, String email, String picture, Role role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public SSO_USER_INDEX update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
