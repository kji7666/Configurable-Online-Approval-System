package configurable.online.approval.system.state;

import configurable.online.approval.system.domain.enums.ApprovalStatus;
import configurable.online.approval.system.domain.models.Application;
import configurable.online.approval.system.chain.*;

/**
 * 草稿狀態
 * 在此狀態下，只允許「提審」或「撤銷(刪除)」。
 */
public class DraftState extends State {

    @Override
    public Result submit(Application application) {
        System.out.println("處理提審：草稿 -> 簽核中");
        application.setStatus(ApprovalStatus.PENDING);
        return new Result("0000", "提審成功，已進入簽核流程。");
    }

    @Override
    public Result withdraw(Application application) {
        System.out.println("處理撤銷：草稿 -> 已撤銷");
        application.setStatus(ApprovalStatus.WITHDRAWN);
        return new Result("0000", "草稿已成功撤銷");
    }
}