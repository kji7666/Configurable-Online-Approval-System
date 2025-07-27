package configurable.online.approval.system.domain.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat; // 引入 AssertJ

class UserTest {

    @Test
    @DisplayName("應能成功建立 User 物件並驗證其屬性")
    void shouldCreateUserAndVerifyProperties() {
        // Arrange (安排): 準備測試資料
        String userId = "U001";
        String userName = "王大明";
        String department = "工程部";

        // Act (執行): 建立 User 物件
        User user = new User(userId, userName, department);

        // Assert (斷言): 驗證結果是否符合預期
        assertThat(user).isNotNull(); // 驗證物件不是 null
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getUserName()).isEqualTo(userName);
        assertThat(user.getDepartment()).isEqualTo(department);
    }
}