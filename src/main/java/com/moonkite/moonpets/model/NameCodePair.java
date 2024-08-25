package com.moonkite.moonpets.model;

/**
 * @author 风筝
 * @date 2024/8/19 16:05
 */
public class NameCodePair {
    private final String name;
    private final String code;

    public NameCodePair(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return name;
    }
}
