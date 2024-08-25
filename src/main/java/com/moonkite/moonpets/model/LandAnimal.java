package com.moonkite.moonpets.model;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LandAnimal {
    private int x, y;
    private int dx, dy;
    private ImageIcon currentActionImage;

    private Map<String,AnimationProperty> animationPropertyMap = new HashMap<>();
    private boolean isFlipped = false;

    private List<String> actionList;

    private static final Random RANDOM = new Random();

    private ScheduledExecutorService scheduler;

    public LandAnimal(PetColor color, String imagePath, List<String> actionList, int x, int y) {
        this.x = x;
        this.y = y;
        this.dx = 0;
        this.dy = 0;
        this.actionList = actionList;

        // 加载单一颜色下的所有动作
        initActions(color, imagePath);

        int i = RANDOM.nextInt(0, actionList.size());
        String action = actionList.get(i);
        AnimationProperty animationProperty = animationPropertyMap.get(action);
        currentActionImage = animationProperty.getActionImage();
        this.dx = animationProperty.getAction().getSpeed();

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduleNextAction();
    }

    private void scheduleNextAction() {
        int delay = RANDOM.nextInt(2000) + 1000; // 随机延迟1-6秒
        scheduler.schedule(() -> {
            loopPlay();
            scheduleNextAction(); // 递归调用以继续循环
        }, delay, TimeUnit.MILLISECONDS);
    }
    private void loopPlay() {
        int i = RANDOM.nextInt(0, actionList.size());
        String action = actionList.get(i);
        AnimationProperty animationProperty = animationPropertyMap.get(action);
        Integer speed = animationProperty.getAction().getSpeed();
        currentActionImage  = animationProperty.getActionImage();
        if(isFlipped) {
            this.dx = -speed;
        }else {
            this.dx = speed;
        }
    }

    public void stopAnimation() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    private void initActions(PetColor color, String imagePath) {
        for (String actionCode:actionList) {
            AnimalAction action = AnimalAction.getByCode(actionCode);
            AnimationProperty animationProperty = new AnimationProperty();
            animationProperty.setAction(action);
            String imageFileName = String.format("%s_%s_8fps.gif", color.getValue(), action.getCode());
            ImageIcon imageIcon = new ImageIcon(getClass().getResource(imagePath + imageFileName));
            animationProperty.setActionImage(imageIcon);
            animationProperty.setColor(color.getValue());
            animationPropertyMap.put(action.getCode(),animationProperty);
        }
    }


    public void move(int panelWidth, int panelHeight) {
        x += dx;
        y += dy;

        // 注意：y 坐标现在是从底部开始计算的
        if (x <= 0) {
            dx = -dx+1;
            isFlipped = !isFlipped; // 改变翻转状态
        }
        if( x + currentActionImage.getIconWidth() >= panelWidth) {
            dx = -dx-1;
            isFlipped = !isFlipped; // 改变翻转状态
        }
        if (y <= 0 || y + currentActionImage.getIconHeight() >= panelHeight) {
            dy = -dy;
        }
    }

    public void draw(Graphics2D g2d) {
        // 保存当前的变换
        AffineTransform oldTransform = g2d.getTransform();

        // 创建一个新的变换
        AffineTransform at = new AffineTransform();

        // 先将坐标系移动到图像的位置
        at.translate(x, y);

        // 垂直翻转图像以修正上下颠倒的问题
        at.scale(1, -1);

        // 如果需要水平翻转
        if (isFlipped) {
            at.scale(-1, 1);
            at.translate(-currentActionImage.getIconWidth(), 0);
        }

        // 应用变换
        g2d.transform(at);

        // 绘制图像
        currentActionImage.paintIcon(null, g2d, 0, -currentActionImage.getIconHeight());

        // 恢复原来的变换
        g2d.setTransform(oldTransform);
    }

    // Getter 方法
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return currentActionImage.getIconWidth();
    }

    public int getHeight() {
        return currentActionImage.getIconHeight();
    }
}