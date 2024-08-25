package com.moonkite.moonpets.config;

/**
 * @author 风筝
 */
public class BackgroundTemplate {
    private String name;
    private String code;
    private String path;
    private String backgroundImageName;
    private String foregroundImageName;

    private Integer y;

    // 构造函数
    public BackgroundTemplate() {}

    // Getter 和 Setter 方法
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBackgroundImageName() {
        return backgroundImageName;
    }

    public void setBackgroundImageName(String backgroundImageName) {
        this.backgroundImageName = backgroundImageName;
    }

    public String getForegroundImageName() {
        return foregroundImageName;
    }

    public void setForegroundImageName(String foregroundImageName) {
        this.foregroundImageName = foregroundImageName;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "BackgroundTemplate{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", path='" + path + '\'' +
                ", backgroundImageName='" + backgroundImageName + '\'' +
                ", foregroundImageName='" + foregroundImageName + '\'' +
                '}';
    }
}
