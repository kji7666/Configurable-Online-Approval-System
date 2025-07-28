package configurable.online.approval.system.chain;

import configurable.online.approval.system.domain.models.Application;

/**
 * 直屬主管簽核者
 * 這是簽核鏈的第一個節點。
 */
public class ManagerApprover extends Approver {

    public ManagerApprover(String approverName) {
        super(approverName);
    }

    @Override
    public void handle(Application application) {
        System.out.println("--- 步驟：直屬主管 Manager(" + this.approverName + ") 簽核 ---");

        // 主管的審批邏輯：
        // 在這個範例中，我們假設主管總是會同意，然後將請求傳遞給下一位。
        // 在真實場景中，這裡可以有複雜的判斷，例如判斷申請是否合理。
        // 如果主管在此處就駁回了，可以不呼叫 passToNext()，直接更新狀態為 REJECTED。

        System.out.println("簽核意見：同意。");

        // 將請求傳遞給鏈的下一個節點
        passToNext(application);

        // **注意**：事件的發布通常是在流程的終點。
        // 如果主管就是最後一關，那麼 passToNext() 內部就會發布 APPROVE 事件。
        // 如果主管駁回了，我們可以在這裡發布 REJECT 事件。
        /*
        if (some_condition_for_rejection) {
            Result rejectResult = this.stateHandler.reject(application);
            if ("0000".equals(rejectResult.getCode())) {
                this.eventManager.notify(EventType.REJECT, application);
            }
            return; // 中斷流程
        }
        */
    }
}