package com.moonkite.moonpets;

import com.moonkite.moonpets.config.*;
import com.moonkite.moonpets.model.CustomGif;
import com.moonkite.moonpets.model.FlyAnimal;
import com.moonkite.moonpets.model.LandAnimal;
import com.moonkite.moonpets.model.PetColor;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

public class CombinedPanel extends JPanel implements ActionListener {
    private List<BufferedImage> backgroundImageList;

    private Image backgroundImage;

    private Image foregroundImage;

    /**
     * 陆地动物
     */
    private List<LandAnimal> landAnimals;

    private List<FlyAnimal> flyAnimals;

    private List<CustomGif> customGifs;
    private Timer timer;

    private final Project project;
    private final MoonPetsState state;
    private final List<AnimalTemplate> animalTemplates;

    private final List<BackgroundTemplate> backgroundTemplates;

    // 保存当前背景，用来获取y轴坐标
    private BackgroundTemplate currentBackgroundTemplate;

    /**
     * 是否是系统背景
     */
    private Boolean isSystemBackground;

    public static final String TRANSPARENT_FOREGROUND = "/images/backgrounds/transparent_foreground.png";


    private static final Random RANDOM = new Random();

    private boolean isRendering = true;

    public CombinedPanel(Project project, List<BackgroundTemplate> backgroundTemplates, List<AnimalTemplate> animalTemplates) {
        setLayout(new BorderLayout());
        setOpaque(false);
        this.project = project;
        this.state = project.getService(MoonPetsState.class);

        this.animalTemplates = animalTemplates;
        this.backgroundTemplates = backgroundTemplates;

        // 动物初始化
        landAnimals = new ArrayList<>();
        flyAnimals = new ArrayList<>();
        customGifs = new ArrayList<>();
        timer = new Timer(100, this);
        timer.start();

        loadBackgroundImageFromState();

        //addComponentListener(new java.awt.event.ComponentAdapter() {
           // public void componentResized(java.awt.event.ComponentEvent evt) {
               // loadAnimalsFromState();
                initialLoadAnimalsFromState();
          //  }
        //});
    }

    public void startRendering() {
        if (!isRendering) {
            isRendering = true;
            if (timer != null) {
                timer.start();
            }
            // Add any other logic needed to resume rendering
        }
    }

    public void stopRendering() {
        if (isRendering) {
            isRendering = false;
            if (timer != null) {
                timer.stop();
            }
        }
    }


    public void initialLoadAnimalsFromState() {
        landAnimals.clear();
        flyAnimals.clear();
        customGifs.clear();
        List<PetUserConfig> animals = state.getAnimals();
        if (animals != null && !animals.isEmpty()) {
            for (PetUserConfig petConfig : animals) {
                addAnimalFromConfig(petConfig);
            }
        }
        repaint(); // 重绘面板以显示初始加载的所有动物
    }

    public void addNewAnimal(PetUserConfig petConfig) {
        addAnimalFromConfig(petConfig);
        repaint(); // 只重绘以显示新添加的动物
    }

    private void addAnimalFromConfig(PetUserConfig petConfig) {
        if (petConfig.getCode() != null) {
            AnimalTemplate template = findTemplateByCode(petConfig.getCode());
            if (template != null) {
                PetColor petColor = PetColor.getByCode(petConfig.getColor());
                int y = currentBackgroundTemplate == null ? 0 : currentBackgroundTemplate.getY();
                if ("land".equals(template.getAnimalType())) {
                    landAnimals.add(new LandAnimal(petColor, template.getImagePath(), template.getActionList(), RANDOM.nextInt(10, 60), y));
                } else if ("fly".equals(template.getAnimalType())) {
                    flyAnimals.add(new FlyAnimal(petColor, template.getImagePath(), template.getActionList(), RANDOM.nextInt(10, 60), y));
                }
            }
        } else {
            // 自定义的图片
            if(!petConfig.getSystem()) {
                customGifs.add(new CustomGif(petConfig.getImagePath(), RANDOM.nextInt(60, 100), 0, petConfig.getMovable()));
            }
        }
    }

    private AnimalTemplate findTemplateByCode(String code) {
        return animalTemplates.stream()
                .filter(template -> template.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }


    /**
     * 动态切换背景
     *
     * @param backgroundConfig
     */
    public void updateBackground(PetUserBackgroundConfig backgroundConfig) {
        if (backgroundConfig.getSystem()) {
            String backgroundCode = backgroundConfig.getBackgroundCode();
            BackgroundTemplate backgroundTemplate = findBackgroundTemplateByCode(backgroundCode);
            if (backgroundTemplate != null) {
                String path = backgroundTemplate.getPath();
                this.isSystemBackground = true;
                if(backgroundTemplate.getForegroundImageName()==null) {
                    //只有背景图的情况
                    loadBackgroundImage(path + backgroundTemplate.getBackgroundImageName(),
                            TRANSPARENT_FOREGROUND, true);
                }else {
                    loadBackgroundImage(path + backgroundTemplate.getBackgroundImageName(),
                            path + backgroundTemplate.getForegroundImageName(), true);
                }
                this.currentBackgroundTemplate = backgroundTemplate;

                adjustAnimalPositions();
                repaint();
            }
        } else {
            // 加载用户自定义背景
            this.isSystemBackground = false;
            loadBackgroundImage(backgroundConfig.getBackgroundImagePath(), backgroundConfig.getForegroundImagePath(), false);
            adjustAnimalPositionsToBottom();
            repaint();
        }
    }

    private void adjustAnimalPositions() {
        int newLandY = this.currentBackgroundTemplate.getY();

        for (LandAnimal animal : landAnimals) {
            animal.setY(newLandY);
        }

        for (FlyAnimal animal : flyAnimals) {
            // 对于飞行动物，我们可能需要一个不同的逻辑
            // 这里简单地将它们设置到一个稍高的位置
            animal.setY(newLandY + animal.getY());
        }
    }

    private void adjustAnimalPositionsToBottom() {

        for (LandAnimal animal : landAnimals) {
            animal.setY(0);
        }

        for (FlyAnimal animal : flyAnimals) {
            // 对于飞行动物，我们可能需要一个不同的逻辑
            // 这里简单地将它们设置到一个稍高的位置
            animal.setY( animal.getY());
        }
    }

    /**
     * 加载背景
     */
    private void loadBackgroundImageFromState() {
        PetUserBackgroundConfig backgroundConfig = state.getBackgroundConfig();
        BackgroundTemplate backgroundTemplate;
        if (backgroundConfig != null) {
            if (backgroundConfig.getBackgroundCode() != null) {
                // 从用户配置中加载
                backgroundTemplate = findBackgroundTemplateByCode(backgroundConfig.getBackgroundCode());
                this.isSystemBackground = true;
                String path = backgroundTemplate.getPath();
                if(backgroundTemplate.getForegroundImageName()==null) {
                    //只有背景图的情况
                    loadBackgroundImage(path + backgroundTemplate.getBackgroundImageName(),
                            TRANSPARENT_FOREGROUND, true);
                }else {
                    loadBackgroundImage(path + backgroundTemplate.getBackgroundImageName(),
                            path + backgroundTemplate.getForegroundImageName(), true);
                }
                this.currentBackgroundTemplate = backgroundTemplate;

            } else if (backgroundConfig.getSystem() != null && !backgroundConfig.getSystem()) {
                this.isSystemBackground = false;
                loadBackgroundImage(backgroundConfig.getBackgroundImagePath(), backgroundConfig.getForegroundImagePath(), false);
            }else {
                randLoadBackground();
            }
        } else {
            randLoadBackground();
        }

    }

    private void randLoadBackground() {
        BackgroundTemplate backgroundTemplate;
        int i = RANDOM.nextInt(0, this.backgroundTemplates.size());
        backgroundTemplate = this.backgroundTemplates.get(i);
        String path = backgroundTemplate.getPath();
        this.isSystemBackground = true;
        loadBackgroundImage(path + backgroundTemplate.getBackgroundImageName(),
                path + backgroundTemplate.getForegroundImageName(), true);

        this.currentBackgroundTemplate = backgroundTemplate;
    }

    private BackgroundTemplate findBackgroundTemplateByCode(String code) {
        return this.backgroundTemplates.stream()
                .filter(bg -> bg.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    private void loadBackgroundImage(String backgroundImagePath, String foregroundImagePath, Boolean isSystem) {
        backgroundImage = loadBufferedImage(backgroundImagePath, isSystem);
        if (backgroundImage == null) {
            backgroundImage = loadBufferedImage(TRANSPARENT_FOREGROUND, true);
        }

        if (foregroundImagePath != null) {
            foregroundImage = loadBufferedImage(foregroundImagePath, isSystem);
        } else {
            foregroundImage = loadBufferedImage(TRANSPARENT_FOREGROUND, true);
        }
    }

    private Image loadBufferedImage(String path, Boolean isSystem) {
        try {
            if (!isSystem) {
                // 从本地文件系统加载
                File file = new File(path);
                if (!file.exists()) {
                    System.err.println("找不到本地图片文件: " + path);
                    return null;
                }
                // 使用 ImageIcon 来支持 GIF 动画
                ImageIcon icon = new ImageIcon(path);
                return icon.getImage();
            } else {
                // 从资源加载
                URL imageURL = getClass().getResource(path);
                if (imageURL == null) {
                    System.err.println("找不到资源图片: " + path);
                    return null;
                }
                // 使用 ImageIcon 来支持 GIF 动画
                ImageIcon icon = new ImageIcon(imageURL);
                return icon.getImage();
            }
        } catch (Exception e) {
            System.err.println("加载图片时出错: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // 1. 先绘制背景图
        drawBackground(g2d, backgroundImage);
        // 2. 绘制动物
        g2d.translate(0, getHeight());
        g2d.scale(1, -1);

        for (LandAnimal animal : landAnimals) {
            animal.draw(g2d);
        }

        for (FlyAnimal animal : flyAnimals) {
            animal.draw(g2d);
        }

        for (CustomGif customGif : customGifs) {
            customGif.draw(g2d);
        }

        // 3. 绘制前景图
        g2d.translate(0, getHeight());
        g2d.scale(1, -1);
        drawBackground(g2d, foregroundImage);
        g2d.dispose();
    }

    private void drawBackground(Graphics2D g2d, Image image) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imgWidth = image.getWidth(null);
        int imgHeight = image.getHeight(null);

        int scaledWidth, scaledHeight;
        int yOffset;

        // 判断图片是否需要缩放
        if (imgHeight > 700) {
            // 计算缩放比例，以面板高度的一定比例为基准
            double scale = (double) (panelHeight * 0.8) / imgHeight; // 使用面板高度的80%作为基准
            scaledHeight = (int) (imgHeight * scale);
            scaledWidth = (int) (imgWidth * scale);
        } else {
            // 对于小图片，使用原始尺寸
            scaledWidth = imgWidth;
            scaledHeight = imgHeight;
        }

        // 计算需要绘制的次数以覆盖整个面板宽度
        int repetitions = (int) Math.ceil((double) panelWidth / scaledWidth);

        // 计算垂直方向的偏移量，使图像固定在底部
        yOffset = panelHeight - scaledHeight;

        // 使用 RenderingHints 来确保更好的绘制质量
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 平铺图像
        for (int i = 0; i < repetitions; i++) {
            g2d.drawImage(image, i * scaledWidth, yOffset, scaledWidth, scaledHeight, null);
        }

        // 填充顶部可能的空白区域
        if (yOffset > 0) {
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, panelWidth, yOffset);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (LandAnimal animal : landAnimals) {
            animal.move(getWidth(), getHeight());
        }

        for (FlyAnimal animal : flyAnimals) {
            animal.move(getWidth(), getHeight());
        }

        for (CustomGif gif : customGifs) {
            gif.move(getWidth(), getHeight());
        }
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(585, 500);
    }
}