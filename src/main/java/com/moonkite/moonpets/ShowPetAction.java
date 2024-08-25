package com.moonkite.moonpets;

/**
 * @author 风筝
 * @date 2024/8/18 17:56
 */
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.ui.Messages;
import com.moonkite.moonpets.config.MoonPetsState;


public class ShowPetAction extends ToggleAction {

    private MoonPetsState state;

    private PetService petService;

    @Override
    public boolean isSelected(AnActionEvent e) {
        Project project = e.getProject();
        petService = project.getService(PetService.class);
        this.state = project.getService(MoonPetsState.class);
        return this.state.getEnableOffend();
    }

    @Override
    public void setSelected(AnActionEvent e, boolean state) {
        // 在这里实现选中状态的切换
        if (state) {
            // 激活模式
            Messages.showMessageDialog(e.getProject(), "打扰模式已开启", "提示", Messages.getInformationIcon());
            this.state.setEnableOffend(true);
            petService.showPet();
        } else {
            // 取消模式
            Messages.showMessageDialog(e.getProject(), "打扰模式已关闭", "提示", Messages.getInformationIcon());
            this.state.setEnableOffend(false);
            petService.stopScheduler();
        }
    }
}
