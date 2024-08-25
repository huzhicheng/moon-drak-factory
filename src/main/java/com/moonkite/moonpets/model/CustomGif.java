package com.moonkite.moonpets.model;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CustomGif {
    private int x, y;
    private int dx, dy;
    private ImageIcon currentActionImage;

    private boolean isFlipped = false;

    private List<String> actionList = new ArrayList<>();

    private String imagePath;

    private Boolean movable;

    private static final Random RANDOM = new Random();

    private ScheduledExecutorService scheduler;

    public CustomGif(String imagePath, int x, int y,Boolean movable) {
        this.x = x;
        this.y = y;
        this.dx = 0;
        this.dy = 0;
        this.actionList = actionList;
        this.imagePath = imagePath;
        this.movable = movable;

        this.actionList.add(AnimalAction.IDEL.getCode());
        if(movable) {
            this.actionList.add(AnimalAction.WALK.getCode());
        }

        try {
            ImageIcon imageIcon = loadGif(imagePath);
            if (imageIcon != null) {

                scheduler = Executors.newSingleThreadScheduledExecutor();
                scheduleNextAction();
            }
        }catch (Exception e) {

        }
    }

    private ImageIcon loadGif(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("File does not exist: " + path);
            return null;
        }

        ImageIcon icon = new ImageIcon(path);
        this.currentActionImage = icon;
        return icon;
    }
    private void scheduleNextAction() {
        int delay = RANDOM.nextInt(3000) + 1000; // 随机延迟1-4秒
        scheduler.schedule(() -> {
            loopPlay();
            scheduleNextAction(); // 递归调用以继续循环
        }, delay, TimeUnit.MILLISECONDS);
    }
    private void loopPlay() {

        int speed = 0;
        if(this.movable) {
            int i = RANDOM.nextInt(0, 2);
            if(i==0) {
                speed = AnimalAction.WALK.getSpeed();
            }
        }

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


    public void move(int panelWidth, int panelHeight) {
        x += dx;
        y += dy;

        // 注意：y 坐标现在是从底部开始计算的
        if (x <= 0 || x + currentActionImage.getIconWidth() >= panelWidth) {
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