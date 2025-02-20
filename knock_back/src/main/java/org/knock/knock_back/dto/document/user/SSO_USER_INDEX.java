package org.knock.knock_back.dto.document.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.knock.knock_back.dto.Enum.AlarmTiming;
import org.knock.knock_back.dto.Enum.CategoryLevelOne;
import org.knock.knock_back.dto.Enum.Role;
import org.knock.knock_back.dto.Enum.SocialLoginType;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Document(indexName = "sso-user-index")
public class SSO_USER_INDEX {

    @Id
    @Enumerated(EnumType.STRING)
    private String id;

    @Field(type = FieldType.Text, analyzer = "nori", fielddata = true)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String name;

    @Field(type = FieldType.Text, analyzer = "nori", fielddata = true)
    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
    //TODO : KAKAO 비즈 앱 동의 이후 nullable = false
    private String email;

    @Field(type = FieldType.Text, analyzer = "nori", fielddata = true)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String nickName;

    @Field(type = FieldType.Text)
    @Column
    @Enumerated(EnumType.STRING)
    private String picture;

    @Field(type = FieldType.Text)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialLoginType loginType;

    @Field(type = FieldType.Text)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Field(type = FieldType.Text)
    @Enumerated(EnumType.STRING)
    private CategoryLevelOne favoriteLevelOne;

    @Field(type = FieldType.Text)
    @Enumerated(EnumType.STRING)
    private AlarmTiming[] alarmTimings;

    @CurrentTimestamp
    @Column(nullable = false)
    private Date lastLoginTime;

    @Field(type = FieldType.Text)
    @Enumerated(EnumType.STRING)
    private Map<CategoryLevelOne, LinkedList<String>> subscribeList;

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
        this.subscribeList = new HashMap<>();
        subscribeList.put(CategoryLevelOne.MOVIE, new LinkedList<>());
        subscribeList.put(CategoryLevelOne.MUSICAL, new LinkedList<>());
        subscribeList.put(CategoryLevelOne.EXHIBITION, new LinkedList<>());
        subscribeList.put(CategoryLevelOne.OPERA, new LinkedList<>());
    }

    public SSO_USER_INDEX update(String name, String email, String picture) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.lastLoginTime = new Timestamp(System.currentTimeMillis());

        return this;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateFavoriteLevelOne(CategoryLevelOne favoriteLevelOne) {
        this.favoriteLevelOne = favoriteLevelOne;
    }

    public void updateAlarmTimings(AlarmTiming[] alarmTimings) {
        this.alarmTimings = alarmTimings;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
