package configurable.online.approval.system.chain;

import configurable.online.approval.system.domain.enums.ApprovalStatus;
import configurable.online.approval.system.domain.models.ExpenseApplication;
import configurable.online.approval.system.domain.models.User;
import configurable.online.approval.system.observer.EventManager;
import configurable.online.approval.system.observer.EventType;
import configurable.online.approval.system.observer.MockObserver;
import configurable.online.approval.system.state.StateHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ApprovalChain 簽核責任鏈測試 (整合 Observer)")
class ApprovalChainTest {

    private User testUser;
    private Approver chain;
    private StateHandler stateHandler;
    private EventManager eventManager;
    private MockObserver mockObserver;

    @BeforeEach
    void setUp() {
        // 1. 準備所有需要的依賴
        testUser = new User("U001", "測試員", "測試部");
        stateHandler = new StateHandler();
        eventManager = new EventManager(EventType.APPROVE); // 我們的鏈只會觸發 APPROVE 事件
        mockObserver = new MockObserver();

        // 將 Mock 觀察者註冊到事件管理器中
        eventManager.subscribe(EventType.APPROVE, mockObserver);

        // 2. 組裝責任鏈
        Approver manager = new ManagerApprover("王經理");
        Approver director = new DirectorApprover("李總監");
        manager.setNext(director);
        
        // 3. 為鏈上的所有節點注入依賴
        this.chain = manager;
        injectDependenciesToChain(this.chain);
    }

    // 輔助方法，與 Facade 中的一樣
    private void injectDependenciesToChain(Approver approver) {
        if (approver == null) return;
        approver.setDependencies(this.stateHandler, this.eventManager);
        injectDependenciesToChain(approver.getNext());
    }

    @Test
    @DisplayName("當責任鏈走完並核准時，應觸發 APPROVE 事件通知")
    void shouldNotifyObserverWhenChainApproves() {
        // Arrange
        ExpenseApplication highAmountApp = new ExpenseApplication(
                testUser, new BigDecimal("8000.00"), "高金額測試");
        highAmountApp.setStatus(ApprovalStatus.PENDING); // 模擬提審後的狀態

        // Act
        chain.handle(highAmountApp);

        // Assert
        // 1. 驗證申請單的最終狀態
        assertThat(highAmountApp.getStatus()).isEqualTo(ApprovalStatus.APPROVED);
        
        // 2. 驗證我們的 Mock 觀察者是否被呼叫了！
        assertThat(mockObserver.wasCalled).isTrue();
        assertThat(mockObserver.callCount).isEqualTo(1);
        assertThat(mockObserver.receivedEventType).isEqualTo(EventType.APPROVE);
        assertThat(mockObserver.receivedApplication).isEqualTo(highAmountApp);
    }

    @Test
    @DisplayName("如果責任鏈中途被中斷 (未實作)，則不應觸發 APPROVE 事件")
    void shouldNotNotifyObserverIfChainIsInterrupted() {
        // 這個測試案例展示了如何測試「不發生」的情況
        // Arrange
        // 假設我們未來有一個會駁回的 ManagerApprover
        // ManagerApprover rejectingManager = new RejectingManagerApprover(...);
        // ... 組裝一個會失敗的鏈 ...

        // Act
        // chain.handle(application);

        // Assert
        // 驗證觀察者從未被呼叫
        // assertThat(mockObserver.wasCalled).isFalse();
    }
}
