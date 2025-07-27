package configurable.online.approval.system.domain.models;

import configurable.online.approval.system.chain.Approver; // 雖然我們說domain不依賴其他業務包，但此處是個例外
import configurable.online.approval.system.domain.enums.ApprovalStatus;
import configurable.online.approval.system.domain.enums.ApplicationType;
import java.util.Date;
import java.util.UUID;
/**
 * 申請單抽象基礎類別
 * 定義了所有申請單共通的屬性與狀態。
 */
public abstract class Application {

    protected String applicationId;
    protected ApplicationType type;
    protected ApprovalStatus status;
    protected User applicant;
    protected Date creationDate;

    // 關鍵設計：讓申請單自己攜帶它的處理流程。
    // 這個欄位將由策略模式(Strategy)在建立申請單後進行設定。
    protected Approver chainHead;

    // 保護的建構函式，只能被子類別呼叫
    protected Application(ApplicationType type, User applicant) {
        this.applicationId = UUID.randomUUID().toString(); // 自動生成ID
        this.type = type;
        this.applicant = applicant;
        this.status = ApprovalStatus.DRAFT; // 初始狀態為草稿
        this.creationDate = new Date();
    }

    // Getters and Setters for all fields...
    // 特別是 status 的 setter，將由狀態模式(State)來呼叫以更新狀態
    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }
    
    public ApprovalStatus getStatus() {
        return status;
    }

    public void setChainHead(Approver chainHead) {
        this.chainHead = chainHead;
    }

    public Approver getChainHead() {
        return chainHead;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public ApplicationType getType() {
        return type;
    }

    public void setType(ApplicationType type) {
        this.type = type;
    }

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}