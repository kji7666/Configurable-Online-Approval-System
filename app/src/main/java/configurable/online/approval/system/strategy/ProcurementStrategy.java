package configurable.online.approval.system.strategy;

import configurable.online.approval.system.chain.Approver;
import configurable.online.approval.system.chain.ManagerApprover;
import configurable.online.approval.system.chain.ProcurementApprover; // 假設您已建立此類別
import configurable.online.approval.system.domain.models.Application;

public class ProcurementStrategy implements IRoutingStrategy {

    @Override
    public Approver getRoutingChain(Application application) {
        System.out.println("【策略模式】: 偵測到採購申請，啟用 [主管 -> 採購部] 簽核流程。");
        Approver manager = new ManagerApprover("王經理");
        // 假設您已經建立了一個 ProcurementApprover 類別
        Approver procurementOfficer = new ProcurementApprover("採購部小張");

        manager.setNext(procurementOfficer);

        return manager;
    }
}