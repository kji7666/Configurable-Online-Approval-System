package configurable.online.approval.system.state;

import configurable.online.approval.system.domain.enums.ApprovalStatus;
import configurable.online.approval.system.domain.models.Application;

/**
 * 簽核中狀態
 * 在此狀態下，只允許「核准」或「駁回」。
 */
public class PendingState extends State {

    @Override
    public Result approve(Application application) {
        System.out.println("處理核准：簽核中 -> 已核准");
        application.setStatus(ApprovalStatus.APPROVED);
        // 這裡可以觸發通知觀察者的事件
        return new Result("0000", "核准成功");
    }

    @Override
    public Result reject(Application application) {
        System.out.println("處理駁回：簽核中 -> 已駁回");
        application.setStatus(ApprovalStatus.REJECTED);
        // 這裡可以觸發通知觀察者的事件
        return new Result("0000", "駁回成功");
    }
}