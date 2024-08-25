package com.moonkite.moonpets;

/**
 * @author 风筝
 * @date 2024/8/17 15:43
 */

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
import com.moonkite.moonpets.config.*;
import com.moonkite.moonpets.loader.AnimalTemplateLoader;
import com.moonkite.moonpets.model.NameCodePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MoonPetsToolWindow {
    private JPanel myToolWindowContent;

    private CombinedPanel combinedPanel;

    private Project project;
    private List<AnimalTemplate> animalTemplates;

    private List<BackgroundTemplate> backgroundTemplateList;

    private MoonPetsState state;
    private Map<String, String> animalNameToCodeMap;
    private Map<String, String> backgroundNameToCodeMap;

    private Timer timer;

    public MoonPetsToolWindow(@NotNull Project project, ToolWindow toolWindow) {
        this.project = project;
        this.state = project.getService(MoonPetsState.class);

        init(project);

        //startPeriodicUpdates();
    }

    private void startPeriodicUpdates() {
        timer = new Timer(60000, e -> {
//            stopRendering();
//            startRendering();
            init(this.project);
        });
        timer.start();
    }
    private void init(@NotNull Project project) {
        this.myToolWindowContent = new JPanel(new BorderLayout());
        animalTemplates = AnimalTemplateLoader.loadAnimalTemplateList();
        backgroundTemplateList = AnimalTemplateLoader.loadBackgroundTemplates();
        combinedPanel = new CombinedPanel(project, backgroundTemplateList, animalTemplates);
        myToolWindowContent.add(combinedPanel, BorderLayout.CENTER);

        // 强制布局更新
        myToolWindowContent.revalidate();
        myToolWindowContent.repaint();


        animalNameToCodeMap = animalTemplates.stream()
                .collect(Collectors.toMap(AnimalTemplate::getAnimalName, AnimalTemplate::getCode));
        backgroundNameToCodeMap = backgroundTemplateList.stream()
                .collect(Collectors.toMap(BackgroundTemplate::getName, BackgroundTemplate::getCode));

        System.out.println("MoonPetsToolWindow 初始化完成,组件数: " + myToolWindowContent.getComponentCount());
    }

    public void startRendering() {
        init(this.project);
       // combinedPanel.startRendering();
    }

    public void stopRendering() {
       // combinedPanel.stopRendering();
//        this.myToolWindowContent.
    }

    public AnAction createAddAnimalAction() {
        return new AnAction("添加元素", "添加新元素", AllIcons.Actions.OpenNewTab) { // 可以在这里设置图标
            @Override
            public void actionPerformed(AnActionEvent e) {
                AddAnimalDialog dialog = new AddAnimalDialog();
                dialog.show();
            }
        };
    }

    private class AddAnimalDialog extends DialogWrapper {
        private ComboBox<NameCodePair> templateCombo;
        private JLabel colorLabel;
        private ComboBox<NameCodePair> colorCombo;
        private JLabel nameLabel;
        private JBTextField nameField;
        private JLabel customImageLabel;
        private JTextField customImagePath;
        private JButton browseButton;
        private JCheckBox isMovableCheckBox;

        protected AddAnimalDialog() {
            super(project);
            init();
            setTitle("添加新元素");
        }

        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            JPanel dialogPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = JBUI.insets(5, 5, 5, 15);  // 增加右边距

            // 种类选择
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.weightx = 0.3;
            dialogPanel.add(new JLabel("种类:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.weightx = 0.7;
            List<NameCodePair> animalPairs = new ArrayList<>(animalTemplates.stream()
                    .map(animal -> new NameCodePair(animal.getAnimalName(), animal.getCode()))
                    .collect(Collectors.toList()));
            animalPairs.add(new NameCodePair("自定义元素", "custom"));
            templateCombo = new ComboBox<>(animalPairs.toArray(new NameCodePair[0]));
            templateCombo.addActionListener(e -> updateFields());
            dialogPanel.add(templateCombo, gbc);

            // 颜色选择
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.weightx = 0.3;
            colorLabel = new JLabel("选择颜色:");
            dialogPanel.add(colorLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.weightx = 0.7;
            colorCombo = new ComboBox<>();
            dialogPanel.add(colorCombo, gbc);

            // 名称输入
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            gbc.weightx = 0.3;
            nameLabel = new JLabel("设置名称:");
            dialogPanel.add(nameLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.weightx = 0.7;
            nameField = new JBTextField();
            dialogPanel.add(nameField, gbc);

            // 自定义图片路径
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 1;
            gbc.weightx = 0.3;
            customImageLabel = new JLabel("自定义图片:");
            dialogPanel.add(customImageLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 3;
            gbc.gridwidth = 1;
            gbc.weightx = 0.5;
            customImagePath = new JTextField();
            customImagePath.setEnabled(false);
            dialogPanel.add(customImagePath, gbc);

            gbc.gridx = 2;
            gbc.gridy = 3;
            gbc.gridwidth = 1;
            gbc.weightx = 0.2;
            browseButton = new JButton("浏览");
            browseButton.addActionListener(e -> browseFile(customImagePath));
            browseButton.setEnabled(false);
            dialogPanel.add(browseButton, gbc);

            // 是否移动
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 3;
            gbc.weightx = 1.0;
            isMovableCheckBox = new JCheckBox("是否移动");
            isMovableCheckBox.setEnabled(false);
            dialogPanel.add(isMovableCheckBox, gbc);

            updateFields();

            return dialogPanel;
        }


        private void updateFields() {
            NameCodePair selectedTemplate = (NameCodePair) templateCombo.getSelectedItem();
            boolean isCustom = "custom".equals(selectedTemplate.getCode());

            colorLabel.setVisible(!isCustom);
            colorCombo.setVisible(!isCustom);

            customImageLabel.setVisible(isCustom);
            customImagePath.setEnabled(isCustom);
            customImagePath.setVisible(isCustom);
            browseButton.setEnabled(isCustom);
            browseButton.setVisible(isCustom);

            isMovableCheckBox.setEnabled(isCustom);
            isMovableCheckBox.setVisible(isCustom);

            if (!isCustom) {
                updateColorCombo();
            } else {
                colorCombo.removeAllItems();
            }
        }

        private void updateColorCombo() {
            NameCodePair selectedTemplate = (NameCodePair) templateCombo.getSelectedItem();
            if (selectedTemplate != null && !"custom".equals(selectedTemplate.getCode())) {
                String selectedTemplateCode = selectedTemplate.getCode();
                AnimalTemplate template = animalTemplates.stream()
                        .filter(t -> t.getCode().equals(selectedTemplateCode))
                        .findFirst()
                        .orElse(null);

                colorCombo.removeAllItems();
                if (template != null) {
                    for (String color : template.getColorList()) {
                        colorCombo.addItem(new NameCodePair(color, color));
                    }
                }
            }
        }

        private void browseFile(JTextField textField) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        }

        @Override
        protected void doOKAction() {
            NameCodePair templatePair = (NameCodePair) templateCombo.getSelectedItem();
            String name = nameField.getText();

            PetUserConfig petUserConfig = new PetUserConfig();
            petUserConfig.setName(name);

            if ("custom".equals(templatePair.getCode())) {
                petUserConfig.setColor("default");
                petUserConfig.setImagePath(customImagePath.getText());
                petUserConfig.setMovable(isMovableCheckBox.isSelected());
                petUserConfig.setSystem(false);
            } else {
                NameCodePair colorPair = (NameCodePair) colorCombo.getSelectedItem();
                petUserConfig.setCode(templatePair.getCode());
                petUserConfig.setColor(colorPair.getCode());
                petUserConfig.setSystem(true);
            }


            addAnimalToState(petUserConfig);
            super.doOKAction();
        }
    }

    private void addAnimalToState(PetUserConfig config) {
        if (config.getSystem()) {
            state.addAnimal(config);
            combinedPanel.addNewAnimal(config);
        } else {
            List<PetUserConfig> animals = state.getAnimals();
            if (animals != null && animals.size() > 0) {
                Optional<PetUserConfig> existingConfig = animals.stream().filter(petUserConfig -> config.getImagePath().equals(petUserConfig.getImagePath())).findFirst();
                if (!existingConfig.isPresent()) {
                    // 如果没有找到相同 imagePath 的配置，添加新的配置
                    state.addAnimal(config);
                    combinedPanel.addNewAnimal(config);
                }
            } else {
                state.addAnimal(config);
                combinedPanel.addNewAnimal(config);
            }
        }
    }


    public AnAction createChangeBackgroundAction() {
        return new AnAction("更改背景", "更改背景图片", AllIcons.Actions.Edit) { // 使用编辑图标，你可以选择其他合适的图标
            @Override
            public void actionPerformed(AnActionEvent e) {
                ChangeBackgroundDialog dialog = new ChangeBackgroundDialog();
                if (dialog.showAndGet()) {
                    // 对话框确认后的逻辑
                    updateBackground(dialog.getSelectedBackground());
                }
            }
        };
    }


    /**
     * 保存并更新背景
     *
     * @param backgroundCode
     */
    private void updateBackground(String backgroundCode) {
        if (backgroundCode != null) {
            PetUserBackgroundConfig backgroundConfig = new PetUserBackgroundConfig();
            if (backgroundCode.startsWith("custom:")) {
                String[] paths = backgroundCode.substring(7).split(";");
                if (paths.length == 1) {
                    backgroundConfig.setSystem(false);
                    backgroundConfig.setBackgroundImagePath(paths[0]);
                    //backgroundConfig.setForegroundImagePath(paths[1]);
                }
            } else {
                backgroundConfig.setSystem(true);
                backgroundConfig.setBackgroundCode(backgroundCode);
            }
            state.setBackgroundConfig(backgroundConfig);
            combinedPanel.updateBackground(backgroundConfig);
        }
    }

    public AnAction createClearAnimalsAction() {
        return new AnAction("清理", "清理所有除背景外的元素", AllIcons.Actions.DeleteTag) { // 使用编辑图标，你可以选择其他合适的图标
            @Override
            public void actionPerformed(AnActionEvent e) {
                // 对话框确认后的逻辑
                clearAllAnimals();
            }
        };
    }

    private void clearAllAnimals() {
        int result = JOptionPane.showConfirmDialog(
                myToolWindowContent,
                "确定要清除所有除背景外的元素吗？此操作不可撤销。",
                "确认清除",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            state.clearAllAnimals();  // 假设 MoonPetsState 有 clearAnimals 方法
            // 如果需要，在这里更新 UI
            combinedPanel.initialLoadAnimalsFromState();
        }
    }


    private class ChangeBackgroundDialog extends DialogWrapper {
        private ComboBox<NameCodePair> backgroundCombo;
        private JTextField customBackgroundPath;
        private JTextField customForegroundPath;

        protected ChangeBackgroundDialog() {
            super(project);
            init();
            setTitle("更改背景");
        }


        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            JPanel dialogPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            dialogPanel.add(new JLabel("选择背景:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            List<NameCodePair> backgroundPairs = new ArrayList<>(backgroundTemplateList.stream()
                    .map(bg -> new NameCodePair(bg.getName(), bg.getCode()))
                    .collect(Collectors.toList()));
            backgroundPairs.add(new NameCodePair("自定义背景", "custom"));
            backgroundCombo = new ComboBox<>(backgroundPairs.toArray(new NameCodePair[0]));
            backgroundCombo.addActionListener(e -> updateCustomFields(dialogPanel));
            dialogPanel.add(backgroundCombo, gbc);

            setInitialSelection();
            return dialogPanel;
        }

        private void updateCustomFields(JPanel dialogPanel) {
            boolean isCustom = "custom".equals(((NameCodePair) backgroundCombo.getSelectedItem()).getCode());

            // 移除所有自定义背景相关的组件
            for (Component comp : dialogPanel.getComponents()) {
                if (comp instanceof JLabel && "自定义背景:".equals(((JLabel) comp).getText())) {
                    dialogPanel.remove(comp);
                }
                if (comp == customBackgroundPath || comp instanceof JButton) {
                    dialogPanel.remove(comp);
                }
            }

            if (isCustom) {
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 5, 5);

                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.gridwidth = 1;
                dialogPanel.add(new JLabel("自定义背景:"), gbc);

                gbc.gridx = 1;
                gbc.gridy = 1;
                customBackgroundPath = new JTextField();
                dialogPanel.add(customBackgroundPath, gbc);

                gbc.gridx = 2;
                gbc.gridy = 1;
                JButton browseBackgroundButton = new JButton("浏览");
                browseBackgroundButton.addActionListener(e -> browseFile(customBackgroundPath));
                dialogPanel.add(browseBackgroundButton, gbc);
            }

            dialogPanel.revalidate();
            dialogPanel.repaint();
        }

        private void setInitialSelection() {
            PetUserBackgroundConfig currentConfig = state.getBackgroundConfig();
            if (currentConfig != null) {
                if (currentConfig.getBackgroundCode() != null || (currentConfig.getSystem() == null && currentConfig.getBackgroundCode() == null)) {
                    // 系统预设背景
                    String code = currentConfig.getBackgroundCode();
                    for (int i = 0; i < backgroundCombo.getItemCount(); i++) {
                        NameCodePair item = backgroundCombo.getItemAt(i);
                        if (item.getCode().equals(code)) {
                            backgroundCombo.setSelectedIndex(i);
                            break;
                        }
                    }
                } else {
                    // 自定义背景
                    for (int i = 0; i < backgroundCombo.getItemCount(); i++) {
                        NameCodePair item = backgroundCombo.getItemAt(i);
                        if ("custom".equals(item.getCode())) {
                            backgroundCombo.setSelectedIndex(i);
                            break;
                        }
                    }
                    customBackgroundPath.setText(currentConfig.getBackgroundImagePath());
                    customBackgroundPath.setEnabled(true);
                }
            }
        }

        private void updateCustomFields() {
            boolean isCustom = "custom".equals(((NameCodePair) backgroundCombo.getSelectedItem()).getCode());
            customBackgroundPath.setEnabled(isCustom);
            //  customForegroundPath.setEnabled(isCustom);
        }

        private void browseFile(JTextField textField) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        }

        public String getSelectedBackground() {
            NameCodePair selected = (NameCodePair) backgroundCombo.getSelectedItem();
            if ("custom".equals(selected.getCode())) {
//                return "custom:" + customBackgroundPath.getText() + ";" + customForegroundPath.getText();

                // 只要背景图，降低配置难度
                return "custom:" + customBackgroundPath.getText();
            }
            return selected.getCode();
        }
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }
}
