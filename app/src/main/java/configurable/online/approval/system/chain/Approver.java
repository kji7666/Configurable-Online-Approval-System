package configurable.online.approval.system.chain;

import configurable.online.approval.system.domain.models.Application;
import configurable.online.approval.system.observer.EventManager;
import configurable.online.approval.system.observer.EventType;
import configurable.online.approval.system.state.Result;
import configurable.online.approval.system.state.StateHandler;

/**
 * 抽象簽核者 (Handler)
 * 定義了所有簽核節點的共通結構與行為。
 */
public abstract class Approver {

    protected String approverName;
    protected Approver next; // 指向下一個責任鏈節點

    // 我們讓每個簽核者都持有一個狀態處理器的引用，以便在簽核後更新申請單狀態
    protected StateHandler stateHandler;
    protected EventManager eventManager;


    public Approver(String approverName) {
        this.approverName = approverName;
    }

    // 新增一個方法，用於注入依賴
    public void setDependencies(StateHandler stateHandler, EventManager eventManager) {
        this.stateHandler = stateHandler;
        this.eventManager = eventManager;
    }

    /**
     * 設定下一個簽核者，並返回下一個簽核者以便進行鏈式呼叫
     * @param next 下一個簽核者
     * @return next
     */
    public Approver setNext(Approver next) {
        this.next = next;
        return this.next;
    }
    public Approver getNext() {
        return this.next;
    }
    
    /**
     * 處理申請單的核心方法 (模板方法)
     * @param application 待處理的申請單
     */
    public abstract void handle(Application application);

    /**
     * 一個輔助方法，用於將請求傳遞給鏈上的下一個節點
     * @param application 申請單
     */
    protected void passToNext(Application application) {
        if (next != null) {
            System.out.println("-> 請求傳遞給下一位簽核者: " + next.approverName);
            next.handle(application);
        } else {
            // 如果沒有下一個節點，表示這是鏈的末端，流程已走完
            System.out.println("簽核鏈結束。最終簽核通過。");
            // 在鏈的末端，我們將狀態設定為 "已核准"
            Result finalResult = this.stateHandler.approve(application);
            System.out.println("最終狀態更新結果: " + finalResult);
            // 在鏈的末端發布核准事件
            if ("0000".equals(finalResult.getCode())) {
                this.eventManager.notify(EventType.APPROVE, application);
            }
        }
    }
}