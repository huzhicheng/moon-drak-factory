package com.moonkite.moonpets;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;
import com.moonkite.moonpets.config.MoonPetsState;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public final class PetService {
    private final Project project;
    private Timer messageTimer;

    private MoonPetsState state;
    private final Random random = new Random();

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    private final String gifFolderPath = "/images/offend/";

    private final Integer MAX_GIF_NUMBNER = 15;

    public PetService(Project project) {
        this.project = project;
        this.state = project.getService(MoonPetsState.class);
        System.out.println("state= " + this.state.getEnableOffend());
        if (this.state.getEnableOffend()) {
            showPet();
        }
    }

    public void showPet() {
        // 获取当前IDE窗口
        IdeFrame frame = WindowManager.getInstance().getIdeFrame(project);
        if (frame == null) {
            System.err.println("无法获取当前IDE窗口");
            return;
        }

        scheduleNextAction(frame);
    }

    private void scheduleNextAction(IdeFrame frame) {
        // 随机延迟1-6秒
        int delay = random.nextInt(5000) + 1000;
        scheduler.schedule(() -> {
            randomOffend(frame);
            scheduleNextAction(frame); // 递归调用以继续循环
        }, delay, TimeUnit.MILLISECONDS);
    }

    private void randomOffend(IdeFrame frame) {

        System.out.println("state= " + this.state.getEnableOffend());
        // 计算是否触发显示GIF的概率 (例如20%的概率)
        if (shouldShowGif() && this.state.getEnableOffend()) {
            try {
                String averageRandomImagePath = getAverageRandomImagePath();
                showGif(frame, averageRandomImagePath);
            } catch (Exception e) {
                System.err.println("获取随机图片失败: " + e.getMessage());
            }
        }
    }

    public boolean shouldShowGif() {
        // 例如30%的概率触发显示GIF
        return random.nextDouble() < 0.3;
    }

    private void showGif(IdeFrame frame, String gifPath) {
        // 加载gif
        URL imageURL = getClass().getResource(gifPath);
        if (imageURL == null) {
            System.err.println("找不到资源图片: " + gifPath);
            return;
        }

        // 使用 ImageIcon 来支持 GIF 动画
        ImageIcon icon = new ImageIcon(imageURL);

        Dimension size = frame.getComponent().getSize();

        int petX = random.nextInt(100, size.width - 100);
        int petY = random.nextInt(100, size.height - 100);
        System.out.println("x=" + petX + " y=" + petY);

        // 创建 JLabel 来显示 GIF
        JLabel petLabel = new JLabel(icon);
        petLabel.setBounds(petX, petY, icon.getIconWidth(), icon.getIconHeight());

        // 显示
        JRootPane rootPane = frame.getComponent().getRootPane();
        rootPane.getLayeredPane().add(petLabel, JLayeredPane.DRAG_LAYER);
        rootPane.getLayeredPane().revalidate();
        rootPane.getLayeredPane().repaint();
        // 设置计时器，在1-3秒后隐藏
        int delay = 1000 + random.nextInt(2000); // 1到3秒之间
        messageTimer = new Timer(delay, e -> hidePet(rootPane,petLabel));
        messageTimer.setRepeats(false);
        messageTimer.start();
    }

    public void hidePet(JRootPane rootPane,JLabel petLabel) {
        if (petLabel != null) {
            rootPane.getLayeredPane().remove(petLabel);
            petLabel = null;
            rootPane.getLayeredPane().revalidate();
            rootPane.getLayeredPane().repaint();

        }
    }

    public String getAverageRandomImagePath() {
        int range = MAX_GIF_NUMBNER ;

        // 平均随机数的计算方式
        double step = 1.0 / range;
        double randomValue = random.nextDouble();
        int index = (int) (randomValue / step);

        // 返回对应范围内的随机数
        int nameNumber = 1 + index;
        return gifFolderPath + nameNumber + ".gif";
    }

}

