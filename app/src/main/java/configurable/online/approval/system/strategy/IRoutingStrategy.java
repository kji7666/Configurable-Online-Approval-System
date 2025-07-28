package configurable.online.approval.system.strategy;

import configurable.online.approval.system.chain.Approver;
import configurable.online.approval.system.domain.models.Application;

/**
 * 路由策略介面 (Strategy)
 * 定義了如何根據一個申請單來組裝出對應的簽核責任鏈。
 */
public interface IRoutingStrategy {

    /**
     * 獲取簽核責任鏈的頭節點。
     * 每個實現此介面的策略，都代表了一種不同的簽核流程組裝規則。
     *
     * @param application 申請單物件，策略可以根據其內容(如金額、類型)決定如何組裝鏈
     * @return 組裝好的責任鏈的頭節點
     */
    Approver getRoutingChain(Application application);
}