package com.moonkite.moonpets.model;

/**
 * @author 风筝
 * @date 2024/8/17 17:54
 */
public enum PetColor {
    /**
     * 颜色枚举
     */
    DEFAULT("default"),
    AKITA("akita"),
    WHITE("white"),

    BROWN("brown"),
    GREEN("green"),
    GRAY("gray"),

    ORANGE("orange"),

    BLACK("black"),

    MAGICAL("magical"),

    RED("red"),

    YELLOW("yellow");

    public static PetColor getByCode(String code) {
        for (PetColor type : values()) {
            if (type.value.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未找到匹配的PetColor: " + code);
    }

    private final String value;

    PetColor(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
