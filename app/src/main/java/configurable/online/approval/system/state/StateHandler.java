package configurable.online.approval.system.state;

import configurable.online.approval.system.domain.enums.ApprovalStatus;
import configurable.online.approval.system.domain.models.Application;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 狀態處理器 (Context)
 * 負責維護所有狀態物件，並將客戶端的請求委派給當前申請單狀態對應的狀態物件。
 */
public class StateHandler {

    // 使用 Map 來儲存所有狀態的單一實例 (類似享元模式)
    private final Map<ApprovalStatus, State> stateMap = new ConcurrentHashMap<>();

    public StateHandler() {
        stateMap.put(ApprovalStatus.DRAFT, new DraftState());
        stateMap.put(ApprovalStatus.PENDING, new PendingState());
        stateMap.put(ApprovalStatus.APPROVED, new ApprovedState());
        stateMap.put(ApprovalStatus.REJECTED, new RejectedState());
        stateMap.put(ApprovalStatus.WITHDRAWN, new WithdrawnState()); // 假設您也建立了 WithdrawnState
        // ... 初始化所有狀態
    }

    /**
     * 提審操作
     */
    public Result submit(Application application) {
        // 1. 根據申請單的當前狀態，從 Map 中獲取對應的狀態物件
        State currentState = stateMap.get(application.getStatus());
        // 2. 將操作委派給該狀態物件
        return currentState.submit(application);
    }

    /**
     * 核准操作
     */
    public Result approve(Application application) {
        State currentState = stateMap.get(application.getStatus());
        return currentState.approve(application);
    }

    /**
     * 駁回操作
     */
    public Result reject(Application application) {
        State currentState = stateMap.get(application.getStatus());
        return currentState.reject(application);
    }
    
    /**
     * 撤銷操作
     */
    public Result withdraw(Application application) {
        State currentState = stateMap.get(application.getStatus());
        return currentState.withdraw(application);
    }
}