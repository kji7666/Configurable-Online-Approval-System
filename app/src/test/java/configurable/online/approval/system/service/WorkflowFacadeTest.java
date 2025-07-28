package configurable.online.approval.system.service;

import configurable.online.approval.system.domain.models.User;
import configurable.online.approval.system.state.Result;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("WorkflowFacade 整合測試")
class WorkflowFacadeTest {

    private WorkflowFacade facade;
    private User testUser;
    
    // 這是捕獲 System.out.println 輸出的關鍵工具
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        // 在每個測試執行前，都建立一個新的 Facade 和 User 實例
        facade = new WorkflowFacade();
        testUser = new User("T001", "整合測試員", "品質保證部");
        
        // 關鍵修正：在建立 PrintStream 時，明確指定使用 UTF-8 編碼，防止亂碼
        try {
            System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            // 這個異常在現代 Java 中幾乎不可能發生，但語法上需要處理
            // 如果真的發生，讓測試直接失敗
            throw new RuntimeException(e);

        }
    }

    @AfterEach
    void restoreStreams() {
        // 在每個測試結束後，恢復標準輸出，避免影響其他測試或日誌
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("提交低金額報銷單(<5000)應觸發正確的策略、責任鏈和觀察者")
    void shouldProcessLowAmountExpenseCorrectly() {
        // Arrange
        BigDecimal lowAmount = new BigDecimal("1000.00");
        String reason = "低金額整合測試";

        // Act: 執行我們要測試的核心方法
        Result result = facade.submitExpenseApplication(testUser, lowAmount, reason);
        
        // 關鍵修正：在将字节流转为字符串时，也明确指定使用 UTF-8
        String consoleOutput = outContent.toString(StandardCharsets.UTF_8);
        
        // 恢復標準輸出，以便在測試失敗時能看到日誌，方便除錯
        System.setOut(originalOut);
        System.out.println("--- 低金額測試控制台輸出 ---\n" + consoleOutput + "\n--------------------");


        // Assert: 驗證所有可觀測的副作用
        // 1. 驗證 Facade 的最終回傳結果
        assertThat(result.getCode()).isEqualTo("0000");
        assertThat(result.getMessage()).contains("已成功提交並完成處理");
        assertThat(result.getMessage()).contains("最終狀態: APPROVED");

        // 2. 驗證是否選擇了正確的策略
        assertThat(consoleOutput).contains("策略模式已選擇流程: LowAmountStrategy");

        // 3. 驗證責任鏈的執行路徑是否正確
        assertThat(consoleOutput).contains("直屬主管 Manager(王經理) 簽核");
        assertThat(consoleOutput).doesNotContain("部門總監 Director(李總監) 簽核");

        // 4. 驗證觀察者是否被正確觸發
        assertThat(consoleOutput).contains("【日誌紀錄】[觀察者]: 觸發了 SUBMIT 事件");
        assertThat(consoleOutput).contains("【日誌紀錄】[觀察者]: 觸發了 APPROVE 事件");
        assertThat(consoleOutput).contains("【郵件通知】[觀察者]: 正在向 整合測試員(T001) 發送郵件");
    }

    @Test
    @DisplayName("提交高金額報銷單(>=5000)應觸發正確的策略、責任鏈和觀察者")
    void shouldProcessHighAmountExpenseCorrectly() {
        // Arrange
        BigDecimal highAmount = new BigDecimal("8000.00");
        String reason = "高金額整合測試";

        // Act
        Result result = facade.submitExpenseApplication(testUser, highAmount, reason);
        
        // 關鍵修正：同样，明确指定使用 UTF-8
        String consoleOutput = outContent.toString(StandardCharsets.UTF_8);
        
        System.setOut(originalOut);
        System.out.println("--- 高金額測試控制台輸出 ---\n" + consoleOutput + "\n--------------------");

        // Assert
        // 1. 驗證 Facade 的最終回傳結果
        assertThat(result.getCode()).isEqualTo("0000");
        assertThat(result.getMessage()).contains("最終狀態: APPROVED");

        // 2. 驗證是否選擇了正確的策略
        assertThat(consoleOutput).contains("策略模式已選擇流程: HighAmountStrategy");

        // 3. 驗證責任鏈的執行路徑是否正確
        assertThat(consoleOutput).contains("直屬主管 Manager(王經理) 簽核");
        assertThat(consoleOutput).contains("部門總監 Director(李總監) 簽核");

        // 4. 驗證觀察者是否被正確觸發
        assertThat(consoleOutput).contains("【日誌紀錄】[觀察者]: 觸發了 SUBMIT 事件");
        assertThat(consoleOutput).contains("【日誌紀錄】[觀察者]: 觸發了 APPROVE 事件");
        assertThat(consoleOutput).contains("【郵件通知】[觀察者]: 正在向 整合測試員(T001) 發送郵件");
    }
}