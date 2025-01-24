package org.zerock.knock.dto.document.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.zerock.knock.dto.Enum.AlarmTiming;
import org.zerock.knock.dto.Enum.CategoryLevelOne;
import org.zerock.knock.dto.Enum.Role;
import org.zerock.knock.dto.Enum.SocialLoginType;

import java.sql.Timestamp;
import java.util.Date;

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
//    @Column(nullable = false)
    //TODO : KAKAO 비즈 앱 동의 이후 nullable = false
    private String email;

    @Field(type = FieldType.Text, analyzer = "nori", fielddata = true)
    @Column(nullable = false)
    private String nickName;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialLoginType loginType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private CategoryLevelOne favoriteLevelOne;

    @Enumerated(EnumType.STRING)
    private AlarmTiming[] alarmTimings;


    @CurrentTimestamp
    @Column(nullable = false)
    private Date lastLoginTime;

    @Builder
    public SSO_USER_INDEX(String id, String name, String email, String nickName, String picture, SocialLoginType loginType, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nickName = nickName;
        this.picture = picture;
        this.loginType = loginType;
        this.role = role;
        this.favoriteLevelOne = CategoryLevelOne.MOVIE;
        this.alarmTimings = new AlarmTiming[] { AlarmTiming.ZERO_HOUR, AlarmTiming.ZERO_HOUR, AlarmTiming.ZERO_HOUR, AlarmTiming.ZERO_HOUR };
        this.lastLoginTime = new Timestamp(System.currentTimeMillis());
    }

    public SSO_USER_INDEX update(String name, String email, String picture) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.lastLoginTime = new Timestamp(System.currentTimeMillis());

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
