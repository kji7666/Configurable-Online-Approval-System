package configurable.online.approval.system.domain.enums;

/**
 * 簽核狀態列舉
 * 定義了申請單的整個生命週期。
 */
public enum ApprovalStatus {
    DRAFT,          // 0: 草稿
    PENDING,        // 1: 簽核中
    APPROVED,       // 2: 已核准
    REJECTED,       // 3: 已駁回
    WITHDRAWN,      // 4: 已撤銷
    CLOSED          // 5: 已關閉
}
