package com.moonkite.moonpets.model;

import javax.swing.*;

/**
 * @author 风筝
 * @date 2024/8/17 17:34
 */
public class AnimationProperty {

    private ImageIcon actionImage;

    private AnimalAction action;

    private String color;

    private String name;

    public ImageIcon getActionImage() {
        return actionImage;
    }

    public void setActionImage(ImageIcon actionImage) {
        this.actionImage = actionImage;
    }

    public AnimalAction getAction() {
        return action;
    }

    public void setAction(AnimalAction action) {
        this.action = action;
    }

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
}
