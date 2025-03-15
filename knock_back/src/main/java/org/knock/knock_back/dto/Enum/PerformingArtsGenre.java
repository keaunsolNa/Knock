package org.knock.knock_back.dto.Enum;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nks
 * @apiNote 공연예술 장르 ENUM
 */
@Getter
public enum PerformingArtsGenre {

    THEATER("연극"),
    MUSICAL("뮤지컬"),
    CLASSICAL("클래식"),
    KOREAN_TRADITIONAL("국악"),
    POPULAR_MUSIC("대중 음악"),
    WESTERN_KOREAN_DANCE("서양, 한국무용"),
    POPULAR_DANCE("대중 무용"),
    CIRCUS_MAGIC("서커스, 마술"),
    COMPLEX("복합"),
    UNKNOWN("알 수 없음"); // 예외적인 값 처리

    private final String korean;
    private static final Map<String, PerformingArtsGenre> lookup = new HashMap<>();

    // 한글 장르명을 ENUM 값으로 매핑
    static {
        for (PerformingArtsGenre genre : PerformingArtsGenre.values()) {
            lookup.put(genre.korean, genre);
        }
    }

    PerformingArtsGenre(String korean) {
        this.korean = korean;
    }

    // 한글 장르명을 ENUM 변환하는 메서드
    public static PerformingArtsGenre fromKorean(String korean) {
        if (korean == null || korean.isBlank()) {
            return UNKNOWN;
        }
        return lookup.getOrDefault(korean, UNKNOWN); // 매칭되지 않으면 UNKNOWN 반환
    }
}
