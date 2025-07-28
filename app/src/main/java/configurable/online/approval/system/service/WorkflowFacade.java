package configurable.online.approval.system.service;

import configurable.online.approval.system.chain.Approver;
import configurable.online.approval.system.domain.enums.ApplicationType;
import configurable.online.approval.system.domain.models.Application;
import configurable.online.approval.system.domain.models.ExpenseApplication;
import configurable.online.approval.system.domain.models.User;
import configurable.online.approval.system.factory.ExpenseApplicationFactory;
import configurable.online.approval.system.factory.LeaveApplicationFactory;
import configurable.online.approval.system.factory.ProcurementApplicationFactory;
import configurable.online.approval.system.factory.IApplicationFactory;
import configurable.online.approval.system.observer.EmailNotifier;
import configurable.online.approval.system.observer.EventManager;
import configurable.online.approval.system.observer.EventType;
import configurable.online.approval.system.observer.HistoryLogger;
import configurable.online.approval.system.state.Result;
import configurable.online.approval.system.state.StateHandler;
import configurable.online.approval.system.strategy.HighAmountStrategy;
import configurable.online.approval.system.strategy.IRoutingStrategy;
import configurable.online.approval.system.strategy.LowAmountStrategy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 工作流外觀類 (Facade) - 整合版本
 * 封裝了申請單創建、流程選擇、狀態轉換、依賴注入和簽核啟動等一系列複雜操作，
 * 為客戶端提供一個簡單統一的介面。
 */
public class WorkflowFacade {

    // 持有所有需要的子系統核心服務
    private final Map<ApplicationType, IApplicationFactory> factories;
    private final StateHandler stateHandler;
    private final EventManager eventManager;

    public WorkflowFacade() {
        // 1. 初始化核心服務
        this.stateHandler = new StateHandler();
        
        // 建立並設定 EventManager，告訴它我們關心哪些事件
        this.eventManager = new EventManager(
            EventType.SUBMIT, 
            EventType.APPROVE, 
            EventType.REJECT
        );

        // 2. 註冊所有觀察者！
        // 這是解耦的關鍵：Facade 負責組裝，但不需要知道觀察者具體做了什麼。
        System.out.println("【系統初始化】: 正在註冊觀察者...");
        this.eventManager.subscribe(EventType.SUBMIT, new HistoryLogger());
        this.eventManager.subscribe(EventType.APPROVE, new HistoryLogger());
        this.eventManager.subscribe(EventType.APPROVE, new EmailNotifier());
        this.eventManager.subscribe(EventType.REJECT, new HistoryLogger());
        this.eventManager.subscribe(EventType.REJECT, new EmailNotifier());

        // 3. 初始化工廠
        this.factories = new HashMap<>();
        this.factories.put(ApplicationType.EXPENSE, new ExpenseApplicationFactory());
        this.factories.put(ApplicationType.LEAVE, new LeaveApplicationFactory());
        this.factories.put(ApplicationType.PROCUREMENT, new ProcurementApplicationFactory());
    }

    /**
     * 提交一個新的費用報銷申請 (核心業務方法)
     *
     * @param applicant 申請人
     * @param amount 金額
     * @param reason 事由
     * @return 處理結果
     */
    public Result submitExpenseApplication(User applicant, BigDecimal amount, String reason) {
        System.out.println("\n================== 開始處理一筆新的費用報銷申請 ==================");
        System.out.println("申請人: " + applicant.getUserName() + ", 金額: " + amount);

        // 步驟 1: 使用工廠創建申請單物件
        IApplicationFactory factory = factories.get(ApplicationType.EXPENSE);
        if (factory == null) {
            return new Result("9999", "系統錯誤：找不到對應的申請單工廠。");
        }
        ExpenseApplication application = (ExpenseApplication) factory.create(applicant, amount, reason);
        System.out.println("【步驟 1/5】工廠已成功建立申請單，ID: " + application.getApplicationId());

        // 步驟 2: 根據業務規則選擇路由策略
        IRoutingStrategy strategy;
        if (amount.compareTo(new BigDecimal("5000")) >= 0) {
            strategy = new HighAmountStrategy();
        } else {
            strategy = new LowAmountStrategy();
        }
        System.out.println("【步驟 2/5】策略模式已選擇流程: " + strategy.getClass().getSimpleName());

        // 步驟 3: 使用策略獲取對應的簽核責任鏈，並為鏈注入依賴
        Approver chainHead = strategy.getRoutingChain(application);
        injectDependenciesToChain(chainHead); // 注入 stateHandler 和 eventManager
        application.setChainHead(chainHead);
        System.out.println("【步驟 3/5】責任鏈已組裝完畢並注入依賴。");

        // 步驟 4: 使用狀態處理器將申請單狀態從 DRAFT 轉為 PENDING
        Result submitResult = stateHandler.submit(application);
        if (!"0000".equals(submitResult.getCode())) {
            return submitResult; // 如果提審失敗（例如狀態不對），直接返回結果
        }
        System.out.println("【步驟 4/5】狀態模式已成功將申請單狀態變更為 PENDING。");
        
        // 提審成功後，發布 SUBMIT 事件
        this.eventManager.notify(EventType.SUBMIT, application);

        // 步驟 5: 啟動責任鏈，開始真正的簽核流程
        System.out.println("\n--- 啟動簽核流程 ---");
        Objects.requireNonNull(application.getChainHead()).handle(application);
        System.out.println("--- 簽核流程結束 ---\n");

        return new Result("0000", "申請單 " + application.getApplicationId() + " 已成功提交並完成處理。最終狀態: " + application.getStatus());
    }

    /**
     * 輔助方法：遞迴地為責任鏈上的所有節點注入依賴。
     * 這樣每個 Approver 節點就都能夠使用 StateHandler 和 EventManager 了。
     * @param approver 鏈的當前節點
     */
    private void injectDependenciesToChain(Approver approver) {
        if (approver == null) return;
        
        // 將核心服務注入到當前節點
        approver.setDependencies(this.stateHandler, this.eventManager);
        
        // 遞迴呼叫，為鏈上的下一個節點也注入依賴
        // (前提是 Approver 類別中需要提供 getNext() 方法)
        injectDependenciesToChain(approver.getNext());
    }
}