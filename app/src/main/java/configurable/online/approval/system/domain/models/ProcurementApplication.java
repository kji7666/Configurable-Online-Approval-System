// 根據您的專案結構，套件名稱應為此
package configurable.online.approval.system.domain.models;

// 引入需要用到的其他類別
import configurable.online.approval.system.domain.enums.ApplicationType;
import java.math.BigDecimal;

/**
 * 採購申請單
 * 繼承自 Application，並添加了採購品項、數量、單價、總金額等專有屬性。
 */
public class ProcurementApplication extends Application {

    // 採購申請單的專有屬性
    private String itemName;        // 採購品項名稱
    private int quantity;           // 數量
    private BigDecimal unitPrice;   // 單價
    private String reason;          // 採購事由

    /**
     * ProcurementApplication 的建構函式
     *
     * @param applicant  申請人 (User 物件)
     * @param itemName   採購品項名稱
     * @param quantity   數量
     * @param unitPrice  單價
     * @param reason     採購事由
     */
    public ProcurementApplication(User applicant, String itemName, int quantity, BigDecimal unitPrice, String reason) {
        // 第一步：呼叫父類別(Application)的建構函式，設定共通屬性
        super(ApplicationType.PROCUREMENT, applicant);

        // 第二步：初始化這個類別自己獨有的屬性
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.reason = reason;
    }

    // --- 提供外部存取專有屬性的 Getters ---

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public String getReason() {
        return reason;
    }

    /**
     * 計算並回傳總金額。
     * 這是一個衍生屬性，不直接儲存為欄位，而是透過計算得到，確保資料的一致性。
     * @return 總金額 (單價 * 數量)
     */
    public BigDecimal getTotalAmount() {
        // 進行必要的空值檢查，避免 NullPointerException
        if (this.unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return this.unitPrice.multiply(new BigDecimal(this.quantity));
    }
}