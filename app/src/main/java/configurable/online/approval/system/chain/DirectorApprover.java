package configurable.online.approval.system.chain;

import configurable.online.approval.system.domain.models.Application;
import configurable.online.approval.system.domain.models.ExpenseApplication;

import java.math.BigDecimal;

/**
 * 部門總監簽核者
 * 負責處理需要更高層級審批的申請。
 */
public class DirectorApprover extends Approver {

    public DirectorApprover(String approverName) {
        super(approverName);
    }

    @Override
    public void handle(Application application) {
        System.out.println("--- 步驟：部門總監 Director(" + this.approverName + ") 簽核 ---");

        // 總監的審批邏輯：
        // 只有費用報銷單且金額大於等於 5000 時，總監才需要處理。
        // 這是責任鏈模式的一個變體：一個節點可以決定是否處理請求。
        if (application instanceof ExpenseApplication) {
            ExpenseApplication expenseApp = (ExpenseApplication) application;
            if (expenseApp.getAmount().compareTo(new BigDecimal("5000")) < 0) {
                System.out.println("金額小於 5000，總監無需審批，直接跳過並傳遞。");
                passToNext(application); // 直接傳遞，不處理
                return;
            }
        }
        
        // 如果需要處理，則執行簽核邏輯
        System.out.println("簽核意見：同意。");
        passToNext(application);
    }
}