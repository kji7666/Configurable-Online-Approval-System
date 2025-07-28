package configurable.online.approval.system.factory;

import configurable.online.approval.system.domain.models.Application;
import configurable.online.approval.system.domain.models.ExpenseApplication;
import configurable.online.approval.system.domain.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ApplicationFactory 工廠方法測試")
class ApplicationFactoryTest {

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("U001", "測試員", "測試部");
    }

    @Test
    @DisplayName("ExpenseApplicationFactory 應能成功創建 ExpenseApplication")
    void shouldCreateExpenseApplicationCorrectly() {
        // Arrange
        IApplicationFactory factory = new ExpenseApplicationFactory();
        BigDecimal amount = new BigDecimal("500.00");
        String reason = "交通費報銷";

        // Act
        Application app = factory.create(testUser, amount, reason);

        // Assert
        // 1. 驗證創建出來的物件類型是否正確
        assertThat(app).isInstanceOf(ExpenseApplication.class);

        // 2. 將其轉型並驗證其內部屬性是否正確
        ExpenseApplication expenseApp = (ExpenseApplication) app;
        assertThat(expenseApp.getApplicant()).isEqualTo(testUser);
        assertThat(expenseApp.getAmount()).isEqualByComparingTo(amount);
        assertThat(expenseApp.getReason()).isEqualTo(reason);
    }

    @Test
    @DisplayName("當傳入錯誤參數給 ExpenseApplicationFactory 時應拋出異常")
    void shouldThrowExceptionForInvalidArguments() {
        // Arrange
        IApplicationFactory factory = new ExpenseApplicationFactory();

        // Act & Assert
        // 使用 AssertJ 的 assertThatThrownBy 來驗證異常
        assertThatThrownBy(() -> {
            // 故意傳入錯誤的參數類型 (String 而不是 BigDecimal)
            factory.create(testUser, "這不是一個金額", "錯誤的測試");
        })
        .isInstanceOf(IllegalArgumentException.class) // 驗證拋出的是我們預期的異常類型
        .hasMessageContaining("需要提供 BigDecimal 金額和 String 事由"); // 驗證異常訊息
    }
    
    // 您可以為 LeaveApplicationFactory 等其他工廠添加類似的測試...
}