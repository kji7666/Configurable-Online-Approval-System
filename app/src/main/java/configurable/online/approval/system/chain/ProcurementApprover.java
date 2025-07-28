package configurable.online.approval.system.chain;

import configurable.online.approval.system.domain.models.Application;
import configurable.online.approval.system.domain.models.ProcurementApplication;

/**
 * 採購部專員簽核者
 * 這是專門處理採購申請的責任鏈節點。
 */
public class ProcurementApprover extends Approver {

    public ProcurementApprover(String approverName) {
        super(approverName);
    }

    @Override
    public void handle(Application application) {
        System.out.println("--- 步驟：採購部專員(" + this.approverName + ") 簽核 ---");

        // 業務邏輯：作為採購專員，只應該處理採購申請單。
        // 這是一個很好的實踐，確保了職責的清晰。
        if (!(application instanceof ProcurementApplication)) {
            System.out.println("錯誤：非採購申請單被錯誤地傳遞到了採購部。流程中止。");
            // 在真實場景中，這裡應該記錄錯誤日誌，並可能將申請單狀態變為一個錯誤狀態。
            return;
        }

        ProcurementApplication procurementApp = (ProcurementApplication) application;

        System.out.println(
            "正在審核採購品項: " + procurementApp.getItemName() +
            ", 數量: " + procurementApp.getQuantity() +
            ", 總金額: " + procurementApp.getTotalAmount()
        );
        
        // 採購部的審批邏輯：
        // 這裡可以有更複雜的判斷，例如檢查庫存、尋找供應商等。
        // 在我們的範例中，我們假設採購專員總是同意。
        System.out.println("簽核意見：同意採購。");

        // 將請求傳遞給鏈的下一個節點。
        // 由於採購專員通常是採購流程的最後一環，
        // passToNext() 內部會判斷 next 為 null，並將狀態更新為 APPROVED。
        passToNext(application);
    }
}