package com.moonkite.moonpets.config;

import java.util.List;

/**
 * @author 风筝
 * @date 2024/8/18 16:32
 */
public class AnimalTemplate {

    /**
     * 动物类型：land or fly 陆地动物和会飞的动物
     */
    private String animalType;

    /**
     * 动物所具备的动作集合
     */
    private List<String> actionList;

    /**
     * 颜色集合
     */
    private List<String> colorList;

    /**
     * 动物名称
     */
    private String animalName;

    /**
     * code
     */
    private String code;

    /**
     * 图片所在路径
     */
    private String imagePath;

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public List<String> getActionList() {
        return actionList;
    }

    public void setActionList(List<String> actionList) {
        this.actionList = actionList;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<String> getColorList() {
        return colorList;
    }

    public void setColorList(List<String> colorList) {
        this.colorList = colorList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
