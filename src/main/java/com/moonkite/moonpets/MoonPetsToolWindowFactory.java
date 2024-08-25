package com.moonkite.moonpets;

/**
 * @author 风筝
 * @date 2024/8/17 10:21
 */
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MoonPetsToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        MoonPetsToolWindow moonPetsToolWindow = new MoonPetsToolWindow(project,toolWindow);
        Content content = ContentFactory.getInstance().createContent(moonPetsToolWindow.getContent(), "黑工厂", false);
        toolWindow.getContentManager().addContent(content);

        if (toolWindow instanceof ToolWindowEx) {
            ToolWindowEx toolWindowEx = (ToolWindowEx) toolWindow;
            List<AnAction> actions = new ArrayList<>();
            actions.add(moonPetsToolWindow.createAddAnimalAction());
            actions.add(moonPetsToolWindow.createChangeBackgroundAction());
            actions.add(moonPetsToolWindow.createClearAnimalsAction());
            toolWindowEx.setTitleActions(actions);
        }
    }


}
