package configurable.online.approval.system.strategy;

import configurable.online.approval.system.chain.Approver;
import configurable.online.approval.system.chain.DirectorApprover;
import configurable.online.approval.system.chain.ManagerApprover;
import configurable.online.approval.system.domain.models.Application;

public class HighAmountStrategy implements IRoutingStrategy {

    @Override
    public Approver getRoutingChain(Application application) {
        System.out.println("【策略模式】: 偵測到高金額報銷，啟用 [主管 -> 總監] 兩級簽核流程。");
        // 規定：將主管和總監串聯起來
        Approver manager = new ManagerApprover("王經理");
        Approver director = new DirectorApprover("李總監");
        
        manager.setNext(director); // 建立鏈路
        
        return manager; // 返回鏈的頭節點
    }
}