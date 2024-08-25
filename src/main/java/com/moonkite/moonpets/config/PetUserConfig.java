package com.moonkite.moonpets.config;

/**
 * @author 风筝
 * @date 2024/8/19 14:03
 */
public class PetUserConfig {


    private String color;

    private String name;

    /**
     * code
     */
    private String code;

    /**
     * 图片所在路径
     */
    private String imagePath;

    private Boolean movable;

    private Boolean isSystem;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Boolean getMovable() {
        return movable;
    }

    public void setMovable(Boolean movable) {
        this.movable = movable;
    }

    public Boolean getSystem() {
        return isSystem;
    }

    public void setSystem(Boolean system) {
        isSystem = system;
    }
}
