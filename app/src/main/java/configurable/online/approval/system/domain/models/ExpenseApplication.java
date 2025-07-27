package configurable.online.approval.system.domain.models;

// 引入需要用到的其他類別
import configurable.online.approval.system.domain.enums.ApplicationType;
import java.math.BigDecimal;

/**
 * 費用報銷申請單
 * 繼承自 Application，並添加了報銷金額(amount)和事由(reason)等專有屬性。
 */
public class ExpenseApplication extends Application {

    // 費用報銷單的專有屬性
    private BigDecimal amount; // 報銷金額，使用 BigDecimal 確保精度
    private String reason;     // 報銷事由

    /**
     * ExpenseApplication 的建構函式
     *
     * @param applicant 申請人 (User 物件)
     * @param amount    報銷金額
     * @param reason    報銷事由
     */
    public ExpenseApplication(User applicant, BigDecimal amount, String reason) {
        // 第一步：必須呼叫父類別(Application)的建構函式，來初始化共通屬性。
        // 我們在這裡直接指定類型為 ApplicationType.EXPENSE。
        super(ApplicationType.EXPENSE, applicant);

        // 第二步：初始化這個類別自己獨有的屬性。
        this.amount = amount;
        this.reason = reason;
    }

    // --- 提供外部存取專有屬性的 Getters ---

    public BigDecimal getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
    }

    // 您也可以根據需要添加 Setters，但通常在建立後，這些核心屬性不應被輕易修改。
    // public void setAmount(BigDecimal amount) {
    //     this.amount = amount;
    // }
    //
    // public void setReason(String reason) {
    //     this.reason = reason;
    // }
}