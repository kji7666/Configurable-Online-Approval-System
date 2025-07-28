package configurable.online.approval.system.strategy;

import configurable.online.approval.system.chain.Approver;
import configurable.online.approval.system.chain.DirectorApprover;
import configurable.online.approval.system.chain.ManagerApprover;
import configurable.online.approval.system.domain.models.Application;
import configurable.online.approval.system.domain.models.ExpenseApplication;
import configurable.online.approval.system.domain.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RoutingStrategy 路由策略測試")
class RoutingStrategyTest {

    @Test
    @DisplayName("LowAmountStrategy 應回傳一個只有 Manager 的責任鏈")
    void lowAmountStrategyShouldReturnManagerOnlyChain() {
        // Arrange
        IRoutingStrategy strategy = new LowAmountStrategy();
        Application dummyApp = new ExpenseApplication(new User("u", "n", "d"), BigDecimal.ONE, "r");

        // Act
        Approver chainHead = strategy.getRoutingChain(dummyApp);

        // Assert
        // 1. 驗證頭節點是主管
        assertThat(chainHead).isInstanceOf(ManagerApprover.class);
        // 2. 驗證主管的下一個節點是 null，表示鏈已結束
        assertThat(chainHead.getNext()).isNull();
    }

    @Test
    @DisplayName("HighAmountStrategy 應回傳一個 Manager -> Director 的責任鏈")
    void highAmountStrategyShouldReturnManagerToDirectorChain() {
        // Arrange
        IRoutingStrategy strategy = new HighAmountStrategy();
        Application dummyApp = new ExpenseApplication(new User("u", "n", "d"), BigDecimal.ONE, "r");

        // Act
        Approver chainHead = strategy.getRoutingChain(dummyApp);

        // Assert
        // 1. 驗證頭節點是主管
        assertThat(chainHead).isInstanceOf(ManagerApprover.class);

        // 2. 驗證主管的下一個節點存在且是總監
        Approver nextApprover = chainHead.getNext();
        assertThat(nextApprover).isNotNull();
        assertThat(nextApprover).isInstanceOf(DirectorApprover.class);
        
        // 3. 驗證總監的下一個節點是 null
        assertThat(nextApprover.getNext()).isNull();
    }
}