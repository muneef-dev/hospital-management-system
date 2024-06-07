package com.moha.hms.entity.enums;

public enum Gender {
    MALE,FEMALE;

    public static Gender fromString(String genderStr) {
        for (Gender gender : values()) {
            if (gender.name().equalsIgnoreCase(genderStr)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender: " + genderStr);
    }
}
