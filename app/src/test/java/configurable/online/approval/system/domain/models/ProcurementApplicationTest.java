package configurable.online.approval.system.domain.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

class ProcurementApplicationTest {

    @Test
    @DisplayName("應能正確計算採購申請單的總金額")
    void shouldCalculateTotalAmountCorrectly() {
        // Arrange
        User applicant = new User("U003", "張三", "行政部");
        ProcurementApplication procurementApp = new ProcurementApplication(
            applicant,
            "人體工學椅",
            5, // 數量
            new BigDecimal("3000.00"), // 單價
            "新進員工使用"
        );

        // Act
        BigDecimal totalAmount = procurementApp.getTotalAmount();
        BigDecimal expectedAmount = new BigDecimal("15000.00");

        // Assert
        assertThat(totalAmount).isEqualByComparingTo(expectedAmount);
    }

    @Test
    @DisplayName("當數量為零時，總金額應為零")
    void shouldReturnZeroTotalAmountWhenQuantityIsZero() {
        // Arrange: 測試邊界條件
        User applicant = new User("U003", "張三", "行政部");
        ProcurementApplication procurementApp = new ProcurementApplication(
            applicant,
            "滑鼠",
            0, // 數量為 0
            new BigDecimal("200.00"),
            "備品"
        );

        // Act
        BigDecimal totalAmount = procurementApp.getTotalAmount();

        // Assert
        assertThat(totalAmount).isEqualByComparingTo(BigDecimal.ZERO);
    }
}