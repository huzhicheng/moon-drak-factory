package com.moonkite.moonpets.config;

/**
 * @author 风筝
 * @date 2024/8/19 17:58
 */
public class PetUserBackgroundConfig {

    /**
     * 是否是系统背景，如果为 false，表示是自定义背景
     */
    private Boolean isSystem;

    /**
     * 如果是系统背景，需要backgroundCode
     */
    private String backgroundCode;

    /**
     * 自定义背景图
     */
    private String backgroundImagePath;

    /**
     * 自定义前景图
     */
    private String foregroundImagePath;

    public String getBackgroundCode() {
        return backgroundCode;
    }

    public void setBackgroundCode(String backgroundCode) {
        this.backgroundCode = backgroundCode;
    }

    public Boolean getSystem() {
        return isSystem;
    }

    public void setSystem(Boolean system) {
        isSystem = system;
    }

    public String getBackgroundImagePath() {
        return backgroundImagePath;
    }

    public void setBackgroundImagePath(String backgroundImagePath) {
        this.backgroundImagePath = backgroundImagePath;
    }

    public String getForegroundImagePath() {
        return foregroundImagePath;
    }

    public void setForegroundImagePath(String foregroundImagePath) {
        this.foregroundImagePath = foregroundImagePath;
    }
}
