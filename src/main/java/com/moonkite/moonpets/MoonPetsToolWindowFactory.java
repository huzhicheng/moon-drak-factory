package com.moonkite.moonpets;

/**
 * @author 风筝
 * @date 2024/8/17 10:21
 */
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MoonPetsToolWindowFactory implements ToolWindowFactory {
    private MessageBusConnection connection;
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        MoonPetsToolWindow moonPetsToolWindow = new MoonPetsToolWindow(project,toolWindow);
        Content content = ContentFactory.getInstance().createContent(moonPetsToolWindow.getContent(), "黑工厂", false);
        toolWindow.getContentManager().addContent(content);

//        connection = project.getMessageBus().connect();
//        connection.subscribe(ToolWindowManagerListener.TOPIC, new ToolWindowManagerListener() {
//            @Override
//            public void stateChanged(@NotNull ToolWindowManager toolWindowManager) {
//                ToolWindow window = toolWindowManager.getToolWindow("DarkFactory"); // 确保这里使用你的 ToolWindow ID
//                if (window != null) {
//                    if (window.isVisible()) {
//                        moonPetsToolWindow.startRendering();
//                    } else {
//                        moonPetsToolWindow.stopRendering();
//                    }
//                }
//            }
//        });

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
