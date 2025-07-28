package configurable.online.approval.system.observer;

import configurable.online.approval.system.domain.models.Application;
import configurable.online.approval.system.domain.models.ExpenseApplication;
import configurable.online.approval.system.domain.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EventManager 事件管理器測試")
class EventManagerTest {

    private EventManager eventManager;
    private MockObserver mockObserver;
    private Application testApplication;

    @BeforeEach
    void setUp() {
        // 建立一個只關心 APPROVE 事件的 EventManager
        eventManager = new EventManager(EventType.APPROVE, EventType.REJECT);
        mockObserver = new MockObserver();
        testApplication = new ExpenseApplication(new User("U001", "Test", "Dept"), BigDecimal.TEN, "Test");
    }

    @Test
    @DisplayName("訂閱 APPROVE 事件的觀察者應在 APPROVE 事件發生時被通知")
    void observerSubscribedToApproveShouldBeNotifiedOnApproveEvent() {
        // Arrange
        eventManager.subscribe(EventType.APPROVE, mockObserver);

        // Act
        eventManager.notify(EventType.APPROVE, testApplication);

        // Assert
        assertThat(mockObserver.wasCalled).isTrue(); // 驗證 update 方法被呼叫了
        assertThat(mockObserver.callCount).isEqualTo(1); // 驗證只被呼叫了一次
        assertThat(mockObserver.receivedEventType).isEqualTo(EventType.APPROVE); // 驗證收到的事件類型正確
        assertThat(mockObserver.receivedApplication).isEqualTo(testApplication); // 驗證收到的申請單物件正確
    }

    @Test
    @DisplayName("訂閱 APPROVE 事件的觀察者不應在 REJECT 事件發生時被通知")
    void observerSubscribedToApproveShouldNotBeNotifiedOnRejectEvent() {
        // Arrange
        eventManager.subscribe(EventType.APPROVE, mockObserver);

        // Act
        eventManager.notify(EventType.REJECT, testApplication); // 發布一個不同的事件

        // Assert
        assertThat(mockObserver.wasCalled).isFalse(); // 驗證 update 方法從未被呼叫
        assertThat(mockObserver.callCount).isEqualTo(0);
    }

    @Test
    @DisplayName("取消訂閱後，觀察者不應再收到通知")
    void observerShouldNotBeNotifiedAfterUnsubscribing() {
        // Arrange
        eventManager.subscribe(EventType.APPROVE, mockObserver);
        eventManager.unsubscribe(EventType.APPROVE, mockObserver); // 立即取消訂閱

        // Act
        eventManager.notify(EventType.APPROVE, testApplication);

        // Assert
        assertThat(mockObserver.wasCalled).isFalse();
    }
}