package org.zerock.knock.dto.document.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.zerock.knock.dto.Enum.Role;
import org.zerock.knock.dto.Enum.SocialLoginType;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Document(indexName = "sso-user-index")
public class SSO_USER_INDEX {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "nori", fielddata = true)
    @Column(nullable = false)
    private String name;

    @Field(type = FieldType.Text, analyzer = "nori", fielddata = true)
    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialLoginType loginType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public SSO_USER_INDEX(String id, String name, String email, String picture, SocialLoginType loginType, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.loginType = loginType;
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
