package configurable.online.approval.system.state;

import configurable.online.approval.system.domain.enums.ApprovalStatus;
import configurable.online.approval.system.domain.models.Application;
import configurable.online.approval.system.domain.models.ExpenseApplication;
import configurable.online.approval.system.domain.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StateHandler 狀態處理器測試")
class StateHandlerTest {

    private StateHandler stateHandler;
    private User testUser;

    @BeforeEach
    void setUp() {
        // 在每個測試開始前，都建立一個新的 StateHandler 和 User
        stateHandler = new StateHandler();
        testUser = new User("U001", "測試員", "測試部");
    }

    /**
     * 使用 @Nested 可以將相關的測試組織在一起，讓報告更清晰。
     * 這裡是所有關於「草稿(DRAFT)」狀態的測試。
     */
    @Nested
    @DisplayName("當申請單處於草稿(DRAFT)狀態時")
    class WhenInDraftState {

        private Application application;

        @BeforeEach
        void createDraftApplication() {
            // 為這個測試群組建立一個處於 DRAFT 狀態的申請單
            application = new ExpenseApplication(testUser, new BigDecimal("100"), "測試");
            // 我們可以斷言其初始狀態是正確的
            assertThat(application.getStatus()).isEqualTo(ApprovalStatus.DRAFT);
        }

        @Test
        @DisplayName("執行 '提審(submit)' 操作應成功，且狀態變為 PENDING")
        void shouldSucceedOnSubmitAndChangeStateToPending() {
            // Act
            Result result = stateHandler.submit(application);

            // Assert
            assertThat(result.getCode()).isEqualTo("0000"); // 驗證成功代碼
            assertThat(result.getMessage()).isEqualTo("提審成功");
            assertThat(application.getStatus()).isEqualTo(ApprovalStatus.PENDING); // 驗證狀態已變更
        }

        @Test
        @DisplayName("執行 '核准(approve)' 操作應失敗，且狀態不變")
        void shouldFailOnApproveAndStateShouldNotChange() {
            // Act
            Result result = stateHandler.approve(application);

            // Assert
            assertThat(result.getCode()).isNotEqualTo("0000"); // 驗證失敗代碼
            assertThat(application.getStatus()).isEqualTo(ApprovalStatus.DRAFT); // 驗證狀態未改變
        }

        @Test
        @DisplayName("執行 '駁回(reject)' 操作應失敗，且狀態不變")
        void shouldFailOnRejectAndStateShouldNotChange() {
            // Act
            Result result = stateHandler.reject(application);

            // Assert
            assertThat(result.getCode()).isNotEqualTo("0000");
            assertThat(application.getStatus()).isEqualTo(ApprovalStatus.DRAFT);
        }
    }

    /**
     * 這裡是所有關於「簽核中(PENDING)」狀態的測試。
     */
    @Nested
    @DisplayName("當申請單處於簽核中(PENDING)狀態時")
    class WhenInPendingState {

        private Application application;

        @BeforeEach
        void createPendingApplication() {
            application = new ExpenseApplication(testUser, new BigDecimal("200"), "測試");
            // 手動將狀態設定為 PENDING 以模擬此場景
            application.setStatus(ApprovalStatus.PENDING);
        }

        @Test
        @DisplayName("執行 '核准(approve)' 操作應成功，且狀態變為 APPROVED")
        void shouldSucceedOnApproveAndChangeStateToApproved() {
            // Act
            Result result = stateHandler.approve(application);

            // Assert
            assertThat(result.getCode()).isEqualTo("0000");
            assertThat(application.getStatus()).isEqualTo(ApprovalStatus.APPROVED);
        }

        @Test
        @DisplayName("執行 '駁回(reject)' 操作應成功，且狀態變為 REJECTED")
        void shouldSucceedOnRejectAndChangeStateToRejected() {
            // Act
            Result result = stateHandler.reject(application);

            // Assert
            assertThat(result.getCode()).isEqualTo("0000");
            assertThat(application.getStatus()).isEqualTo(ApprovalStatus.REJECTED);
        }

        @Test
        @DisplayName("執行 '提審(submit)' 操作應失敗，且狀態不變")
        void shouldFailOnSubmitAndStateShouldNotChange() {
            // Act
            Result result = stateHandler.submit(application);

            // Assert
            assertThat(result.getCode()).isNotEqualTo("0000");
            assertThat(application.getStatus()).isEqualTo(ApprovalStatus.PENDING);
        }
    }
}