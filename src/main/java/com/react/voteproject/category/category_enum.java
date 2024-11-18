package com.react.voteproject.category;


import lombok.Getter;

@Getter
public enum category_enum {

    ENTERTAINMENT,
    SPORTS,
    FASHION_BEAUTY,
    FOOD_CULINARY,
    LIFESTYLE,
    GAMING_IT,
    EDUCATION_LEARNING;
    // 카테고리 검증 함수
    public static Boolean fromCode(String code) {
        for (category_enum category : values()) {
            if (category.name().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;

    }

}
