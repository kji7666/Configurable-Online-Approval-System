package configurable.online.approval.system.state;

import configurable.online.approval.system.domain.models.Application;

/**
 * 狀態模式的抽象狀態類別 (Abstract State)
 * 定義了所有狀態下可能發生的事件/操作。
 * 提供預設實作，表示在該狀態下「不允許此操作」。
 */
public abstract class State {

    protected final String OPERATION_NOT_ALLOWED = "此狀態不允許該操作";

    /**
     * 提審
     * @param application 申請單
     * @return 操作結果
     */
    public Result submit(Application application) {
        return new Result("0001", OPERATION_NOT_ALLOWED);
    }

    /**
     * 核准
     * @param application 申請單
     * @return 操作結果
     */
    public Result approve(Application application) {
        return new Result("0001", OPERATION_NOT_ALLOWED);
    }

    /**
     * 駁回
     * @param application 申請單
     * @return 操作結果
     */
    public Result reject(Application application) {
        return new Result("0001", OPERATION_NOT_ALLOWED);
    }

    /**
     * 撤銷
     * @param application 申請單
     * @return 操作結果
     */
    public Result withdraw(Application application) {
        return new Result("0001", OPERATION_NOT_ALLOWED);
    }

    // ... 您可以根據需要添加其他操作，例如 close(), reopen() 等
}