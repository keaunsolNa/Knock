package org.knock.knock_back.dto.Enum;

public enum CategoryLevelOne {

    MOVIE,
    MUSICAL,
    OPERA,
    EXHIBITION,
    MY_PAGE,
    BOARD;

    public static CategoryLevelOne fromValue(String value) {
        for (CategoryLevelOne levelOne : CategoryLevelOne.values()) {
            if (levelOne.name().equalsIgnoreCase(value)) {
                return levelOne;
            }
        }
        throw new IllegalArgumentException("No enum constant " + CategoryLevelOne.class.getCanonicalName() + "." + value);
    }
}
