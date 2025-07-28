package configurable.online.approval.system.observer;

/**
 * 事件類型列舉
 * 定義了系統中所有可能觸發通知的事件。
 */
public enum EventType {
    SUBMIT,     // 提審事件
    APPROVE,    // 核准事件
    REJECT,     // 駁回事件
    WITHDRAW    // 撤銷事件
}