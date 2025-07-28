package configurable.online.approval.system.strategy;

import configurable.online.approval.system.chain.Approver;
import configurable.online.approval.system.chain.ManagerApprover;
import configurable.online.approval.system.domain.models.Application;

public class LowAmountStrategy implements IRoutingStrategy {

    @Override
    public Approver getRoutingChain(Application application) {
        System.out.println("【策略模式】: 偵測到低金額報銷，啟用 [主管] 單級簽核流程。");
        // 規定：只建立一個主管節點，沒有 next
        return new ManagerApprover("王經理");
    }
}