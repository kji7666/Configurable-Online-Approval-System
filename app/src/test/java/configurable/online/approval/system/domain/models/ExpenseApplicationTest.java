package configurable.online.approval.system.domain.models;


import configurable.online.approval.system.domain.enums.ApplicationType;
import configurable.online.approval.system.domain.enums.ApprovalStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

class ExpenseApplicationTest {

    @Test
    @DisplayName("應能成功建立費用報銷單並驗證所有父類與子類屬性")
    void shouldCreateExpenseApplicationAndVerifyAllProperties() {
        // Arrange
        User applicant = new User("U002", "李小華", "財務部");
        BigDecimal amount = new BigDecimal("1500.50");
        String reason = "購買辦公用品";

        // Act
        ExpenseApplication expenseApp = new ExpenseApplication(applicant, amount, reason);

        // Assert
        // 1. 驗證子類別自己的屬性
        assertThat(expenseApp.getAmount()).isEqualByComparingTo(amount); // 對 BigDecimal 使用 isEqualByComparingTo
        assertThat(expenseApp.getReason()).isEqualTo(reason);

        // 2. 驗證繼承自父類別的屬性
        assertThat(expenseApp.getApplicationId()).isNotNull().isNotBlank(); // 應自動生成，不為空
        assertThat(expenseApp.getType()).isEqualTo(ApplicationType.EXPENSE); // 應在建構函式中被設定
        assertThat(expenseApp.getStatus()).isEqualTo(ApprovalStatus.DRAFT);  // 初始狀態應為 DRAFT
        assertThat(expenseApp.getApplicant()).isEqualTo(applicant);
        assertThat(expenseApp.getCreationDate()).isNotNull(); // 應自動生成，不為空
    }
}